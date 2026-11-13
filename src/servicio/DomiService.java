package servicio;

import modelo.Cotizacion;
import modelo.Pedido;
import java.util.ArrayList;
import modelo.Repartidor;


public class DomiService {

    private ArrayList<Pedido> pedidos;           // En esta lista es donde se almacenarán todos los pedidos.
    private ArrayList<Repartidor> repartidores;  // En esta lista es donde se almacenarán todos los repartidores.


    public DomiService() {                      // Este es el Constructor
        pedidos = new ArrayList<>();
        repartidores = new ArrayList<>();
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

            if (mejor == null|| actual.getCosto() < mejor.getCosto() || (Double.compare(actual.getCosto(), mejor.getCosto()) == 0 && actual.getTiempo() < mejor.getTiempo())) {
                mejor = actual;
            }
        }
        if (mejor == null) {
            return null;
        }
        mejor.getRepartidor().setDisponible(false);
        return mejor;

    }
}






