class Pedido {
    private List<LineaPedido> lineas = new ArrayList<LineaPedido>();
    private Envio envio;
    private Cliente cliente;

    public void agregarLinea(LineaPedido l) { lineas.add(l); }
    public void setEnvio(Envio e) { this.envio = e; }
    public void setCliente(Cliente c) { this.cliente = c; }
    public Cliente getCliente() { return cliente; }

    public double totalProductos() {
        double t = 0;
        for (LineaPedido l : lineas) t += l.getSubtotal();
        return t;
    }

    public double total() {
        double tp = totalProductos();
        return tp + (envio == null ? 0 : envio.costo(tp));
    }

    public String recibo() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECIBO =====\n");
        if (cliente != null) sb.append(cliente.toString()).append("\n");
        for (LineaPedido l : lineas) {
            sb.append(" - ").append(l.toString()).append("\n");
        }
        double tp = totalProductos();
        sb.append(String.format("Subtotal: $%.2f\n", tp));
        if (envio != null) {
            double ce = envio.costo(tp);
            sb.append("Envío: ").append(envio.toString()).append(String.format(" | Costo: $%.2f\n", ce));
            sb.append(String.format("TOTAL A PAGAR: $%.2f\n", tp + ce));
        } else {
            sb.append(String.format("TOTAL A PAGAR: $%.2f\n", tp));
        }
        sb.append("==================\n");
        return sb.toString();
    }
}
