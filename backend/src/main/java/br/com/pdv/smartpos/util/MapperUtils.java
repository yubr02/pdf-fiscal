package br.com.pdv.smartpos.util;

import br.com.pdv.smartpos.dto.ClienteResponse;
import br.com.pdv.smartpos.dto.CupomNaoLancadoResponse;
import br.com.pdv.smartpos.dto.FiadoResponse;
import br.com.pdv.smartpos.dto.ItemVendaResponse;
import br.com.pdv.smartpos.dto.ProdutoResponse;
import br.com.pdv.smartpos.dto.TributacaoResponse;
import br.com.pdv.smartpos.dto.VendaResponse;
import br.com.pdv.smartpos.model.Cliente;
import br.com.pdv.smartpos.model.CupomNaoLancado;
import br.com.pdv.smartpos.model.Fiado;
import br.com.pdv.smartpos.model.ItemVenda;
import br.com.pdv.smartpos.model.ManutencaoTributaria;
import br.com.pdv.smartpos.model.Produto;
import br.com.pdv.smartpos.model.Venda;

import java.util.List;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static ProdutoResponse toProdutoResponse(Produto produto) {
        return new ProdutoResponse(
            produto.getId(),
            produto.getCodigoInterno(),
            produto.getCodigoBarras(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getCategoria(),
            produto.getSubcategoria(),
            produto.getPrecoCusto(),
            produto.getPrecoVenda(),
            produto.getEstoque(),
            produto.getUnidade(),
            produto.getNcm(),
            produto.getCest(),
            produto.getCfop(),
            produto.getCst(),
            produto.getCsosn(),
            produto.getAliquota(),
            produto.getOrigem(),
            produto.getAtivo()
        );
    }

    public static ClienteResponse toClienteResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getCpfCnpj(), cliente.getTelefone(), cliente.getEndereco());
    }

    public static ItemVendaResponse toItemVendaResponse(ItemVenda item) {
        return new ItemVendaResponse(
            item.getId(),
            item.getProduto().getId(),
            item.getProduto().getCodigoBarras(),
            item.getProduto().getNome(),
            item.getQuantidade(),
            item.getValorUnitario(),
            item.getSubtotal()
        );
    }

    public static VendaResponse toVendaResponse(Venda venda) {
        List<ItemVendaResponse> itens = venda.getItens().stream().map(MapperUtils::toItemVendaResponse).toList();
        return new VendaResponse(
            venda.getId(),
            venda.getNumeroVenda(),
            venda.getUsuario().getNome(),
            venda.getCliente() != null ? venda.getCliente().getNome() : null,
            venda.getTipoVenda(),
            venda.getStatusVenda(),
            venda.getSubtotal(),
            venda.getDesconto(),
            venda.getAcrescimo(),
            venda.getTotal(),
            venda.getValorPago(),
            venda.getTroco(),
            venda.getFormaPagamento(),
            venda.getQuantidadeItens(),
            venda.getDataHoraVenda(),
            itens
        );
    }

    public static FiadoResponse toFiadoResponse(Fiado fiado) {
        return new FiadoResponse(
            fiado.getId(),
            fiado.getCliente().getNome(),
            fiado.getCliente().getCpfCnpj(),
            fiado.getCliente().getTelefone(),
            fiado.getVenda().getNumeroVenda(),
            fiado.getValorPendente(),
            fiado.getDataVencimento(),
            fiado.getStatusFiado()
        );
    }

    public static TributacaoResponse toTributacaoResponse(ManutencaoTributaria tributacao) {
        return new TributacaoResponse(
            tributacao.getId(),
            tributacao.getProduto().getId(),
            tributacao.getProduto().getNome(),
            tributacao.getNcm(),
            tributacao.getCest(),
            tributacao.getCfop(),
            tributacao.getCst(),
            tributacao.getCsosn(),
            tributacao.getAliquota(),
            tributacao.getOrigem()
        );
    }

    public static CupomNaoLancadoResponse toCupomResponse(CupomNaoLancado cupom) {
        return new CupomNaoLancadoResponse(cupom.getId(), cupom.getNumeroCupom(), cupom.getDataHoraCupom(), cupom.getValor(), cupom.getStatusCupom());
    }
}
