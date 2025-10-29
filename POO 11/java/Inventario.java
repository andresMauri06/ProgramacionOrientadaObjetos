
class Inventario {
    private List<ItemInventario> items = new ArrayList<>();
    private int nextId = 1;

    public Ropa agregarRopa(String tipo, String talla, double precio, int cantidad) {
        Ropa r = new Ropa(nextId++, tipo, talla, precio);
        items.add(new ItemInventario(r, cantidad));
        return r;
    }

    public boolean existe(int id) {
        return items.stream().anyMatch(item -> item.getRopa().getId() == id);
    }

    public ItemInventario getItem(int id) {
        return items.stream().filter(item -> item.getRopa().getId() == id).findFirst().orElse(null);
    }

    public List<ItemInventario> listar() {
        return items;
    }

    public void reponer(int id, int cant) {
        ItemInventario it = getItem(id);
        if (it != null) {
            it.setCantidad(it.getCantidad() + cant);
        }
    }

    public boolean descontar(int id, int cant) {
        ItemInventario it = getItem(id);
        if (it == null || it.getCantidad() < cant) return false;
        it.setCantidad(it.getCantidad() - cant);
        return true;
    }
}
