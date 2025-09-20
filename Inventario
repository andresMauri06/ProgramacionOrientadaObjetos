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

