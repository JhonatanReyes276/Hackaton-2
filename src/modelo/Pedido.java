package modelo;

import java.util.Set;

public class Pedido {

    private static int contadorPedidos = 1;
    private static final Set<String> estados_validos = Set.of("EN_CAMINO", "ENTREGADO", "CANCELADO");

    private int numero;
    private Cliente cliente;
    private String descripcion;
    private boolean esGrande;
    private double km;
    private Repartidor repartidorAsignado;
    private double costo;
    private double tiempo;
    private String estado;

    public Pedido(Cliente cliente, String descripcion, boolean esGrande, double km, Repartidor repartidorAsignado, double costo, double tiempo) {
        setCliente(cliente);
        setDescripcion(descripcion);
        this.esGrande = esGrande;
        setKm(km);
        setRepartidorAsignado(repartidorAsignado);
        setCosto(costo);
        setTiempo(tiempo);
        setEstado("EN_CAMINO");
        this.numero = contadorPedidos++;
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El pedido debe tener un cliente");
        }
        this.cliente = cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción del pedido no puede estar vacía");
        }
        this.descripcion = descripcion.trim();
    }

    public boolean isEsGrande() {
        return esGrande;
    }

    public void setEsGrande(boolean esGrande) {
        this.esGrande = esGrande;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public Repartidor getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public void setRepartidorAsignado(Repartidor repartidorAsignado) {
        if (repartidorAsignado == null) {
            throw new IllegalArgumentException("El pedido debe tener un repartidor asignado");
        }
        this.repartidorAsignado = repartidorAsignado;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo del pedido no puede ser negativo");
        }
        this.costo = costo;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo estimado del pedido debe ser mayor a 0");
        }
        this.tiempo = tiempo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String nuevoEstado) {
        String estadoArreglado = nuevoEstado.trim().toUpperCase();
        if (!estados_validos.contains(estadoArreglado)) {
            throw new IllegalArgumentException("Estado inválido. Solo se permite: EN_CAMINO, ENTREGADO o CANCELADO.");
        }
        if ("ENTREGADO".equals(this.estado) || "CANCELADO".equals(this.estado)) {
            throw new IllegalArgumentException("El pedido #" + this.numero + " ya está en estado final (" + this.estado + ")");
        }
        this.estado = estadoArreglado;
    }
}