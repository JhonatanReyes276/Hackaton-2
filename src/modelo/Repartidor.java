package modelo;

public abstract class Repartidor {

    private String nombre ;
    private String codigo;
    private boolean disponible ;


    public Repartidor(String codigo, String nombre) {

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

    public abstract double calcularCosto(double km);

    public abstract double calcularTiempo(double km);

    public abstract boolean puedeLlevarPedido(double km, boolean esGrande);
}
