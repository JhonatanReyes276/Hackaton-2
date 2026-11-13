package modelo;

public class Cotizacion {

    private Repartidor repartidor;
    private double costo;
    private double tiempo;

    public Cotizacion(Repartidor repartidor, double costo, double tiempo) {
        this.repartidor = repartidor;
        this.costo = costo;
        this.tiempo = tiempo;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public double getCosto() {
        return costo;
    }

    public double getTiempo() {
        return tiempo;
    }
}