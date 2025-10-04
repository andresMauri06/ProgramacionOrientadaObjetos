class PagoConTarjeta implements MetodoPago {
    private String tarjetaNumero;
    private String tarjetaNombre;

    public PagoConTarjeta(String tarjetaNumero, String tarjetaNombre) {
        this.tarjetaNumero = tarjetaNumero;
        this.tarjetaNombre = tarjetaNombre;
    }

    @Override
    public boolean realizarPago(double monto) {
        // Lógica para procesar pago con tarjeta
        System.out.println("Procesando pago con tarjeta de crédito: " + tarjetaNumero);
        System.out.println("Pago de $" + monto + " realizado con éxito.");
        return true; // Simulamos un pago exitoso
    }
}