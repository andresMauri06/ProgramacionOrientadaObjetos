
class Envio {
    public enum Metodo { ESTANDAR, EXPRESS }

    private String destinatario;
    private String direccion;
    private Metodo metodo;

    public Envio(String destinatario, String direccion, Metodo metodo) {
        this.destinatario = destinatario;
        this.direccion = direccion;
        this.metodo = metodo;
    }

    public String getDestinatario() { return destinatario; }
    public String getDireccion() { return direccion; }
    public Metodo getMetodo() { return metodo; }

    public double costo(double totalProductos) {
        switch (metodo) {
            case EXPRESS: return 9.90;
            default: return totalProductos >= 60 ? 0.0 : 4.99;
        }
    }

    @Override
    public String toString() {
        return "Destinatario: " + destinatario + " | Dirección: " + direccion + " | Método: " + metodo;
    }
}