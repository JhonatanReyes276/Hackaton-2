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
        barriosCobertura.add("chapinero");
        barriosCobertura.add("teusaquillo");
        barriosCobertura.add("la candelaria");
        barriosCobertura.add("usaquen");
        barriosCobertura.add("cedritos");
        barriosCobertura.add("fontibon");
        barriosCobertura.add("suba");
        barriosCobertura.add("kennedy");
        barriosCobertura.add("bosa");
    }

    private void inicializarDistanciasBarrios() {
        distanciasBarrios.put("chapinero", 1.5);
        distanciasBarrios.put("teusaquillo", 3.0);
        distanciasBarrios.put("la candelaria", 4.5);
        distanciasBarrios.put("usaquen", 6.0);
        distanciasBarrios.put("cedritos", 8.0);
        distanciasBarrios.put("fontibon", 11.0);
        distanciasBarrios.put("suba", 12.0);
        distanciasBarrios.put("kennedy", 14.0);
        distanciasBarrios.put("bosa", 18.0);
    }

    // Buscar cliente por cédula
    public Cliente buscarCliente(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }

        return clientes.get(cedula.trim());
    }

    // Listar todos los clientes registrados
    public ArrayList<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    // Editar los datos de un cliente
    public void editarCliente(String cedula, String nuevaDireccion, String nuevoBarrio, String nuevoTelefono) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        String barrioNormalizado = normalizarBarrio(nuevoBarrio);
        if (!barriosCobertura.contains(barrioNormalizado)) {
            throw new IllegalArgumentException("No tenemos cobertura en ese barrio");
        }

        //cliente temporal para validar los datos
        Cliente clienteTemporal = new Cliente(
                cliente.getCedula(),
                cliente.getNombre(),
                nuevaDireccion,
                nuevoBarrio,
                nuevoTelefono
        );

        // Si todos los datos son válidos, actualizamos el original
        cliente.setDireccion(clienteTemporal.getDireccion());
        cliente.setBarrio(clienteTemporal.getBarrio());
        cliente.setTelefono(clienteTemporal.getTelefono());
    }

    // Eliminar un cliente
    public void eliminarCliente(String cedula) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        // No se puede eliminar si tiene pedidos en camino
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().getCedula().equals(cliente.getCedula()) && pedido.getEstado().equals("EN_CAMINO")) {
                throw new IllegalArgumentException("No se puede eliminar el cliente porque tiene pedidos en camino");
            }
        }

        clientes.remove(cliente.getCedula());
    }

    // Consultar distancia según el barrio
    public double obtenerDistanciaBarrio(String barrio) {
        String barrioNormalizado = normalizarBarrio(barrio);
        Double distancia = distanciasBarrios.get(barrioNormalizado);
        if (distancia == null) {
            throw new IllegalArgumentException("No tenemos cobertura en ese barrio");
        }
        return distancia;
    }

    private String normalizarBarrio(String barrio) {
        if (barrio == null || barrio.isBlank()) {
            throw new IllegalArgumentException("El barrio es obligatorio");
        }
        return java.text.Normalizer.normalize(barrio.trim().toLowerCase(), java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    public DomiService() {
        pedidos = new ArrayList<>();
        repartidores = new ArrayList<>();

        listaBarriosCobertura();
        inicializarDistanciasBarrios();
    }

    public void registrarCliente(String cedula, String nombre, String telefono, String barrio, String direccion) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        cedula = cedula.trim();
        if (clientes.containsKey(cedula)) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con esa cédula");
        }
        String barrioNormalizado = normalizarBarrio(barrio);
        if (!barriosCobertura.contains(barrioNormalizado)) {
            throw new IllegalArgumentException(
                    "No tenemos cobertura en ese barrio");
        }
        Cliente nuevoCliente = new Cliente(cedula, nombre, direccion, barrio, telefono);
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

    public void entregarPedido(int numeroPedido, int estrellas) {

        Pedido pedido = buscarPedido(numeroPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }

        pedido.setEstado("ENTREGADO");

        pedido.getRepartidorAsignado().setDisponible(true);

        pedido.getRepartidorAsignado().recibirCalificacion(estrellas);

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
        double ingresosBici = 0;
        double ingresosMoto = 0;
        double ingresosCarro = 0;

        Repartidor estrella = null;
        double mejorPromedio = 0;

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado().equals("ENTREGADO")) {

                entregados++;
                ingresos += pedido.getCosto();

                if (pedido.getRepartidorAsignado().getClass().getSimpleName().equals("Bici")) {

                    ingresosBici += pedido.getCosto();

                } else if (pedido.getRepartidorAsignado().getClass().getSimpleName().equals("Moto")) {

                    ingresosMoto += pedido.getCosto();

                } else if (pedido.getRepartidorAsignado().getClass().getSimpleName().equals("Carro")) {

                    ingresosCarro += pedido.getCosto();

                }

            } else if (pedido.getEstado().equals("CANCELADO")) {

                cancelados++;

            } else if (pedido.getEstado().equals("EN_CAMINO")) {

                enCamino++;

            }
        }

        for (Repartidor repartidor : repartidores) {
            double promedio = repartidor.obtenerPromedio();

            if (promedio > 0 && promedio > mejorPromedio) {

                mejorPromedio = promedio;
                estrella = repartidor;

            }
        }

        System.out.println("\n===== CIERRE DEL DÍA  =====");

        System.out.println("Total pedidos: " + pedidos.size());

        System.out.println("Entregados: " + entregados);

        System.out.println("Cancelados: " + cancelados);

        System.out.println("En camino: " + enCamino);

        System.out.println("Ingresos del día: $" + ingresos);

        System.out.println("Ingresos Bicis: $" + ingresosBici);

        System.out.println("Ingresos Motos: $" + ingresosMoto);

        System.out.println("Ingresos Carros: $" + ingresosCarro);

        if (estrella != null) {

            System.out.println("\nRepartidor estrella:");

            System.out.println(estrella.getNombre()
                    + "  Promedio: "
                    + mejorPromedio
            );

        }

    }

}
