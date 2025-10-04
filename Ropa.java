
class Ropa {
    private int id;
    private String tipo;
    private String talla;
    private double precio;

    public Ropa(int id, String tipo, String talla, double precio) {
        this.id = id;
        this.tipo = tipo;
        this.talla = talla;
        this.precio = precio;
    }

    // Getters / Setters
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public String toString() {
        return String.format("ID:%d | %s T:%s | $%.2f", id, tipo, talla, precio);
    }
}
