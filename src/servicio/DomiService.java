package servicio;

import modelo.Cliente;
import modelo.Cotizacion;
import modelo.Pedido;
import java.util.ArrayList;
import modelo.Repartidor;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;


public class DomiService {

    private ArrayList<Pedido> pedidos;           // En esta lista es donde se almacenarán todos los pedidos.
    private ArrayList<Repartidor> repartidores;  // En esta lista es donde se almacenarán todos los repartidores.
    private final Map<String, Cliente> clientes = new HashMap<>();
    private final Set<String> barriosCobertura = new HashSet<>();
    private final Map<String, Double> distanciasBarrios = new HashMap<>();

    private void listaBarriosCobertura() {
        barriosCobertura.add("Chapinero");
        barriosCobertura.add("Teusaquillo");
        barriosCobertura.add("La Candelaria");
        barriosCobertura.add("Usaquén");
        barriosCobertura.add("Cedritos");
        barriosCobertura.add("Fontibón");
        barriosCobertura.add("Suba");
        barriosCobertura.add("Kennedy");
        barriosCobertura.add("Bosa");
    }

    private void inicializarDistanciasBarrios() {
        distanciasBarrios.put("Chapinero", 1.5);
        distanciasBarrios.put("Teusaquillo", 3.0);
        distanciasBarrios.put("La Candelaria", 4.5);
        distanciasBarrios.put("Usaquén", 6.0);
        distanciasBarrios.put("Cedritos", 8.0);
        distanciasBarrios.put("Fontibón", 11.0);
        distanciasBarrios.put("Suba", 12.0);
        distanciasBarrios.put("Kennedy", 14.0);
        distanciasBarrios.put("Bosa", 18.0);
    }

    public DomiService() {                      // Este es el Constructor
        pedidos = new ArrayList<>();
        repartidores = new ArrayList<>();
    }

    public void registrarCliente(String cedula, String nombre, String telefono, String barrio, String direccion) {
        if (clientes.containsKey(cedula)) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con esa cédula");
        }

        Cliente nuevoCliente = new Cliente(cedula, nombre, telefono, barrio, direccion);
        clientes.put(cedula, nuevoCliente);
    }

    public void agregarPedido(Pedido pedido) {  // Agg un nuevo pedido a la lista de Pedidos.
        pedidos.add(pedido);
    }

    public void agregarRepartidor(Repartidor repartidor) {  // Agg un nuevo repartidor a la lista de repartidores.
        repartidores.add(repartidor);
    }

    //Recorre repartidores: si están disponibles y si puede llevarlo
    //Guarda a los que cumplan y los guarda en una cotización
    public ArrayList<Cotizacion> cotizar(double km, boolean esGrande, boolean lluvia) {
        ArrayList<Cotizacion> cotizaciones = new ArrayList<>();
        if (km <= 0 || !Double.isFinite(km)) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero");
        }
        for (Repartidor repartidor : repartidores) {
            if (repartidor.isDisponible() && repartidor.puedeLlevarPedido(km, esGrande, lluvia)) {
                double costo = repartidor.calcularCosto(km, lluvia);
                double tiempo = repartidor.calcularTiempo(km);
                cotizaciones.add(new Cotizacion(repartidor, costo, tiempo));
            }
        }
        return cotizaciones;
    }

    //Toma las cotizaciones y elige el de menor costo, también si empatan compara tiempos
    //Se marca al repartidor como no disponible devuevle la mejor cotización
    public Cotizacion asignar(ArrayList<Cotizacion> cotizaciones) {
        if (cotizaciones == null || cotizaciones.isEmpty()) {
            return null;
        }
        Cotizacion mejor = null;
        for (Cotizacion actual : cotizaciones) {
            // Evita asignar un repartidor que ya no esté disponible.
            if (!actual.getRepartidor().isDisponible()) {
                continue;
            }

            if (mejor == null || actual.getCosto() < mejor.getCosto() || (Double.compare(actual.getCosto(), mejor.getCosto()) == 0 && actual.getTiempo() < mejor.getTiempo())) {
                mejor = actual;
            }
        }
        if (mejor == null) {
            return null;
        }
        mejor.getRepartidor().setDisponible(false);
        return mejor;

    }

// Buscar Pedido

    public Pedido buscarPedido(int numeroPedido) {

        for (Pedido pedido : pedidos) {

            if (pedido.getNumero() == numeroPedido) {
                return pedido;
            }

        }

        return null;
    }

// Entregar Pedido

    public void entregarPedido(int numeroPedido) {

        Pedido pedido = buscarPedido(numeroPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }

        pedido.setEstado("ENTREGADO");

        pedido.getRepartidorAsignado().setDisponible(true);
    }

// Cancelar Pedido

    public void cancelarPedido(int numeroPedido) {

        Pedido pedido = buscarPedido(numeroPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }

        pedido.setEstado("CANCELADO");

        pedido.getRepartidorAsignado().setDisponible(true);
    }

    // Cierre del Día

    public void cierreDelDia() {

        int entregados = 0;
        int cancelados = 0;
        int enCamino = 0;

        double ingresos = 0;

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado().equals("ENTREGADO")) {

                entregados++;
                ingresos += pedido.getCosto();

            } else if (pedido.getEstado().equals("CANCELADO")) {

                cancelados++;

            } else if (pedido.getEstado().equals("EN_CAMINO")) {

                enCamino++;

            }
        }

        System.out.println("\n===== CIERRE DEL DÍA  =====");

        System.out.println("Total pedidos: " + pedidos.size());

        System.out.println("Entregados: " + entregados);

        System.out.println("Cancelados: " + cancelados);

        System.out.println("En camino: " + enCamino);

        System.out.println("Ingresos del día: $" + ingresos);
    }

}


