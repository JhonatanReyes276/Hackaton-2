package modelo;

public class Bici extends Repartidor{
    public Bici( String nombre, String codigo) {
        super(nombre,codigo);
    }

    @Override
    public double calcularCosto(double km) {
        return 2000 + (km* 800);
    }

    @Override
    public double calcularTiempo(double km) {
        return (km / 15.0) *60;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande) {
        return km <= 5.0 && !esGrande;
    }
}
