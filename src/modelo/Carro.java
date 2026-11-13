package modelo;

public class Carro extends Repartidor {

        public Carro( String nombre, String codigo) {

            super(nombre,codigo,true);
        }


    @Override
    public double calcularCosto(double km) {
        return 0;
    }

    @Override
    public double calcularTiempo(double km) {
        return 0;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande) {
        return false;
    }
}
