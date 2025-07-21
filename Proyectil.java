package battlespace;

public class Proyectil {
    private int fila;
    private int columna;
    private int daño;
    private int direccion;
    private boolean activo;

    public Proyectil(int fila, int columna, int daño, int direccion) {
        this.fila = fila;
        this.columna = columna;
        this.daño = daño;
        this.direccion = direccion;
        this.activo = true;
    }

    public void mover() {
        this.fila += direccion;
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean estaActivo() {
        return this.activo;
    }

    public int getFila() {
        return this.fila;
    }

    public int getColumna() {
        return this.columna;
    }

    public int getDaño() {
        return this.daño;
    }
}
