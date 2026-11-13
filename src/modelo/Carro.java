package modelo;

public class Carro extends Repartidor {

        public Carro( String nombre, String codigo) {

            super(nombre,codigo);
        }


    @Override
    public double calcularCosto(double km, boolean lluvia) {
        double costo= 5000 + (km * 1200);
        if(lluvia){
            return costo *1.20;
        }
        return costo;
    }

    @Override
    public double calcularTiempo(double km) {
        return(km/25.0) * 60;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande, boolean lluvia) {
        return true;
    }
}
