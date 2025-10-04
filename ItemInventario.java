
class ItemInventario {
    private Ropa ropa;
    private int cantidad;

    public ItemInventario(Ropa ropa, int cantidad) {
        this.ropa = ropa;
        this.cantidad = cantidad;
    }

    public Ropa getRopa() { return ropa; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}