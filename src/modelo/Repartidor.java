package modelo;

public abstract class Repartidor implements Calificable{

    private String nombre ;
    private String codigo;
    private boolean disponible ;

    private int sumarCalificacion= 0;
    private int cantidadCalificacion= 0;


    public Repartidor(String nombre, String codigo) {

        setCodigo(codigo);
        setNombre(nombre);
        this.disponible = true;
    }


    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.nombre = nombre.trim() ;
    }

    public void setCodigo(String codigo) {
        if(codigo == null || codigo.trim().isEmpty()){
            throw new IllegalArgumentException("El codigo no puede estar vacio");
        }
        this.codigo = codigo.trim();
    }

    public void setDisponible(boolean disponible) {

        this.disponible = disponible;
    }

    public String getNombre() {
        return nombre;
    }


    public String getCodigo() {
        return codigo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public abstract double calcularCosto(double km , boolean lluvia);

    public abstract double calcularTiempo(double km);

    public abstract boolean puedeLlevarPedido(double km, boolean esGrande, boolean lluvia);

    @Override
    public void recibirCalificacion( int estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException("La calificacion debe estar sobre 1 y no pasar de  5");
        }
        sumarCalificacion = sumarCalificacion + estrellas;
        cantidadCalificacion = cantidadCalificacion +1;
    }

    @Override
    public double obtenerPromedio (){
        if (cantidadCalificacion == 0){
            return 0.0;
        }
        return (double) sumarCalificacion / cantidadCalificacion;
    }
}
