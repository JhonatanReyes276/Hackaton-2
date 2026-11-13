package app;

import modelo.Cliente;
import modelo.Cotizacion;
import modelo.Pedido;
import modelo.Repartidor;
import servicio.DomiService;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    private final DomiService servicio;
    private final Scanner sc = new Scanner(System.in);

    public Menu(DomiService servicio) {
        this.servicio = servicio;
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        listarClientes();
                        break;
                    case 4:
                        editarCliente();
                        break;
                    case 5:
                        eliminarCliente();
                        break;
                    case 6:
                        registrarRepartidor();
                        break;
                    case 7:
                        listarRepartidores();
                        break;
                    case 8:
                        nuevoPedido();
                        break;
                    case 9:
                        entregarPedido();
                        break;
                    case 10:
                        cancelarPedido();
                        break;
                    case 11:
                        listarPedidos();
                        break;
                    case 12:
                        servicio.cierreDelDia();
                        break;
                    case 0:
                        System.out.println("¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción inválida, intenta de nuevo.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        String[] opciones = {
                "Registrar cliente",
                "Buscar cliente",
                "Listar clientes",
                "Editar cliente",
                "Eliminar cliente",
                "Registrar repartidor",
                "Listar repartidores",
                "Nuevo pedido",
                "Entregar pedido",
                "Cancelar pedido",
                "Listar pedidos",
                "Cierre del día"
        };

        System.out.println("\n================ DOMI SERVICE ================");

        // Dos opciones por fila: 1 y 2, luego 3 y 4, y así sucesivamente
        for (int i = 0; i < opciones.length; i += 2) {
            System.out.printf("%2d. %-24s %2d. %s%n",
                    i + 1, opciones[i],
                    i + 2, opciones[i + 1]);
        }

        System.out.println(" 0. Salir");
    }

    // ===================== CLIENTES =====================

    private void registrarCliente() {
        System.out.println("\n--- Registrar cliente ---");
        String cedula = leerTexto("Cédula: ");
        String nombre = leerTexto("Nombre: ");
        String telefono = leerTexto("Teléfono (7 a 10 dígitos): ");
        System.out.println("Barrios con cobertura: " + servicio.getBarriosCobertura());
        String barrio = leerTexto("Barrio: ");
        String direccion = leerTexto("Dirección: ");

        servicio.registrarCliente(cedula, nombre, telefono, barrio, direccion);
        System.out.println("Cliente registrado correctamente.");
    }

    private void buscarCliente() {
        System.out.println("\n--- Buscar cliente ---");
        Cliente cliente = servicio.buscarCliente(leerTexto("Cédula: "));
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
        } else {
            mostrarCliente(cliente);
        }
    }

    private void listarClientes() {
        System.out.println("\n--- Clientes registrados ---");
        ArrayList<Cliente> clientes = servicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente cliente : clientes) {
            mostrarCliente(cliente);
            System.out.println("-----------------------------");
        }
    }

    private void editarCliente() {
        System.out.println("\n--- Editar cliente ---");
        String cedula = leerTexto("Cédula del cliente: ");
        if (servicio.buscarCliente(cedula) == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        String direccion = leerTexto("Nueva dirección: ");
        System.out.println("Barrios con cobertura: " + servicio.getBarriosCobertura());
        String barrio = leerTexto("Nuevo barrio: ");
        String telefono = leerTexto("Nuevo teléfono: ");

        servicio.editarCliente(cedula, direccion, barrio, telefono);
        System.out.println("Cliente actualizado correctamente.");
    }

    private void eliminarCliente() {
        System.out.println("\n--- Eliminar cliente ---");
        String cedula = leerTexto("Cédula del cliente: ");
        if (leerSiNo("¿Seguro que quieres eliminarlo?")) {
            servicio.eliminarCliente(cedula);
            System.out.println("Cliente eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void mostrarCliente(Cliente c) {
        System.out.println("Cédula:    " + c.getCedula());
        System.out.println("Nombre:    " + c.getNombre());
        System.out.println("Teléfono:  " + c.getTelefono());
        System.out.println("Barrio:    " + c.getBarrio());
        System.out.println("Dirección: " + c.getDireccion());
    }

    // ===================== REPARTIDORES =====================

    private void registrarRepartidor() {
        System.out.println("\n--- Registrar repartidor ---");
        System.out.println("1. Bici");
        System.out.println("2. Moto");
        System.out.println("3. Carro");
        System.out.println("4. Dron");
        int opcion = leerEntero("Tipo: ");

        String tipo;
        switch (opcion) {
            case 1:
                tipo = "BICI";
                break;
            case 2:
                tipo = "MOTO";
                break;
            case 3:
                tipo = "CARRO";
                break;
            case 4:
                tipo = "DRON";
                break;
            default:
                System.out.println("Tipo inválido.");
                return;
        }

        String nombre = leerTexto("Nombre: ");
        String codigo = leerTexto("Código: ");

        servicio.registrarRepartidor(tipo, nombre, codigo);
        System.out.println("Repartidor registrado correctamente.");
    }

    private void listarRepartidores() {
        System.out.println("\n--- Repartidores ---");
        ArrayList<Repartidor> repartidores = servicio.listarRepartidores();
        if (repartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }
        for (Repartidor r : repartidores) {
            System.out.printf("[%s] %s (%s) - %s - Promedio: %.1f%n",
                    r.getCodigo(),
                    r.getNombre(),
                    r.getClass().getSimpleName(),
                    r.isDisponible() ? "Disponible" : "Ocupado",
                    r.obtenerPromedio());
        }
    }

    // ===================== PEDIDOS =====================

    private void nuevoPedido() {
        System.out.println("\n--- Nuevo pedido ---");
        Cliente cliente = servicio.buscarCliente(leerTexto("Cédula del cliente: "));
        if (cliente == null) {
            System.out.println("Cliente no encontrado. Regístralo primero (opción 1).");
            return;
        }

        String descripcion = leerTexto("Descripción del pedido: ");
        boolean esGrande = leerSiNo("¿Es un pedido grande?");
        boolean lluvia = leerSiNo("¿Está lloviendo?");

        double km = servicio.obtenerDistanciaBarrio(cliente.getBarrio());
        System.out.println("Distancia a " + cliente.getBarrio() + ": " + km + " km");

        ArrayList<Cotizacion> cotizaciones = servicio.cotizar(km, esGrande, lluvia);
        if (cotizaciones.isEmpty()) {
            System.out.println("No hay repartidores disponibles que puedan llevar este pedido.");
            return;
        }

        System.out.println("\nOpciones disponibles:");
        for (Cotizacion c : cotizaciones) {
            System.out.printf("- %s (%s): $%,.0f | %.0f min%n",
                    c.getRepartidor().getNombre(),
                    c.getRepartidor().getClass().getSimpleName(),
                    c.getCosto(),
                    c.getTiempo());
        }

        Cotizacion elegida = servicio.asignar(cotizaciones);
        if (elegida == null) {
            System.out.println("No se pudo asignar ningún repartidor.");
            return;
        }

        Pedido pedido = servicio.crearPedido(cliente, descripcion, esGrande, km, elegida);

        System.out.println("\nPedido #" + pedido.getNumero() + " creado.");
        System.out.println("Repartidor asignado (el más económico): " + elegida.getRepartidor().getNombre());
        System.out.printf("Costo: $%,.0f | Tiempo estimado: %.0f min%n", pedido.getCosto(), pedido.getTiempo());
    }

    private void entregarPedido() {
        System.out.println("\n--- Entregar pedido ---");
        int numero = leerEntero("Número del pedido: ");
        int estrellas = leerEntero("Calificación del repartidor (1 a 5): ");

        servicio.entregarPedido(numero, estrellas);
        System.out.println("Pedido #" + numero + " entregado. ¡Gracias por la calificación!");
    }

    private void cancelarPedido() {
        System.out.println("\n--- Cancelar pedido ---");
        int numero = leerEntero("Número del pedido: ");

        servicio.cancelarPedido(numero);
        System.out.println("Pedido #" + numero + " cancelado.");
    }

    private void listarPedidos() {
        System.out.println("\n--- Pedidos ---");
        ArrayList<Pedido> pedidos = servicio.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (Pedido p : pedidos) {
            System.out.printf("#%d | %s | %s | %s | $%,.0f | %.0f min | %s%n",
                    p.getNumero(),
                    p.getCliente().getNombre(),
                    p.getDescripcion(),
                    p.getRepartidorAsignado().getNombre(),
                    p.getCosto(),
                    p.getTiempo(),
                    p.getEstado());
        }
    }

    // ===================== LECTURA DE DATOS =====================

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero válido.");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String respuesta = sc.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Responde con 's' o 'n'.");
        }
    }
}