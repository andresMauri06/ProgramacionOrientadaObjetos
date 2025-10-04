class PagoConPaypal implements MetodoPago {
    private String email;

    public PagoConPaypal(String email) {
        this.email = email;
    }

    @Override
    public boolean realizarPago(double monto) {
        // Lógica para procesar pago con PayPal
        System.out.println("Procesando pago con PayPal: " + email);
        System.out.println("Pago de $" + monto + " realizado con éxito.");
        return true; // Simulamos un pago exitoso
    }
}