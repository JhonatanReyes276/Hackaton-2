package servicio;

import modelo.Bici;
import modelo.Carro;
import modelo.Cliente;
import modelo.Cotizacion;
import modelo.Dron;
import modelo.Moto;
import modelo.Pedido;
import modelo.Repartidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


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

    public DomiService() {
        pedidos = new ArrayList<>();
        repartidores = new ArrayList<>();

        listaBarriosCobertura();
        inicializarDistanciasBarrios();
    }

    // ===================== BARRIOS =====================

    // Devuelve los barrios con cobertura (ordenados) para mostrarlos en el menú
    public Set<String> getBarriosCobertura() {
        return new TreeSet<>(barriosCobertura);
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

    // ===================== CLIENTES =====================

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

    // ===================== REPARTIDORES =====================

    public void agregarRepartidor(Repartidor repartidor) {  // Agg un nuevo repartidor a la lista de repartidores.
        repartidores.add(repartidor);
    }

    // Crea el repartidor según el tipo (BICI, MOTO, CARRO, DRON) y lo agrega a la lista
    public void registrarRepartidor(String tipo, String nombre, String codigo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de repartidor es obligatorio");
        }
        if (codigo != null && buscarRepartidor(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un repartidor con ese código");
        }

        Repartidor nuevo;
        switch (tipo.trim().toUpperCase()) {
            case "BICI":
                nuevo = new Bici(nombre, codigo);
                break;
            case "MOTO":
                nuevo = new Moto(nombre, codigo);
                break;
            case "CARRO":
                nuevo = new Carro(nombre, codigo);
                break;
            case "DRON":
                nuevo = new Dron(nombre, codigo);
                break;
            default:
                throw new IllegalArgumentException("Tipo de repartidor inválido");
        }
        agregarRepartidor(nuevo);
    }

    public Repartidor buscarRepartidor(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return repartidor;
            }
        }
        return null;
    }

    public ArrayList<Repartidor> listarRepartidores() {
        return new ArrayList<>(repartidores);
    }

    // ===================== COTIZAR Y ASIGNAR =====================

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

    // ===================== PEDIDOS =====================

    public void agregarPedido(Pedido pedido) {  // Agg un nuevo pedido a la lista de Pedidos.
        pedidos.add(pedido);
    }

    // Crea el pedido con la cotización ya asignada y lo guarda en la lista.
    // Si algo falla al crearlo, libera al repartidor que ya había sido asignado.
    public Pedido crearPedido(Cliente cliente, String descripcion, boolean esGrande, double km, Cotizacion cotizacion) {
        if (cotizacion == null) {
            throw new IllegalArgumentException("No hay una cotización asignada");
        }
        try {
            Pedido pedido = new Pedido(cliente, descripcion, esGrande, km,
                    cotizacion.getRepartidor(), cotizacion.getCosto(), cotizacion.getTiempo());
            agregarPedido(pedido);
            return pedido;
        } catch (RuntimeException e) {
            cotizacion.getRepartidor().setDisponible(true);
            throw e;
        }
    }

    public ArrayList<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos);
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

        // Se valida antes para no dejar el pedido entregado con una calificación inválida
        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
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

    // ===================== CIERRE DEL DÍA =====================

    public void cierreDelDia() {

        int entregados = 0;
        int cancelados = 0;
        int enCamino = 0;

        double ingresos = 0;
        double ingresosBici = 0;
        double ingresosMoto = 0;
        double ingresosCarro = 0;
        double ingresosDron = 0;

        Repartidor estrella = null;
        double mejorPromedio = 0;

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado().equals("ENTREGADO")) {

                entregados++;
                ingresos += pedido.getCosto();

                String tipo = pedido.getRepartidorAsignado().getClass().getSimpleName();

                if (tipo.equals("Bici")) {
                    ingresosBici += pedido.getCosto();
                } else if (tipo.equals("Moto")) {
                    ingresosMoto += pedido.getCosto();
                } else if (tipo.equals("Carro")) {
                    ingresosCarro += pedido.getCosto();
                } else if (tipo.equals("Dron")) {
                    ingresosDron += pedido.getCosto();
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
        System.out.println("Ingresos Drones: $" + ingresosDron);

        if (estrella != null) {
            System.out.println("\nRepartidor estrella:");
            System.out.println(estrella.getNombre() + "  Promedio: " + mejorPromedio);
        }

    }

}