package modelo;

public class Dron extends Repartidor {

    public Dron(String nombre, String codigo) {
        super(nombre, codigo);
    }

    @Override
    public double calcularCosto(double km, boolean lluvia) {
        double costo = 4000 + (km * 600);
        if (lluvia) {
            return costo * 1.20;
        }
        return costo;
    }

    @Override
    public double calcularTiempo(double km) {
        return (km / 50.0) * 60;
    }

    @Override
    public boolean puedeLlevarPedido(double km, boolean esGrande, boolean lluvia) {
        // Debe cumplir 3 cosas al tiempo: NO llueve, km <= 8.0 y NO es grande
        return !lluvia && km <= 8.0 && !esGrande;
    }
}