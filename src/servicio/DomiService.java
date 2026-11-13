package servicio;

import modelo.Pedido;
import java.util.ArrayList;
import modelo.Repartidor;


public class DomiService {

    private ArrayList<Pedido> pedidos;
    private ArrayList<Repartidor> repartidores;


    public DomiService() {
        pedidos = new ArrayList<>();
        repartidores = new ArrayList<>();
    }


    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public void agregarRepartidor(Repartidor repartidor) {
        repartidores.add(repartidor);
    }


}
// Pendiente Estructura de Pedido para realizar el metodo de buscarPedido
// Pendiente por realizar K.V: Cambios de estado y cierre del dia






