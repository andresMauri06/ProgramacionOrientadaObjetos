package battle_space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class battle_space {
  
  private String nombreDeJuego = "Battle Space";
  private int topeAtaque = 20;
  private int topeSalud = 50;
  private int valorPorEnemigo = 80;
  private List<String> nombresDeEnemigos = Arrays.asList("Goku", "Superman", "Batman");

  public class Colours {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
  }

  public class Images {
    public static final String HEART = Colours.RED+"\u2665"+Colours.BLACK;
    public static final String NAVY = Colours.BLUE+"\u25B2"+Colours.BLACK;
    public static final String FIRE = Colours.CYAN+"|"+Colours.BLACK;
    public static final String FIREBLAST = Colours.PURPLE+"\u25CF"+Colours.BLACK;
  }


  public String dibujarCampo(Jugador jugador, List<Enemigo> enemigos) {
    String contenido = "";
    String mostrarEnemigos = "LISTA DE ENEMIGOS:\n" + enemigos.stream().map((e) -> String.format("%s \n%d. %s \nSalud: %s\nAtaque: %s", Images.NAVY, enemigos.indexOf(e) + 1, e.nombre, String.format("%s%d%s", Colours.GREEN,e.salud,Colours.BLACK), String.format("%s%d%s", Colours.RED,e.ataque,Colours.BLACK))).collect(Collectors.joining("\n---------------\n"));
    String mostrarJugador = String.format("%s\n%s quedan %d enemigos vivos.\nSalud: %s + %s\nAtaque: %s + %s\nPuntaje: %s", Images.HEART, jugador.nombre, enemigos.size(), String.format("%s%d%s", Colours.GREEN,jugador.salud,Colours.BLACK), String.format("%s%d%s", Colours.GREEN,jugador.saludAdicional,Colours.BLACK), String.format("%s%d%s", Colours.RED,jugador.ataque,Colours.BLACK), String.format("%s%d%s", Colours.RED,jugador.ataqueAdicional,Colours.BLACK), String.format("%s%d%s", Colours.BLUE,jugador.puntaje,Colours.BLACK));
    contenido = mostrarEnemigos + "\n============ VS ============\n" + mostrarJugador;
    return contenido;
  }


  List<NivelDeDificultad> nivelesDeDificultad = Arrays.asList(
        new NivelDeDificultad("Facil", 1000, 30),
        new NivelDeDificultad("Medio", 80, 20),
        new NivelDeDificultad("Dificil", 60, 5) 
      );

  public class NivelDeDificultad {
    private String nombre;
    private int salud;
    private int ataque;

    public NivelDeDificultad(String nombre, int salud, int ataque) {
      this.nombre = nombre;
      this.salud = salud;
      this.ataque = ataque;
    }

    public String mostrarNiveles() {
      return nivelesDeDificultad.stream().map((n) -> String.format("%d. %s -> Salud: %d - Ataque: %d", nivelesDeDificultad.indexOf(n) + 1, n.nombre, n.salud, n.ataque)).collect(Collectors.joining(" | "));
    }
    
  }

  public class Enemigo {
    private int codigo;
    private String nombre; // campo tipo?
    private int salud;
    private int ataque;

    public Enemigo() {}

    public Enemigo(int codigo, String nombre, int salud, int ataque) {

      this.nombre = nombre;
      this.salud = salud;
      this.ataque = ataque;
    }

    public void atacar(Jugador jugador) {
      jugador.salud -= this.ataque;
    }

    public void recibirDaño(int cantidad) {
      this.salud -= cantidad;
    }

    public boolean estaVivo() {
      return this.salud > 0;
    }

    public String mostrarEstado() {
      return String.format("Enemigo: %s | Salud: %d | Ataque: %d", this.nombre, this.salud, this.ataque);
    }
    
  }

  public class Mejora {
    private String tipo;
    private int valor;

    

    public Mejora(String tipo, int valor) {
      this.tipo = tipo;
      this.valor = valor;
    }

    private String getTipo() {
      return this.tipo;
    }

    private String descripcion() {
      return String.format("Mejora el estado de %s", this.tipo);
    }

  }

  public class Jugador {
    private String nombre;
    private int salud;
    private int ataque;
    private int puntaje;
    private int ataqueAdicional;
    private int saludAdicional;

    public Jugador(String nombre, int salud, int ataque, int ataqueAdicional, int saludAdicional) {
      this.nombre = nombre;
      this.salud = salud;
      this.ataque = ataque;
      this.ataqueAdicional = ataqueAdicional;
      this.saludAdicional = saludAdicional;
      this.puntaje = 0;
    }
    
    public void atacar(Enemigo enemigo) {
      enemigo.salud -= this.ataque + this.ataqueAdicional;
    }

    public void recibirDaño(int cantidad) {
      this.salud = (this.salud + this.saludAdicional) - cantidad;
    }

    public boolean estaVivo() {
      return (this.salud + this.saludAdicional) > 0;
    }

    public void usarMejora(Mejora mejora) {
      String prefix = "";
      switch (mejora.tipo) {
        case "salud":
          prefix = Colours.GREEN;
          this.saludAdicional += mejora.valor;
          break;
        default:
          // case "ataque":
          prefix = Colours.RED;
          this.ataqueAdicional += mejora.valor;
          break;
      }
      System.out.println(String.format("\n%sAgregando +%d a %s adicional %s\n", prefix, mejora.valor, mejora.tipo, Colours.BLACK));
    }

  }

  public class Juego {
    private Jugador jugador;
    private List<Enemigo> enemigos;
    private boolean juegoTerminado;
    private Scanner scanner;
    private int turno;
    

    // public Juego(Jugador jugador, List<Enemigo> enemigos) {
    //   this.jugador = jugador;
    //   this.enemigos = enemigos;
    // }

    public Juego() {}

    public void iniciarJuego() {
      this.scanner = new Scanner(System.in);
      System.out.println(String.format("Bienvenido.\nIngresa el nombre del Jugador: "));
      String nombre = scanner.nextLine();
      System.out.println(String.format("Contra cuantos enemigos deseas pelear: "));
      int cantidad = Integer.parseInt(scanner.nextLine());
      NivelDeDificultad niveles = new NivelDeDificultad("",0,0);
      System.out.println(String.format("Que nivel de dificultad deseas: %s", niveles.mostrarNiveles()));
      int dificultad = Integer.parseInt(scanner.nextLine());
      NivelDeDificultad nivelDeDificultad = nivelesDeDificultad.get(dificultad - 1);

      // Inicializando jugador
      Jugador nuevoJueJugador = new Jugador(nombre, nivelDeDificultad.salud, nivelDeDificultad.ataque, 0,0);
      this.jugador = nuevoJueJugador;
      this.enemigos = this.generarEnemigos(cantidad);
      this.ejecutarTurno();
    }

    public void ejecutarTurno() {
      // Iteraciones
      while (this.jugador.estaVivo() && this.enemigos.size() > 0) {
        Combate nuevoCombate = new Combate(this.jugador, this.enemigos);
        nuevoCombate.jugarJudador();
        this.enemigos = nuevoCombate.eliminarEnemigosDerrotados();
        nuevoCombate.jugarEnemigos();
      }
      this.juegoTerminado = true;
      int opcion = 0;
      String prefix = "Mataste a todos los enemigos";
      if (!this.jugador.estaVivo()) {
        prefix = "Te moriste";
      }
      System.out.println(String.format("%s\n¿Deseas jugar de nuevo?: 1. SI - 2. NO", prefix));
      opcion = Integer.parseInt(scanner.nextLine());
      if (opcion == 1) {
        this.iniciarJuego();
      }
      // MUERE LA APLICACIÓN
    }

    public boolean verificarFinDelJuego() {
      return this.jugador.estaVivo();
    }

    public String mostrarEstado() {
      return String.format("Jugador: %s", this.jugador.nombre);
    }

    public List<Enemigo> generarEnemigos(int cantidad) {
      List<Enemigo> enemigos = new ArrayList<>();
      try {
        for (int i = 0; i < cantidad; i++) {
          int posicionNombreEnemigo = (int)(Math.random() * (nombresDeEnemigos.size() - 1)) + 1;
          int salud = (int)(Math.random() * topeSalud ) + 1;
          int ataque = (int)(Math.random() * topeAtaque) + 1;
          enemigos.add(new Enemigo(i+1, nombresDeEnemigos.get(posicionNombreEnemigo) + " " + (i+1) , salud, ataque));
          System.out.println(enemigos.size());
        }
      } catch (Exception e) {
        // TODO: handle exception
      }
      return enemigos;
    }
    
  }

  public class Combate {
    private Jugador jugador;
    private List<Enemigo> enemigos;
    private Scanner scanner;

    public Combate(Jugador jugador, List<Enemigo> enemigos) {
      this.scanner = new Scanner(System.in);
      this.jugador = jugador;
      this.enemigos = enemigos;
    }

    public void jugarJudador() {
      System.out.println(dibujarCampo(jugador, enemigos));
      System.out.println("¿Elige el enemigo a atacar? ");
      int enemigoAAtacar = Integer.parseInt(scanner.nextLine());
      this.jugador.atacar(this.enemigos.get(enemigoAAtacar - 1));
      this.verificarMejora(this.enemigos.get(enemigoAAtacar - 1));
    }
    
    public void jugarEnemigos() {
      enemigos.forEach((e) -> {
        e.atacar(this.jugador);
      });
    }

    public List<Enemigo> eliminarEnemigosDerrotados() {

      List<Enemigo> enemigos = this.enemigos.stream().filter((e) -> e.salud > 0).toList();
      return enemigos;
    }

    public void verificarMejora(Enemigo enemigo) {
      if (enemigo.salud <= 0) {
        this.jugador.puntaje += valorPorEnemigo;
        boolean existeMejora = ((int)(Math.random() * 10) + 1) <= 3;
        if (existeMejora) {
          boolean esMejoraDeSalud = ((int)(Math.random() * 10) + 1) <= 5;
          int valor = (int)(Math.random() * topeAtaque) + 1;
          Mejora nuevaMejora = new Mejora(esMejoraDeSalud ? "salud" : "ataque", valor);
          this.jugador.usarMejora(nuevaMejora);
        }
      }
    }

  }

  public void main(String[] args) {
    battle_space a = new battle_space();
    
    System.out.println(String.format("********************************************************"));
    System.out.println(String.format("\t\t\t%s\t\t\t", a.nombreDeJuego));
    System.out.println(String.format("***********************************************************"));
    System.out.println(String.format("***********************************************************"));
    Juego nuevoJuevo = new Juego();
    nuevoJuevo.iniciarJuego();
    
    
  }


  

}