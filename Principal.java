package battlespace;

import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        System.out.println("Iniciando Battle Space...");
        System.out.println("Controles: A/D/W/S para moverse, E para disparar, Q para salir");
        System.out.println("¡Presiona ENTER para comenzar!");

        try {
            System.in.read();
        } catch (Exception e) {
        }

        Juego juego = new Juego(5);
        juego.iniciarJuego();
        Scanner scanner = new Scanner(System.in);

        while (!juego.isJuegoTerminado()) {
            System.out.print("Ingresa una tecla: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) continue;
            char tecla = Character.toLowerCase(input.charAt(0));
            if (tecla == 'q') break;
            juego.ejecutarTurno(tecla);
        }

        scanner.close();
    }
}
