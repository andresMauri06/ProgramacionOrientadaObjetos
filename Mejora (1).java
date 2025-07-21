package battlespace;

public class Mejora {
    private String tipo;
    private int valor;
    private int fila;
    private int columna;

    public Mejora(String tipo, int valor, int fila, int columna) {
        this.tipo = tipo;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public int getValor() {
        return valor;
    }

    public String descripcion() {
        String tipoBonito = tipo.equals("vida") ? "Salud" : "Ataque";
        return tipoBonito + " +" + valor;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
