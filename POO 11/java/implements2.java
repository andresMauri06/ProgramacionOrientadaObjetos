class PagoConPaypal implements MetodoPago {
    private String email;

    public PagoConPaypal(String email) {
        this.email = email;
    }

    @Override
    public boolean realizarPago(double monto) {
        //  pago con PayPal
        System.out.println("Procesando pago con PayPal: " + email);
        System.out.println("Pago de $" + monto + " realizado con éxito.");
        return true; //pago exitoso
    }
}