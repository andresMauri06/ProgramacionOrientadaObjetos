import java.util.*;

class Ropa {
    private int id;
    private String tipo;
    private String talla;
    private double precio;

    public Ropa(int id, String tipo, String talla, double precio) {
        this.id = id;
        this.tipo = tipo;
        this.talla = talla;
        this.precio = precio;
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public String toString() {
        return String.format("ID:%d | %s T:%s | $%.2f", id, tipo, talla, precio);
    }
}

class ItemInventario {
    private Ropa ropa;
    private int cantidad;

    public ItemInventario(Ropa ropa, int cantidad) {
        this.ropa = ropa;
        this.cantidad = cantidad;
    }

    public Ropa getRopa() { return ropa; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}

class Inventario {
    private Map<Integer, ItemInventario> items = new HashMap<Integer, ItemInventario>();
    private int nextId = 1;

    public Ropa agregarRopa(String tipo, String talla, double precio, int cantidad) {
        Ropa r = new Ropa(nextId++, tipo, talla, precio);
        items.put(r.getId(), new ItemInventario(r, cantidad));
        return r;
    }

    public boolean existe(int id) {
        return items.containsKey(id);
    }

    public ItemInventario getItem(int id) {
        return items.get(id);
    }

    public List<ItemInventario> listar() {
        return new ArrayList<ItemInventario>(items.values());
    }

    public void reponer(int id, int cant) {
        ItemInventario it = items.get(id);
        if (it != null) {
            it.setCantidad(it.getCantidad() + cant);
        }
    }

    public boolean descontar(int id, int cant) {
        ItemInventario it = items.get(id);
        if (it == null || it.getCantidad() < cant) return false;
        it.setCantidad(it.getCantidad() - cant);
        return true;
    }
}

class LineaPedido {
    private Ropa ropa;
    private int cantidad;

    public LineaPedido(Ropa ropa, int cantidad) {
        this.ropa = ropa;
        this.cantidad = cantidad;
    }

    public Ropa getRopa() { return ropa; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return ropa.getPrecio() * cantidad; }

    @Override
    public String toString() {
        return String.format("%s x%d -> $%.2f", ropa.toString(), cantidad, getSubtotal());
    }
}

class Envio {
    public enum Metodo { ESTANDAR, EXPRESS }

    private String destinatario;
    private String direccion;
    private Metodo metodo;

    public Envio(String destinatario, String direccion, Metodo metodo) {
        this.destinatario = destinatario;
        this.direccion = direccion;
        this.metodo = metodo;
    }

    public String getDestinatario() { return destinatario; }
    public String getDireccion() { return direccion; }
    public Metodo getMetodo() { return metodo; }

    public double costo(double totalProductos) {
        switch (metodo) {
            case EXPRESS: return 9.90;
            default: return totalProductos >= 60 ? 0.0 : 4.99;
        }
    }

    @Override
    public String toString() {
        return "Destinatario: " + destinatario + " | Dirección: " + direccion + " | Método: " + metodo;
    }
}

class Cliente {
    private String nombre;
    private String dni;
    private String telefono;
    private String email;

    public Cliente(String nombre, String dni, String telefono, String email) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters - devuelven  Setters modifican
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Cliente: " + nombre + " | DNI: " + dni + " | Tel: " + telefono + " | Email: " + email;
    }
}

class Pedido {
    private List<LineaPedido> lineas = new ArrayList<LineaPedido>();
    private Envio envio;
    private Cliente cliente;

    public void agregarLinea(LineaPedido l) { lineas.add(l); }
    public void setEnvio(Envio e) { this.envio = e; }
    public void setCliente(Cliente c) { this.cliente = c; }
    public Cliente getCliente() { return cliente; }

    public double totalProductos() {
        double t = 0;
        for (LineaPedido l : lineas) t += l.getSubtotal();
        return t;
    }

    public double total() {
        double tp = totalProductos();
        return tp + (envio == null ? 0 : envio.costo(tp));
    }

    public String recibo() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECIBO =====\n");
        if (cliente != null) sb.append(cliente.toString()).append("\n");
        for (LineaPedido l : lineas) {
            sb.append(" - ").append(l.toString()).append("\n");
        }
        double tp = totalProductos();
        sb.append(String.format("Subtotal: $%.2f\n", tp));
        if (envio != null) {
            double ce = envio.costo(tp);
            sb.append("Envío: ").append(envio.toString()).append(String.format(" | Costo: $%.2f\n", ce));
            sb.append(String.format("TOTAL A PAGAR: $%.2f\n", tp + ce));
        } else {
            sb.append(String.format("TOTAL A PAGAR: $%.2f\n", tp));
        }
        sb.append("==================\n");
        return sb.toString();
    }
}
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Inventario inventario = new Inventario();

    public static void main(String[] args) {
      
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

    // acceso para private
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

    boolean alMenosUno = false;
    while (true) {
        listarInventario();

        int id;
        while (true) {
            id = leerInt("ID a agregar: ");
            if (!inventario.existe(id)) {
                System.out.println("No existe ese ID. Intenta con uno de la lista (1, 2, 3, ...).");
            } else {
                break;
            }
        }

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

        if (alMenosUno) {
            String seguir = leerStr("¿Agregar otra prenda? (s/n): ");
            if (!seguir.equalsIgnoreCase("s")) break;
        }
    }

    System.out.println("\n-- Datos de envío --");
    String nombre = leerStr("Destinatario: ");
    String direccion = leerStr("Dirección completa: ");
    System.out.println("Método de envío: 1) ESTANDAR  2) EXPRESS");
    int m = leerInt("Elige 1 o 2: ");
    Envio.Metodo metodo = (m == 2) ? Envio.Metodo.EXPRESS : Envio.Metodo.ESTANDAR;
    pedido.setEnvio(new Envio(nombre, direccion, metodo));

    System.out.println("\n" + pedido.recibo());
}


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


