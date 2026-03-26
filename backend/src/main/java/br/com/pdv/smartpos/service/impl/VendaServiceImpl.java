package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.ItemVendaRequest;
import br.com.pdv.smartpos.dto.VendaRequest;
import br.com.pdv.smartpos.dto.VendaResponse;
import br.com.pdv.smartpos.exception.BusinessException;
import br.com.pdv.smartpos.exception.NotFoundException;
import br.com.pdv.smartpos.model.Cliente;
import br.com.pdv.smartpos.model.Fiado;
import br.com.pdv.smartpos.model.ItemVenda;
import br.com.pdv.smartpos.model.Produto;
import br.com.pdv.smartpos.model.Usuario;
import br.com.pdv.smartpos.model.Venda;
import br.com.pdv.smartpos.repository.ClienteRepository;
import br.com.pdv.smartpos.repository.FiadoRepository;
import br.com.pdv.smartpos.repository.ProdutoRepository;
import br.com.pdv.smartpos.repository.UsuarioRepository;
import br.com.pdv.smartpos.repository.VendaRepository;
import br.com.pdv.smartpos.service.VendaService;
import br.com.pdv.smartpos.util.MapperUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VendaServiceImpl implements VendaService {

    private final VendaRepository vendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final FiadoRepository fiadoRepository;

    public VendaServiceImpl(
        VendaRepository vendaRepository,
        UsuarioRepository usuarioRepository,
        ClienteRepository clienteRepository,
        ProdutoRepository produtoRepository,
        FiadoRepository fiadoRepository
    ) {
        this.vendaRepository = vendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.fiadoRepository = fiadoRepository;
    }

    @Override
    @Transactional
    public VendaResponse registrar(VendaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        Cliente cliente = request.clienteId() != null
            ? clienteRepository.findById(request.clienteId()).orElseThrow(() -> new NotFoundException("Cliente não encontrado."))
            : null;

        Venda venda = new Venda();
        venda.setNumeroVenda(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        venda.setUsuario(usuario);
        venda.setCliente(cliente);
        venda.setTipoVenda(request.tipoVenda());
        venda.setStatusVenda("FINALIZADA");
        venda.setFormaPagamento(request.formaPagamento());
        venda.setDataHoraVenda(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;
        int quantidadeItens = 0;
        List<ItemVenda> itens = new ArrayList<>();

        for (ItemVendaRequest itemRequest : request.itens()) {
            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));

            if (produto.getEstoque().compareTo(itemRequest.quantidade()) < 0) {
                throw new BusinessException("Estoque insuficiente para o produto " + produto.getNome() + ".");
            }

            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.quantidade());
            item.setValorUnitario(produto.getPrecoVenda());
            item.setSubtotal(produto.getPrecoVenda().multiply(itemRequest.quantidade()));
            subtotal = subtotal.add(item.getSubtotal());
            quantidadeItens += itemRequest.quantidade().intValue();
            itens.add(item);

            produto.setEstoque(produto.getEstoque().subtract(itemRequest.quantidade()));
            produtoRepository.save(produto);
        }

        BigDecimal desconto = request.desconto() != null ? request.desconto() : BigDecimal.ZERO;
        BigDecimal acrescimo = request.acrescimo() != null ? request.acrescimo() : BigDecimal.ZERO;
        BigDecimal total = subtotal.subtract(desconto).add(acrescimo);
        BigDecimal troco = request.valorPago().subtract(total);

        if (troco.compareTo(BigDecimal.ZERO) < 0 && !"FIADO".equalsIgnoreCase(request.formaPagamento())) {
            throw new BusinessException("O valor pago é menor que o total da venda.");
        }

        venda.setSubtotal(subtotal);
        venda.setDesconto(desconto);
        venda.setAcrescimo(acrescimo);
        venda.setTotal(total);
        venda.setValorPago(request.valorPago());
        venda.setTroco(troco.max(BigDecimal.ZERO));
        venda.setQuantidadeItens(quantidadeItens);
        venda.setItens(itens);

        Venda saved = vendaRepository.save(venda);

        if (Boolean.TRUE.equals(request.gerarFiado()) || "FIADO".equalsIgnoreCase(request.formaPagamento())) {
            if (cliente == null) {
                throw new BusinessException("Vendas fiadas exigem cliente vinculado.");
            }
            Fiado fiado = new Fiado();
            fiado.setCliente(cliente);
            fiado.setVenda(saved);
            fiado.setValorPendente(total);
            fiado.setDataVencimento(LocalDate.now().plusDays(30));
            fiado.setStatusFiado("PENDENTE");
            fiadoRepository.save(fiado);
        }

        return MapperUtils.toVendaResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendaResponse> historico(LocalDate dataInicial, LocalDate dataFinal, Long usuarioId) {
        LocalDate inicio = dataInicial != null ? dataInicial : LocalDate.now().minusDays(30);
        LocalDate fim = dataFinal != null ? dataFinal : LocalDate.now();
        LocalDateTime dataHoraInicio = inicio.atStartOfDay();
        LocalDateTime dataHoraFim = fim.atTime(LocalTime.MAX);

        List<Venda> vendas = usuarioId != null
            ? vendaRepository.findByUsuarioIdAndDataHoraVendaBetweenOrderByDataHoraVendaDesc(usuarioId, dataHoraInicio, dataHoraFim)
            : vendaRepository.findByDataHoraVendaBetweenOrderByDataHoraVendaDesc(dataHoraInicio, dataHoraFim);

        return vendas.stream().map(MapperUtils::toVendaResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportarHistorico(LocalDate dataInicial, LocalDate dataFinal, Long usuarioId) {
        List<VendaResponse> vendas = historico(dataInicial, dataFinal, usuarioId);

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Historico de Vendas");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Numero");
            header.createCell(1).setCellValue("Data");
            header.createCell(2).setCellValue("Operador");
            header.createCell(3).setCellValue("Tipo");
            header.createCell(4).setCellValue("Pagamento");
            header.createCell(5).setCellValue("Itens");
            header.createCell(6).setCellValue("Total");

            int rowIndex = 1;
            for (VendaResponse venda : vendas) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(venda.numeroVenda());
                row.createCell(1).setCellValue(venda.dataHoraVenda().toString());
                row.createCell(2).setCellValue(venda.operador());
                row.createCell(3).setCellValue(venda.tipoVenda());
                row.createCell(4).setCellValue(venda.formaPagamento());
                row.createCell(5).setCellValue(venda.quantidadeItens());
                row.createCell(6).setCellValue(venda.total().doubleValue());
            }

            for (int i = 0; i <= 6; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new BusinessException("Não foi possível gerar o Excel do histórico.");
        }
    }
}
