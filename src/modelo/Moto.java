package modelo;

public class Moto extends Repartidor {
    public Moto( String nombre, String codigo) {

        super(nombre,codigo);
    }

    @Override
    public double calcularCosto(double km, boolean lluvia) {
        double costo = 3000+ (km * 1000);
        if (lluvia){
            return costo * 1.20;
        }
        return costo;

    }



    @Override
    public double calcularTiempo(double km) {
        return (km / 30.0) * 60;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande,  boolean lluvia) {
        return km <= 15.0 && !esGrande;
    }
}
