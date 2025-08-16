package battlespace;

import battlespace.Enemigo;
import battlespace.Mejora;

public class Jugador {
    private String nombre;
    private int salud;
    private int ataque;
    private int puntaje;
    private int fila;
    private int columna;
    private char simbolo;

    public Jugador(String nombre, int fila, int columna) {
        this.nombre = nombre;
        this.salud = 100;
        this.ataque = 10;
        this.puntaje = 0;
        this.fila = fila;
        this.columna = columna;
        this.simbolo = 'A';
    }

    public void atacar(Enemigo e) {
        if (e != null && e.estaVivo()) {
            e.recibirDaño(this.ataque);
            if (!e.estaVivo()) {
                this.puntaje += 10;
            }
        }
    }

    public void recibirDaño(int cantidad) {
        this.salud -= cantidad;
        if (this.salud < 0) this.salud = 0;
    }

    public boolean estaVivo() {
        return this.salud > 0;
    }

    public void usarMejora(Mejora m) {
        if (m.getTipo().equals("vida")) {
            this.salud += m.getValor();
            if (this.salud > 100) this.salud = 100;
            System.out.println("¡" + nombre + " recibe una mejora de VIDA! +" + m.getValor() + " → Salud actual: " + this.salud);
        } else if (m.getTipo().equals("ataque")) {
            this.ataque += m.getValor();
            System.out.println("¡" + nombre + " recibe una mejora de ATAQUE! +" + m.getValor() + " → Ataque actual: " + this.ataque);
        }
    }

    public void mover(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public void incrementarPuntaje(int puntos) {
        this.puntaje += puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSalud() {
        return salud;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public char getSimbolo() {
        return simbolo;
    }
}
