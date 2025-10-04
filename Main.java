package Tienda;
import java.util.*;

// ===================== App =====================

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Inventario inventario = new Inventario();

    public static void main(String[] args) {
        // Carga de ejemplo
        inventario.agregarRopa("Camisa",  "M", 19.99, 10);
        inventario.agregarRopa("Pantalon","L", 29.99, 6);
        inventario.agregarRopa("Vestido", "S", 39.50, 4);

        int op;
        do {
            menu();
            op = leerInt("Opción: ");
            switch (op) {
                case 1: listarInventario(); break;
                case 2: agregarProducto(); break;
                case 3: reponerStock(); break;
                case 4: cambiarPrecio(); break;
                case 5: crearPedidoYEnviar(); break;
                case 0: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción inválida.");
            }
        } while (op != 0);
        sc.close();
    }

    private static void menu() {
        System.out.println("\n=== TIENDA DE ROPA ===");
        System.out.println("1. Listar inventario");
        System.out.println("2. Agregar prenda");
        System.out.println("3. Reponer stock");
        System.out.println("4. Cambiar precio");
        System.out.println("5. Crear pedido para cliente y gestionar envío");
        System.out.println("0. Salir");
    }

    private static void listarInventario() {
        System.out.println("\n-- INVENTARIO --");
        for (ItemInventario it : inventario.listar()) {
            System.out.println(it.getRopa().toString() + " | Stock: " + it.getCantidad());
        }
    }

    private static void agregarProducto() {
        System.out.println("\n-- Agregar prenda --");
        String tipo = leerStr("Tipo (Camisa, Pantalon, etc): ");
        String talla = leerStr("Talla (S/M/L/XL): ");
        double precio = leerDouble("Precio: ");
        int cant = leerInt("Cantidad inicial: ");
        Ropa r = inventario.agregarRopa(tipo, talla, precio, cant);
        System.out.println("Agregado: " + r.toString() + " | Stock: " + cant);
    }

    private static void reponerStock() {
        System.out.println("\n-- Reponer stock --");
        int id = leerInt("ID de prenda: ");
        if (!inventario.existe(id)) { System.out.println("No existe ese ID."); return; }
        int cant = leerInt("Cantidad a sumar: ");
        inventario.reponer(id, cant);
        System.out.println("Stock actualizado.");
    }

    private static void cambiarPrecio() {
        System.out.println("\n-- Cambiar precio --");
        int id = leerInt("ID de prenda: ");
        if (!inventario.existe(id)) { System.out.println("No existe ese ID."); return; }
        double p = leerDouble("Nuevo precio: ");
        inventario.getItem(id).getRopa().setPrecio(p);
        System.out.println("Precio actualizado.");
    }

    private static void crearPedidoYEnviar() {
        System.out.println("\n-- Crear pedido --");
        Pedido pedido = new Pedido();

        // 1) Datos del cliente
        System.out.println("-- Datos del cliente --");
        String cNombre   = leerStr("Nombre: ");
        String cDni      = leerStr("DNI: ");
        String cTelefono = leerStr("Teléfono: ");
        String cEmail    = leerStr("Email: ");
        Cliente cliente = new Cliente(cNombre, cDni, cTelefono, cEmail);
        pedido.setCliente(cliente);

        // 2) Selección de productos (forzamos al menos 1 línea)
        boolean alMenosUno = false;
        while (true) {
            listarInventario();

            // Leer ID válido
            int id;
            while (true) {
                id = leerInt("ID a agregar: ");
                if (!inventario.existe(id)) {
                    System.out.println("No existe ese ID. Intenta con uno de la lista (1, 2, 3, ...).");
                } else {
                    break;
                }
            }

            // Leer cantidad válida (con stock suficiente)
            int cant;
            while (true) {
                cant = leerInt("Cantidad: ");
                if (cant <= 0) {
                    System.out.println("La cantidad debe ser mayor que 0.");
                    continue;
                }
                if (!inventario.descontar(id, cant)) {
                    System.out.println("Stock insuficiente. Prueba con otra cantidad.");
                } else {
                    break;
                }
            }

            Ropa r = inventario.getItem(id).getRopa();
            pedido.agregarLinea(new LineaPedido(r, cant));
            alMenosUno = true;
            System.out.println("Agregado: " + r.getTipo() + " x" + cant);

            // ¿seguir agregando?
            if (alMenosUno) {
                String seguir = leerStr("¿Agregar otra prenda? (s/n): ");
                if (!seguir.equalsIgnoreCase("s")) break;
            }
        }

        // 3) Datos de envío
        System.out.println("\n-- Datos de envío --");
        String nombre = leerStr("Destinatario: ");
        String direccion = leerStr("Dirección completa: ");
        System.out.println("Método de envío: 1) ESTANDAR  2) EXPRESS");
        int m = leerInt("Elige 1 o 2: ");
        Envio.Metodo metodo = (m == 2) ? Envio.Metodo.EXPRESS : Envio.Metodo.ESTANDAR;
        pedido.setEnvio(new Envio(nombre, direccion, metodo));

        // 4) Selección del método de pago
        System.out.println("Método de pago: 1) Tarjeta de crédito  2) PayPal");
        int metodoPagoSeleccionado = leerInt("Elige 1 o 2: ");
        if (metodoPagoSeleccionado == 1) {
            String tarjetaNumero = leerStr("Número de tarjeta: ");
            String tarjetaNombre = leerStr("Nombre en la tarjeta: ");
            pedido.setMetodoPago(new PagoConTarjeta(tarjetaNumero, tarjetaNombre));
        } else if (metodoPagoSeleccionado == 2) {
            String emailPaypal = leerStr("Email de PayPal: ");
            pedido.setMetodoPago(new PagoConPaypal(emailPaypal));
        }

        // 5) Recibo
        System.out.println("\n" + pedido.recibo());
    }

    // ===================== Utilidades de lectura =====================

    private static String leerStr(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private static int leerInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println("Valor inválido. Intenta de nuevo.");
            }
        }
    }

    private static double leerDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                String s = sc.nextLine().trim();
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.println("Valor inválido. Intenta de nuevo.");
            }
        }
    }
}
