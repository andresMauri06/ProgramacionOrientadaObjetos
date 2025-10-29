
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
                case 5: filtrarProductos(); break;
                case 6: ordenarProductosPorPrecio(); break;
                case 7: crearPedidoYEnviar(); break;
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
        System.out.println("5. Filtrar productos por tipo/talla");
        System.out.println("6. Ordenar productos por precio");
        System.out.println("7. Crear pedido para cliente y gestionar envío");
        System.out.println("0. Salir");
    }

    private static void listarInventario() {
        System.out.println("\n-- INVENTARIO --");
        for (ItemInventario it : inventario.listar()) {
            System.out.println(it.getRopa().toString() + " | Stock: " + it.getCantidad());
        }
    }

    private static void filtrarProductos() {
        System.out.println("\n-- Filtrar productos --");
        String tipo = leerStr("Introduce el tipo de prenda (Camisa, Pantalón, etc) o 'todos' para mostrar todo: ");
        String talla = leerStr("Introduce la talla (S/M/L) o 'todas' para mostrar todas: ");
        
        List<ItemInventario> listaItems = inventario.listar();
        boolean encontrado = false;

        for (ItemInventario it : listaItems) {
            Ropa ropa = it.getRopa();
            boolean tipoCoincide = tipo.equalsIgnoreCase("todos") || ropa.getTipo().equalsIgnoreCase(tipo);
            boolean tallaCoincide = talla.equalsIgnoreCase("todas") || ropa.getTalla().equalsIgnoreCase(talla);
            
            if (tipoCoincide && tallaCoincide) {
                System.out.println(ropa.toString() + " | Stock: " + it.getCantidad());
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron productos que coincidan con los filtros.");
        }
    }

    private static void ordenarProductosPorPrecio() {
        System.out.println("\n-- Ordenar productos por precio --");
        List<ItemInventario> listaItems = inventario.listar();

        // Ordenar de menor a mayor 
        listaItems.sort(Comparator.comparingDouble(item -> item.getRopa().getPrecio()));

        // Mostrar productos 
        for (ItemInventario it : listaItems) {
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

        //  Datos del cliente
        System.out.println("-- Datos del cliente --");
        String cNombre   = leerStr("Nombre: ");
        String cDni      = leerStr("DNI: ");
        String cTelefono = leerStr("Teléfono: ");
        String cEmail    = leerStr("Email: ");
        Cliente cliente = new Cliente(cNombre, cDni, cTelefono, cEmail);
        pedido.setCliente(cliente);

        //  Selección de productos 
        boolean alMenosUno = false;
        while (true) {
            listarInventario();

            // Leer ID 
            int id;
            while (true) {
                id = leerInt("ID a agregar: ");
                if (!inventario.existe(id)) {
                    System.out.println("No existe ese ID. Intenta con uno de la lista (1, 2, 3, ...).");
                } else {
                    break;
                }
            }

            // Leer cantidad válida con stock 
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

        //  Datos 
        System.out.println("\n-- Datos de envío --");
        String nombre = leerStr("Destinatario: ");
        String direccion = leerStr("Dirección completa: ");
        System.out.println("Método de envío: 1) ESTANDAR  2) EXPRESS");
        int m = leerInt("Elige 1 o 2: ");
        Envio.Metodo metodo = (m == 2) ? Envio.Metodo.EXPRESS : Envio.Metodo.ESTANDAR;
        pedido.setEnvio(new Envio(nombre, direccion, metodo));

        //  método de pago
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
