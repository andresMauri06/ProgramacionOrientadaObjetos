package battlespace;

import java.util.Arrays;

public class Combate {
    private final int filas = 10, columnas = 15;
    private char[][] campo = new char[filas][columnas];
    private Jugador jugador;
    private Enemigo[] enemigos;
    private Proyectil[] proyectilesJugador = new Proyectil[20];
    private Proyectil[] proyectilesEnemigos = new Proyectil[30];
    private Mejora[] mejoras = new Mejora[10];

    public Combate(Jugador jugador, Enemigo[] enemigos) {
        this.jugador = jugador;
        this.enemigos = enemigos;
    }

    public void jugarJugador(char tecla) {
        switch (tecla) {
            case 'a':
                if (jugador.getColumna() > 0)
                    jugador.mover(jugador.getFila(), jugador.getColumna() - 1);
                break;
            case 'd':
                if (jugador.getColumna() < columnas - 1)
                    jugador.mover(jugador.getFila(), jugador.getColumna() + 1);
                break;
            case 'w':
                if (jugador.getFila() > 0)
                    jugador.mover(jugador.getFila() - 1, jugador.getColumna());
                break;
            case 's':
                if (jugador.getFila() < filas - 1)
                    jugador.mover(jugador.getFila() + 1, jugador.getColumna());
                break;
            case 'e':
                dispararJugador();
                break;
        }
        recogerMejora();
    }

    public void jugarEnemigos() {
        for (Enemigo e : enemigos) {
            if (e != null && e.estaVivo()) {
                e.moverAleatorio(filas, columnas);
                if (Math.random() < 0.5) { 
                    for (int i = 0; i < proyectilesEnemigos.length; i++) {
                        if (proyectilesEnemigos[i] == null || !proyectilesEnemigos[i].estaActivo()) {
                            proyectilesEnemigos[i] = new Proyectil(e.getFila() + 1, e.getColumna(), e.getAtaque(), 1);
                            break;
                        }
                    }
                }
            }
        }
        moverProyectiles();
    }

    public void eliminarEnemigosDerrotados() {
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] != null && !enemigos[i].estaVivo()) {
                verificarMejora(enemigos[i]);
                enemigos[i] = null;
            }
        }
    }

    private void dispararJugador() {
        for (int i = 0; i < proyectilesJugador.length; i++) {
            if (proyectilesJugador[i] == null || !proyectilesJugador[i].estaActivo()) {
                proyectilesJugador[i] = new Proyectil(jugador.getFila() - 1, jugador.getColumna(), jugador.getAtaque(), -1);
                break;
            }
        }
    }

    private void moverProyectiles() {
        for (Proyectil p : proyectilesJugador) {
            if (p != null && p.estaActivo()) {
                p.mover();
                if (p.getFila() < 0 || p.getFila() >= filas || p.getColumna() < 0 || p.getColumna() >= columnas) {
                    p.desactivar();
                    continue;
                }
                for (Enemigo e : enemigos) {
                    if (e != null && e.estaVivo() && p.getFila() == e.getFila() && p.getColumna() == e.getColumna()) {
                        e.recibirDaño(p.getDaño());
                        jugador.incrementarPuntaje(10); 
                        p.desactivar();
                        break;
                    }
                }
                
                for (Proyectil pe : proyectilesEnemigos) {
                    if (pe != null && pe.estaActivo() && p.getFila() == pe.getFila() && p.getColumna() == pe.getColumna()) {
                        p.desactivar();
                        pe.desactivar();
                    }
                }
            }
        }

        
        for (Proyectil p : proyectilesEnemigos) {
            if (p != null && p.estaActivo()) {
                p.mover();
                if (p.getFila() < 0 || p.getFila() >= filas || p.getColumna() < 0 || p.getColumna() >= columnas) {
                    p.desactivar();
                    continue;
                }
                if (p.getFila() == jugador.getFila() && p.getColumna() == jugador.getColumna()) {
                    jugador.recibirDaño(p.getDaño());
                    p.desactivar();
                }
            }
        }

        for (Enemigo e : enemigos) {
            if (e != null && e.estaVivo()) {
                if (jugador.getFila() == e.getFila() && jugador.getColumna() == e.getColumna()) {
                    jugador.recibirDaño(jugador.getSalud()); 
                    System.out.println("¡Has chocado contra un enemigo! Has muerto.");
                }
            }
        }
    }

    private void recogerMejora() {
        for (int i = 0; i < mejoras.length; i++) {
            if (mejoras[i] != null &&
                mejoras[i].getFila() == jugador.getFila() &&
                mejoras[i].getColumna() == jugador.getColumna()) {

                System.out.println("¡Has recogido una mejora! " + mejoras[i].descripcion());
                jugador.usarMejora(mejoras[i]);
                mejoras[i] = null;
                break;
            }
        }
    }

    private void verificarMejora(Enemigo e) {
        Mejora mejora = e.soltarMejora();
        if (mejora != null && mejora.getFila() < filas && mejora.getColumna() < columnas) {
            for (int i = 0; i < mejoras.length; i++) {
                if (mejoras[i] == null) {
                    mejoras[i] = mejora;
                    break;
                }
            }
        }
    }

    public void mostrarCampo() {
        for (int i = 0; i < filas; i++) {
            Arrays.fill(campo[i], ' ');
        }
        campo[jugador.getFila()][jugador.getColumna()] = jugador.getSimbolo();
        for (Enemigo e : enemigos) {
            if (e != null && e.estaVivo()) {
                campo[e.getFila()][e.getColumna()] = e.getSimbolo();
            }
        }
        for (Proyectil p : proyectilesJugador) {
            if (p != null && p.estaActivo()) {
                campo[p.getFila()][p.getColumna()] = '*';
            }
        }
        for (Proyectil p : proyectilesEnemigos) {
            if (p != null && p.estaActivo()) {
                campo[p.getFila()][p.getColumna()] = '.';
            }
        }
        for (Mejora m : mejoras) {
            if (m != null) {
                campo[m.getFila()][m.getColumna()] = '+'; 
            }
        }
        for (char[] fila : campo) {
            System.out.println(new String(fila));
        }
    }
}
