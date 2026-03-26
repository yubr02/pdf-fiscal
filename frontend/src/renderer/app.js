import { api } from './services/api.js';
import { currency, dateIso, datetimeNow } from './utils/format.js';

const state = {
  auth: null,
  cart: [],
  selectedClient: null,
  currentSaleType: 'FISCAL'
};

const elements = {
  authView: document.getElementById('auth-view'),
  pdvView: document.getElementById('pdv-view'),
  loginForm: document.getElementById('login-form'),
  registerForm: document.getElementById('register-form'),
  barcodeInput: document.getElementById('barcode-input'),
  saleItemsBody: document.getElementById('sale-items-body'),
  totalVenda: document.getElementById('total-venda'),
  quantidadeItens: document.getElementById('quantidade-itens'),
  operadorNome: document.getElementById('operador-nome'),
  dataHoraAtual: document.getElementById('data-hora-atual'),
  toast: document.getElementById('toast'),
  modalOverlay: document.getElementById('modal-overlay'),
  modalTitle: document.getElementById('modal-title'),
  modalSubtitle: document.getElementById('modal-subtitle'),
  modalContent: document.getElementById('modal-content')
};

document.querySelectorAll('[data-switch-auth]').forEach((button) => {
  button.addEventListener('click', () => switchAuth(button.dataset.switchAuth));
});

document.getElementById('close-modal-btn').addEventListener('click', closeModal);
document.getElementById('clear-sale-btn').addEventListener('click', clearSale);

document.querySelectorAll('[data-action]').forEach((button) => {
  button.addEventListener('click', () => handleAction(button.dataset.action));
});

elements.loginForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  try {
    const auth = await api.login({
      usuario: document.getElementById('login-usuario').value,
      senha: document.getElementById('login-senha').value
    });
    state.auth = auth;
    elements.operadorNome.textContent = `${auth.nome} (${auth.perfil})`;
    elements.authView.classList.add('hidden');
    elements.pdvView.classList.remove('hidden');
    elements.barcodeInput.focus();
    showToast('Login realizado com sucesso.', 'success');
  } catch (error) {
    showToast(error.message, 'error');
  }
});

elements.registerForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  try {
    await api.register({
      nome: document.getElementById('cadastro-nome').value,
      usuario: document.getElementById('cadastro-usuario').value,
      senha: document.getElementById('cadastro-senha').value,
      confirmarSenha: document.getElementById('cadastro-confirmar-senha').value
    });
    showToast('Usuário cadastrado. Faça login para continuar.', 'success');
    switchAuth('login');
  } catch (error) {
    showToast(error.message, 'error');
  }
});

elements.barcodeInput.addEventListener('keydown', async (event) => {
  if (event.key !== 'Enter') return;
  const barcode = elements.barcodeInput.value.trim();
  if (!barcode) return;
  try {
    const produto = await api.getProductByBarcode(barcode);
    addItemToCart(produto);
    elements.barcodeInput.value = '';
  } catch (error) {
    showToast(error.message, 'error');
  }
});

document.addEventListener('keydown', (event) => {
  if (!state.auth) return;
  if (event.key === 'F1') { event.preventDefault(); handleAction('produto-cadastro'); }
  if (event.key === 'F2') { event.preventDefault(); handleAction('produto-busca'); }
  if (event.key === 'F3') { event.preventDefault(); handleAction('compra-fiscal'); }
  if (event.key === 'F4') { event.preventDefault(); handleAction('etiquetas'); }
  if (event.key === 'F5') { event.preventDefault(); handleAction('cupons'); }
  if (event.key === 'F6') { event.preventDefault(); handleAction('fiado'); }
  if (event.key === 'F8') { event.preventDefault(); handleAction('tributacao'); }
  if (event.key === 'F10') { event.preventDefault(); handleAction('historico'); }
  if (event.key === 'F11') { event.preventDefault(); handleAction('caixa'); }
  if (event.ctrlKey && event.key.toLowerCase() === 'r') {
    event.preventDefault();
    handleAction('compra-nao-fiscal');
  }
  if (!elements.modalOverlay.classList.contains('hidden') && event.key === 'Escape') {
    closeModal();
  }
});

setInterval(() => {
  elements.dataHoraAtual.textContent = datetimeNow();
}, 1000);

renderCart();

function switchAuth(view) {
  document.getElementById('login-card').classList.toggle('hidden', view !== 'login');
  document.getElementById('register-card').classList.toggle('hidden', view !== 'register');
}

function addItemToCart(produto) {
  const existing = state.cart.find((item) => item.id === produto.id);
  if (existing) {
    existing.quantidade += 1;
    existing.subtotal = existing.quantidade * Number(existing.precoVenda);
  } else {
    state.cart.push({
      id: produto.id,
      codigoBarras: produto.codigoBarras,
      nome: produto.nome,
      precoVenda: Number(produto.precoVenda),
      quantidade: 1,
      subtotal: Number(produto.precoVenda)
    });
  }
  renderCart();
  elements.barcodeInput.focus();
}

function renderCart() {
  elements.saleItemsBody.innerHTML = '';
  let total = 0;
  let quantidade = 0;
  state.cart.forEach((item) => {
    total += item.subtotal;
    quantidade += item.quantidade;
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${item.codigoBarras}</td>
      <td>${item.nome}</td>
      <td>${item.quantidade}</td>
      <td>${currency(item.precoVenda)}</td>
      <td>${currency(item.subtotal)}</td>
    `;
    elements.saleItemsBody.appendChild(row);
  });
  elements.totalVenda.textContent = currency(total);
  elements.quantidadeItens.textContent = quantidade;
}

function getCartTotal() {
  return state.cart.reduce((acc, item) => acc + item.subtotal, 0);
}

function clearSale() {
  state.cart = [];
  state.selectedClient = null;
  renderCart();
  elements.barcodeInput.focus();
}

function handleAction(action) {
  if (action === 'compra-fiscal') return openRecebimentoModal('FISCAL');
  if (action === 'compra-nao-fiscal') return openRecebimentoModal('NAO_FISCAL');
  if (action === 'produto-busca') return openProductSearchModal();
  if (action === 'produto-cadastro') return openProductManagementModal();
  if (action === 'fiado') return openFiadoModal();
  if (action === 'tributacao') return openTributacaoModal();
  if (action === 'historico') return openHistoricoModal();
  if (action === 'caixa') return openCaixaModal();
  if (action === 'cupons') return openCuponsModal();
  if (action === 'etiquetas') return openEtiquetasModal();
}

function openModal(title, subtitle, content) {
  elements.modalTitle.textContent = title;
  elements.modalSubtitle.textContent = subtitle || '';
  elements.modalContent.innerHTML = '';
  elements.modalContent.appendChild(content);
  elements.modalOverlay.classList.remove('hidden');
}

function closeModal() {
  elements.modalOverlay.classList.add('hidden');
  elements.modalContent.innerHTML = '';
  elements.barcodeInput.focus();
}

function createContentWrapper(html) {
  const wrapper = document.createElement('div');
  wrapper.innerHTML = html.trim();
  return wrapper.firstElementChild;
}

function openRecebimentoModal(tipoVenda) {
  if (!state.cart.length) {
    showToast('Adicione produtos antes de finalizar a venda.', 'error');
    return;
  }

  state.currentSaleType = tipoVenda;
  const template = document.getElementById('recebimento-template');
  const content = document.importNode(template.content, true);
  const total = getCartTotal();

  openModal(
    tipoVenda === 'FISCAL' ? 'Compra Fiscal' : 'Compra Não Fiscal',
    'Confirme o recebimento, troco e cliente quando necessário.',
    content
  );

  document.getElementById('receive-total-value').textContent = currency(total);
  document.getElementById('receive-total-metric').textContent = currency(total);
  document.getElementById('receipt-preview-total').textContent = currency(total);
  document.getElementById('receipt-preview-type').textContent = tipoVenda;

  const paidInput = document.getElementById('receive-paid-value');
  const paymentSelect = document.getElementById('receive-payment-method');
  const clientSearchInput = document.getElementById('receive-client-search');
  const clientResults = document.getElementById('receive-client-results');
  const updateChange = () => {
    const troco = Number(paidInput.value || 0) - total;
    document.getElementById('receive-change-metric').textContent = currency(Math.max(troco, 0));
  };
  paidInput.value = total.toFixed(2);
  updateChange();

  paidInput.addEventListener('input', updateChange);
  document.getElementById('receipt-identified-btn').addEventListener('click', () => {
    showToast('Estrutura pronta para identificação fiscal do cupom.', 'success');
  });

  clientSearchInput.addEventListener('input', async () => {
    const value = clientSearchInput.value.trim();
    if (value.length < 2) {
      clientResults.innerHTML = '';
      return;
    }
    const clients = await api.searchClients(value);
    clientResults.innerHTML = '';
    clients.forEach((client) => {
      const button = document.createElement('button');
      button.className = 'list-item-btn';
      button.textContent = `${client.nome} | ${client.cpfCnpj || 'Sem CPF'} | ${client.telefone || 'Sem telefone'}`;
      button.addEventListener('click', () => {
        state.selectedClient = client;
        clientSearchInput.value = client.nome;
        clientResults.innerHTML = '';
      });
      clientResults.appendChild(button);
    });
  });

  document.getElementById('confirm-sale-btn').addEventListener('click', async () => {
    try {
      const payload = {
        usuarioId: state.auth.id,
        clienteId: state.selectedClient?.id || null,
        tipoVenda,
        formaPagamento: paymentSelect.value,
        valorPago: Number(paidInput.value || 0),
        desconto: 0,
        acrescimo: 0,
        gerarFiado: paymentSelect.value === 'FIADO',
        itens: state.cart.map((item) => ({ produtoId: item.id, quantidade: item.quantidade }))
      };
      const venda = await api.saveSale(payload);
      showToast(`Venda ${venda.numeroVenda} concluída com sucesso.`, 'success');
      window.print();
      clearSale();
      closeModal();
    } catch (error) {
      showToast(error.message, 'error');
    }
  });
}

function openProductSearchModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <label>Buscar produto<input id="search-product-input" type="text" placeholder="Nome, código de barras ou código interno" /></label>
      <div id="search-product-results" class="table-scroll"></div>
    </div>
  `);
  openModal('Pesquisar Produtos', 'Consulta rápida de itens com estoque e preço.', content);
  const input = document.getElementById('search-product-input');
  const results = document.getElementById('search-product-results');
  input.focus();
  input.addEventListener('input', async () => {
    const list = await api.searchProduct(input.value);
    results.innerHTML = renderProductTable(list, true);
    results.querySelectorAll('[data-add-product]').forEach((button) => {
      button.addEventListener('click', async () => {
        const product = await api.getProductByBarcode(button.dataset.barcode);
        addItemToCart(product);
        closeModal();
      });
    });
  });
}

function openProductManagementModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <form id="product-form" class="form-grid product-form-grid">
        <label>Código interno<input name="codigoInterno" required /></label>
        <label>Código de barras<input name="codigoBarras" required /></label>
        <label>Nome do produto<input name="nome" required /></label>
        <label>Descrição<input name="descricao" /></label>
        <label>Categoria<input name="categoria" /></label>
        <label>Subcategoria<input name="subcategoria" /></label>
        <label>Preço custo<input name="precoCusto" type="number" step="0.01" required /></label>
        <label>Preço venda<input name="precoVenda" type="number" step="0.01" required /></label>
        <label>Estoque<input name="estoque" type="number" step="0.001" required /></label>
        <label>Unidade<input name="unidade" value="UN" required /></label>
        <label>NCM<input name="ncm" /></label>
        <label>CEST<input name="cest" /></label>
        <label>CFOP<input name="cfop" /></label>
        <label>CST<input name="cst" /></label>
        <label>CSOSN<input name="csosn" /></label>
        <label>Alíquota<input name="aliquota" type="number" step="0.01" /></label>
        <label>Origem<input name="origem" value="0 - Nacional" /></label>
        <button type="submit" class="primary-btn">Salvar produto</button>
      </form>
      <div id="product-management-list" class="table-scroll"></div>
    </div>
  `);
  openModal('Cadastro de Produtos', 'Cadastro completo para incluir e inativar produtos.', content);

  const form = document.getElementById('product-form');
  const listContainer = document.getElementById('product-management-list');
  refreshProductManagementList(listContainer);

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(form).entries());
    await api.saveProduct({
      ...data,
      precoCusto: Number(data.precoCusto),
      precoVenda: Number(data.precoVenda),
      estoque: Number(data.estoque),
      aliquota: Number(data.aliquota || 0)
    });
    form.reset();
    showToast('Produto salvo com sucesso.', 'success');
    refreshProductManagementList(listContainer);
  });
}

async function refreshProductManagementList(container) {
  const list = await api.listProducts();
  container.innerHTML = renderProductTable(list, false);
  container.querySelectorAll('[data-delete-product]').forEach((button) => {
    button.addEventListener('click', async () => {
      await api.deleteProduct(button.dataset.deleteProduct);
      showToast('Produto inativado.', 'success');
      refreshProductManagementList(container);
    });
  });
}

function renderProductTable(list, addMode) {
  return `
    <table class="pdv-table compact">
      <thead><tr><th>Código</th><th>Nome</th><th>Preço</th><th>Estoque</th><th>Ação</th></tr></thead>
      <tbody>
        ${list.map((item) => `
          <tr>
            <td>${item.codigoBarras}</td>
            <td>${item.nome}</td>
            <td>${currency(item.precoVenda)}</td>
            <td>${item.estoque}</td>
            <td>${addMode
              ? `<button class="secondary-btn small-btn" data-add-product data-barcode="${item.codigoBarras}">Adicionar</button>`
              : `<button class="danger-btn small-btn" data-delete-product="${item.id}">Inativar</button>`}
            </td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;
}

function openFiadoModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <label>Pesquisar fiado<input id="fiado-input" placeholder="Nome, CPF ou telefone" /></label>
      <div id="fiado-results" class="table-scroll"></div>
    </div>
  `);
  openModal('Pesquisar Fiado', 'Consulta de débitos pendentes de clientes.', content);
  const input = document.getElementById('fiado-input');
  const results = document.getElementById('fiado-results');
  input.addEventListener('input', async () => {
    const list = await api.searchFiado(input.value);
    results.innerHTML = `
      <table class="pdv-table compact">
        <thead><tr><th>Cliente</th><th>Venda</th><th>Valor</th><th>Vencimento</th><th>Status</th></tr></thead>
        <tbody>${list.map((item) => `
          <tr>
            <td>${item.cliente}</td>
            <td>${item.numeroVenda}</td>
            <td>${currency(item.valorPendente)}</td>
            <td>${item.dataVencimento}</td>
            <td>${item.statusFiado}</td>
          </tr>
        `).join('')}</tbody>
      </table>
    `;
  });
}

function openTributacaoModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <label>Buscar produto<input id="tributacao-product-search" placeholder="Digite o nome ou código" /></label>
      <div id="tributacao-search-results" class="list-box compact-list"></div>
      <form id="tributacao-form" class="form-grid hidden">
        <input type="hidden" name="produtoId" />
        <label>NCM<input name="ncm" /></label>
        <label>CEST<input name="cest" /></label>
        <label>CFOP<input name="cfop" value="5102" /></label>
        <label>CST<input name="cst" value="102" /></label>
        <label>CSOSN<input name="csosn" /></label>
        <label>Alíquota<input name="aliquota" type="number" step="0.01" /></label>
        <label>Origem<input name="origem" value="0 - Nacional" /></label>
        <button type="submit" class="primary-btn">Salvar tributação</button>
      </form>
    </div>
  `);
  openModal('Manutenção Tributária', 'Edite NCM, CEST, CFOP, CST, CSOSN, alíquota e origem.', content);

  const searchInput = document.getElementById('tributacao-product-search');
  const results = document.getElementById('tributacao-search-results');
  const form = document.getElementById('tributacao-form');

  searchInput.addEventListener('input', async () => {
    const products = await api.searchProduct(searchInput.value);
    results.innerHTML = '';
    products.forEach((product) => {
      const button = document.createElement('button');
      button.className = 'list-item-btn';
      button.textContent = `${product.codigoBarras} | ${product.nome}`;
      button.addEventListener('click', () => {
        form.classList.remove('hidden');
        form.produtoId.value = product.id;
        form.ncm.value = product.ncm || '';
        form.cest.value = product.cest || '';
        form.cfop.value = product.cfop || '';
        form.cst.value = product.cst || '';
        form.csosn.value = product.csosn || '';
        form.aliquota.value = product.aliquota || 0;
        form.origem.value = product.origem || '';
      });
      results.appendChild(button);
    });
  });

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(form).entries());
    await api.saveTributacao({
      ...data,
      produtoId: Number(data.produtoId),
      aliquota: Number(data.aliquota || 0)
    });
    showToast('Dados tributários atualizados.', 'success');
  });
}

function openHistoricoModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <div class="toolbar-row">
        <label>Data inicial<input id="history-start" type="date" value="${dateIso(new Date(Date.now() - 7 * 86400000))}" /></label>
        <label>Data final<input id="history-end" type="date" value="${dateIso()}" /></label>
        <button id="history-load" class="secondary-btn">Consultar</button>
        <button id="history-export" class="primary-btn">Exportar Excel</button>
      </div>
      <div id="history-results" class="table-scroll"></div>
    </div>
  `);
  openModal('Histórico de Vendas', 'Filtro por período com exportação em Excel.', content);

  const load = async () => {
    const params = {
      dataInicial: document.getElementById('history-start').value,
      dataFinal: document.getElementById('history-end').value,
      usuarioId: state.auth.id
    };
    const list = await api.salesHistory(params);
    document.getElementById('history-results').innerHTML = `
      <table class="pdv-table compact">
        <thead><tr><th>Número</th><th>Data</th><th>Tipo</th><th>Pagamento</th><th>Itens</th><th>Total</th></tr></thead>
        <tbody>${list.map((item) => `
          <tr>
            <td>${item.numeroVenda}</td>
            <td>${item.dataHoraVenda.replace('T', ' ')}</td>
            <td>${item.tipoVenda}</td>
            <td>${item.formaPagamento}</td>
            <td>${item.quantidadeItens}</td>
            <td>${currency(item.total)}</td>
          </tr>
        `).join('')}</tbody>
      </table>
    `;
  };

  document.getElementById('history-load').addEventListener('click', load);
  document.getElementById('history-export').addEventListener('click', async () => {
    const params = {
      dataInicial: document.getElementById('history-start').value,
      dataFinal: document.getElementById('history-end').value,
      usuarioId: state.auth.id
    };
    const data = await api.exportSalesHistory(params);
    const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'historico-vendas.xlsx';
    link.click();
    URL.revokeObjectURL(url);
    showToast('Exportação gerada para download.', 'success');
  });

  load();
}

function openCaixaModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <label>Data do caixa<input id="cash-date" type="date" value="${dateIso()}" /></label>
      <div id="cash-summary" class="summary-grid"></div>
      <button id="cash-refresh" class="secondary-btn">Atualizar resumo</button>
      <button id="cash-close" class="primary-btn">Fechar caixa do dia</button>
    </div>
  `);
  openModal('Fechamento do Caixa', 'Resumo diário por operador.', content);

  const render = async () => {
    const resumo = await api.caixaResumo(state.auth.id, document.getElementById('cash-date').value);
    document.getElementById('cash-summary').innerHTML = `
      <div class="summary-card"><span>Total vendido</span><strong>${currency(resumo.totalVendido)}</strong></div>
      <div class="summary-card"><span>Total fiscal</span><strong>${currency(resumo.totalFiscal)}</strong></div>
      <div class="summary-card"><span>Total não fiscal</span><strong>${currency(resumo.totalNaoFiscal)}</strong></div>
      <div class="summary-card"><span>Quantidade de vendas</span><strong>${resumo.quantidadeVendas}</strong></div>
      <div class="summary-card"><span>Status</span><strong>${resumo.fechado ? 'Fechado' : 'Aberto'}</strong></div>
    `;
  };

  document.getElementById('cash-refresh').addEventListener('click', render);
  document.getElementById('cash-close').addEventListener('click', async () => {
    const resumo = await api.caixaFechar({
      usuarioId: state.auth.id,
      dataCaixa: document.getElementById('cash-date').value
    });
    showToast(`Caixa fechado. Total vendido: ${currency(resumo.totalVendido)}.`, 'success');
    render();
  });

  render();
}

function openCuponsModal() {
  const content = createContentWrapper(`<div id="cupom-results" class="table-scroll"></div>`);
  openModal('Cupons Fiscais Não Lançados', 'Cupons pendentes para conferência e lançamento.', content);

  const load = async () => {
    const list = await api.cuponsPendentes();
    document.getElementById('cupom-results').innerHTML = `
      <table class="pdv-table compact">
        <thead><tr><th>Número</th><th>Data</th><th>Valor</th><th>Status</th><th>Ação</th></tr></thead>
        <tbody>${list.map((item) => `
          <tr>
            <td>${item.numeroCupom}</td>
            <td>${item.dataHoraCupom.replace('T', ' ')}</td>
            <td>${currency(item.valor)}</td>
            <td>${item.statusCupom}</td>
            <td><button class="primary-btn small-btn" data-cupom-id="${item.id}">Marcar lançado</button></td>
          </tr>
        `).join('')}</tbody>
      </table>
    `;

    document.querySelectorAll('[data-cupom-id]').forEach((button) => {
      button.addEventListener('click', async () => {
        await api.lancarCupom(button.dataset.cupomId);
        showToast('Cupom marcado como lançado.', 'success');
        load();
      });
    });
  };

  load();
}

function openEtiquetasModal() {
  const content = createContentWrapper(`
    <div class="modal-stack">
      <label>Pesquisar produtos para etiqueta<input id="tag-product-search" placeholder="Nome, código ou referência" /></label>
      <div id="tag-product-results" class="list-box compact-list"></div>
      <div id="tag-preview-area" class="label-preview-grid"></div>
      <button id="tag-print-btn" class="primary-btn">Imprimir etiquetas 100x30 mm</button>
    </div>
  `);
  openModal('Etiquetas de Gôndola', 'Selecione vários produtos e gere a grade para impressão.', content);

  const selected = [];
  document.getElementById('tag-product-search').addEventListener('input', async (event) => {
    const list = await api.searchProduct(event.target.value);
    const resultBox = document.getElementById('tag-product-results');
    resultBox.innerHTML = '';
    list.forEach((product) => {
      const button = document.createElement('button');
      button.className = 'list-item-btn';
      button.textContent = `${product.nome} | ${currency(product.precoVenda)} | ${product.codigoBarras}`;
      button.addEventListener('click', () => {
        selected.push(product);
        renderLabels(selected);
      });
      resultBox.appendChild(button);
    });
  });

  document.getElementById('tag-print-btn').addEventListener('click', () => window.print());
}

function renderLabels(list) {
  document.getElementById('tag-preview-area').innerHTML = list.map((product) => `
    <div class="label-card">
      <strong>${product.nome}</strong>
      <span class="label-price">${currency(product.precoVenda)}</span>
      <small>${product.unidade || 'UN'} | ${product.codigoBarras}</small>
    </div>
  `).join('');
}

function showToast(message, type = 'success') {
  elements.toast.textContent = message;
  elements.toast.className = `toast ${type}`;
  setTimeout(() => {
    elements.toast.className = 'toast hidden';
  }, 3200);
}
