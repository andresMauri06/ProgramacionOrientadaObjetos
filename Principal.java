package battlespace;

import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Iniciando Battle Space...");
        System.out.print("Ingresa el nombre de tu jugador: ");
        String nombreJugador = scanner.nextLine().trim();

        System.out.println("Controles: A/D/W/S para moverse, E para disparar, Q para salir");
        System.out.println("¡Presiona ENTER para comenzar!");
        scanner.nextLine(); 

        Juego juego = new Juego(nombreJugador, 5);
        juego.iniciarJuego();

        while (!juego.isJuegoTerminado()) {
            System.out.print("Ingresa una tecla: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) continue;

            char tecla = Character.toLowerCase(input.charAt(0));
            if (tecla == 'q') {
                System.out.println("Saliendo del juego...");
                break;
            }

            juego.ejecutarTurno(tecla);
        }
    }
}
