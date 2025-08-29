import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;


public class TiendaRopa {

    enum Talla { XS, S, M, L, XL, XXL;

        static Talla fromString(String s) {
            return Talla.valueOf(s.trim().toUpperCase(Locale.ROOT));
        }
    }

    static class Producto {
        final String nombre;   // e.g., "Polera Básica"
        final Talla talla;     // e.g., M
        BigDecimal precio;     // precio unitario
        int cantidad;          // unidades en stock

        Producto(String nombre, Talla talla, BigDecimal precio, int cantidad) {
            this.nombre = nombre;
            this.talla = talla;
            this.precio = precio.setScale(2, RoundingMode.HALF_UP);
            this.cantidad = cantidad;
        }

        String key() { // clave única por nombre+talla
            return (nombre + "||" + talla.name()).toLowerCase(Locale.ROOT);
        }

        @Override public String toString() {
            return nombre + " | Talla " + talla + " | Precio " + precioFormateado(precio) + " | Stock " + cantidad;
        }
    }

    // ====== ESTADO DE LA TIENDA ======
    private final Map<String, Producto> inventario = new LinkedHashMap<>();
    private final Scanner sc = new Scanner(System.in).useLocale(Locale.US);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "PE")); // ajusta a tu moneda

    // ====== ARRANQUE CON DATOS DE EJEMPLO ======
    public TiendaRopa() {
        altaProducto("Polera Básica", Talla.S, bd("39.90"), 10);
        altaProducto("Polera Básica", Talla.M, bd("39.90"), 12);
        altaProducto("Polera Básica", Talla.L, bd("39.90"), 8);
        altaProducto("Jeans Slim", Talla.M, bd("129.50"), 5);
        altaProducto("Chaqueta Denim", Talla.L, bd("199.00"), 4);
    }

    // ====== UTILIDADES ======
    static BigDecimal bd(String s) { return new BigDecimal(s).setScale(2, RoundingMode.HALF_UP); }
    String precioFormateado(BigDecimal x) { return nf.format(x); }

    Optional<Producto> buscar(String nombre, Talla talla) {
        return Optional.ofNullable(inventario.get((nombre + "||" + talla.name()).toLowerCase(Locale.ROOT)));
    }

    void altaProducto(String nombre, Talla talla, BigDecimal precio, int cantidad) {
        Producto p = new Producto(nombre, talla, precio, cantidad);
        inventario.put(p.key(), p);
    }

    // ====== UI ======
    void menu() {
        System.out.println("=== Tienda de Ropa (Java 1.8) ===");
        boolean seguir = true;
        while (seguir) {
            System.out.println("\nElige una opción:");
            System.out.println("  1) Listar inventario");
            System.out.println("  2) Vender (disminuir stock)");
            System.out.println("  3) Reponer (aumentar stock)");
            System.out.println("  4) Agregar nuevo producto");
            System.out.println("  5) Cambiar precio");
            System.out.println("  6) Ver valor total del inventario");
            System.out.println("  7) Salir");
            System.out.print("Opción: ");
            int op = leerEntero(1, 7);

            try {
                switch (op) {
                    case 1:
                        listar();
                        break;
                    case 2:
                        vender();
                        break;
                    case 3:
                        reponer();
                        break;
                    case 4:
                        agregar();
                        break;
                    case 5:
                        cambiarPrecio();
                        break;
                    case 6:
                        valorInventario();
                        break;
                    case 7:
                        System.out.println("¡Hasta luego!");
                        seguir = false;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ====== ACCIONES ======
    void listar() {
        if (inventario.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }
        System.out.println("\n--- Inventario ---");
        for (Producto p : inventario.values()) {
            System.out.println(" • " + p);
        }
    }

    void vender() {
        System.out.println("\n--- Registrar Venta ---");
        String nombre = leerTexto("Nombre del producto: ");
        Talla talla = leerTalla("Talla (XS,S,M,L,XL,XXL): ");
        int unidades = leerEnteroPositivo("Cantidad a vender: ");

        Producto p = buscar(nombre, talla).orElseThrow(new IllegalArgumentException("No existe ese producto/talla.")::fillInStackTrace);
        if (p.cantidad < unidades) throw new IllegalArgumentException("Stock insuficiente. Disponible: " + p.cantidad);

        p.cantidad -= unidades;
        BigDecimal total = p.precio.multiply(new BigDecimal(unidades)).setScale(2, RoundingMode.HALF_UP);
        System.out.println("Venta OK: " + unidades + " x " + p.nombre + " (Talla " + p.talla + ") a " + precioFormateado(p.precio) + " = " + precioFormateado(total));
        System.out.println("Stock restante: " + p.cantidad);
    }

    void reponer() {
        System.out.println("\n--- Reposición ---");
        String nombre = leerTexto("Nombre del producto: ");
        Talla talla = leerTalla("Talla (XS,S,M,L,XL,XXL): ");
        int unidades = leerEnteroPositivo("Cantidad a ingresar: ");

        Producto p = buscar(nombre, talla).orElseThrow(new IllegalArgumentException("No existe ese producto/talla.")::fillInStackTrace);
        p.cantidad += unidades;
        System.out.println("Reposición OK. Stock actual: " + p.cantidad);
    }

    void agregar() {
        System.out.println("\n--- Agregar Producto ---");
        String nombre = leerTexto("Nombre: ");
        Talla talla = leerTalla("Talla (XS,S,M,L,XL,XXL): ");
        if (buscar(nombre, talla).isPresent()) {
            System.out.println("Ya existe ese producto/talla. Usa 'Cambiar precio' o 'Reponer'.");
            return;
        }
        BigDecimal precio = leerPrecio("Precio unitario: ");
        int cantidad = leerEnteroPositivo("Cantidad inicial: ");
        altaProducto(nombre, talla, precio, cantidad);
        System.out.println("Producto agregado: " + nombre + " " + talla + " @ " + precioFormateado(precio) + " (stock " + cantidad + ")");
    }

    void cambiarPrecio() {
        System.out.println("\n--- Cambiar Precio ---");
        String nombre = leerTexto("Nombre del producto: ");
        Talla talla = leerTalla("Talla (XS,S,M,L,XL,XXL): ");
        Producto p = buscar(nombre, talla).orElseThrow(new IllegalArgumentException("No existe ese producto/talla.")::fillInStackTrace);
        BigDecimal nuevo = leerPrecio("Nuevo precio: ");
        p.precio = nuevo.setScale(2, RoundingMode.HALF_UP);
        System.out.println("Precio actualizado: " + p);
    }

    void valorInventario() {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto p : inventario.values()) {
            total = total.add(p.precio.multiply(new BigDecimal(p.cantidad)));
        }
        total = total.setScale(2, RoundingMode.HALF_UP);
        System.out.println("\nValor total del inventario: " + precioFormateado(total));
    }

    // ====== LECTURA/VALIDACIÓN ======
    String leerTexto(String msg) {
        System.out.print(msg);
        String s = sc.nextLine().trim();
        while (s.isEmpty()) {
            System.out.print("No puede estar vacío. Intenta de nuevo: ");
            s = sc.nextLine().trim();
        }
        return s;
    }

    int leerEntero(int min, int max) {
        while (true) {
            String raw = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(raw);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.print("Ingresa un número entre " + min + " y " + max + ": ");
            }
        }
    }

    int leerEnteroPositivo(String msg) {
        System.out.print(msg);
        while (true) {
            String raw = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(raw);
                if (v <= 0) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.print("Debe ser un entero positivo. Intenta de nuevo: ");
            }
        }
    }

    BigDecimal leerPrecio(String msg) {
        System.out.print(msg);
        while (true) {
            String raw = sc.nextLine().trim().replace(',', '.');
            try {
                BigDecimal v = new BigDecimal(raw);
                if (v.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
                return v.setScale(2, RoundingMode.HALF_UP);
            } catch (NumberFormatException e) {
                System.out.print("Precio inválido. Usa número > 0 (ej: 39.90). Intenta de nuevo: ");
            }
        }
    }

    Talla leerTalla(String msg) {
        System.out.print(msg);
        while (true) {
            String raw = sc.nextLine();
            try {
                return Talla.fromString(raw);
            } catch (Exception e) {
                System.out.print("Talla inválida. Opciones: XS,S,M,L,XL,XXL. Intenta de nuevo: ");
            }
        }
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        new TiendaRopa().menu();
    }
}
