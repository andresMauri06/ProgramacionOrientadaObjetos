
class LineaPedido {
    private Ropa ropa;
    private int cantidad;

    public LineaPedido(Ropa ropa, int cantidad) {
        this.ropa = ropa;
        this.cantidad = cantidad;
    }

    public Ropa getRopa() { return ropa; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return ropa.getPrecio() * cantidad; }

    @Override
    public String toString() {
        return String.format("%s x%d -> $%.2f", ropa.toString(), cantidad, getSubtotal());
    }
}