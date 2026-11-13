package modelo;

public class Carro extends Repartidor {

        public Carro( String nombre, String codigo) {

            super(nombre,codigo);
        }


    @Override
    public double calcularCosto(double km) {
        return 5000 + (km * 1200);
    }

    @Override
    public double calcularTiempo(double km) {
        return(km/25.0) * 60;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande) {
        return true;
    }
}
