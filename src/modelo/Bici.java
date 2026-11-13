package modelo;

public class Bici extends Repartidor{
    public Bici( String nombre, String codigo) {
        super(nombre,codigo);
    }

    @Override
    public double calcularCosto(double km , boolean lluvia) {
        double costo= 2000 + (km* 800); return lluvia ? costo *1.20:costo;
    }

    @Override
    public double calcularTiempo(double km) {
        return (km / 15.0) *60;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande,  boolean lluvia) {
        return !lluvia && km <= 5.0 && !esGrande;
    }
}
