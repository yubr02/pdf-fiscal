export function currency(value) {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(Number(value || 0));
}

export function datetimeNow() {
  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'short',
    timeStyle: 'medium'
  }).format(new Date());
}

export function dateIso(value = new Date()) {
  const date = new Date(value);
  return date.toISOString().slice(0, 10);
}
