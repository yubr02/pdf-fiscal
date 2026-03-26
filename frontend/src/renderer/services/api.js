const API_BASE_URL = 'http://localhost:8080/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  });

  if (!response.ok) {
    let message = 'Erro na comunicação com o servidor.';
    try {
      const payload = await response.json();
      message = payload.message || message;
    } catch (_) {
      message = response.statusText || message;
    }
    throw new Error(message);
  }

  const contentType = response.headers.get('Content-Type') || '';
  if (contentType.includes('application/octet-stream')) {
    return response.arrayBuffer();
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export const api = {
  login: (payload) => request('/auth/login', { method: 'POST', body: JSON.stringify(payload) }),
  register: (payload) => request('/auth/register', { method: 'POST', body: JSON.stringify(payload) }),
  searchProduct: (term) => request(`/produtos/buscar?termo=${encodeURIComponent(term)}`),
  getProductByBarcode: (barcode) => request(`/produtos/codigo-barras/${encodeURIComponent(barcode)}`),
  listProducts: () => request('/produtos'),
  saveProduct: (payload, id) => request(id ? `/produtos/${id}` : '/produtos', { method: id ? 'PUT' : 'POST', body: JSON.stringify(payload) }),
  deleteProduct: (id) => request(`/produtos/${id}`, { method: 'DELETE' }),
  saveSale: (payload) => request('/vendas', { method: 'POST', body: JSON.stringify(payload) }),
  salesHistory: (params) => request(`/vendas/historico?${new URLSearchParams(params)}`),
  exportSalesHistory: (params) => request(`/vendas/historico/exportar?${new URLSearchParams(params)}`),
  searchClients: (term) => request(`/clientes/buscar?termo=${encodeURIComponent(term)}`),
  saveClient: (payload) => request('/clientes', { method: 'POST', body: JSON.stringify(payload) }),
  searchFiado: (term) => request(`/fiados/buscar?termo=${encodeURIComponent(term)}`),
  saveTributacao: (payload) => request('/tributacao', { method: 'POST', body: JSON.stringify(payload) }),
  getTributacao: (produtoId) => request(`/tributacao?produtoId=${produtoId}`),
  caixaResumo: (usuarioId, dataCaixa) => request(`/caixa/resumo?usuarioId=${usuarioId}&dataCaixa=${dataCaixa}`),
  caixaFechar: (payload) => request('/caixa/fechar', { method: 'POST', body: JSON.stringify(payload) }),
  cuponsPendentes: () => request('/cupons/pendentes'),
  lancarCupom: (id) => request(`/cupons/${id}/lancar`, { method: 'PATCH' })
};
