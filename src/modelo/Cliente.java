package modelo;

public class Cliente {
    private String cedula;
    private String nombre;
    private String direccion;
    private String telefono;
    private String barrio;

    public Cliente(String cedula, String nombre, String direccion, String barrio, String telefono) {
        setCedula(cedula);
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
        setBarrio(barrio);
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        this.cedula = cedula.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        this.direccion = direccion.trim();
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {

        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        telefono = telefono.trim();

        if (telefono.length() < 7 || telefono.length() > 10) {
            throw new IllegalArgumentException(
                    "El teléfono debe tener entre 7 y 10 dígitos"
            );
        }

        for (int i = 0; i < telefono.length(); i++) {
            char caracter = telefono.charAt(i);

            if (!Character.isDigit(caracter)) {
                throw new IllegalArgumentException(
                        "El teléfono solo debe contener números"
                );
            }
        }

        this.telefono = telefono;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        if (barrio == null || barrio.isBlank()) {
            throw new IllegalArgumentException("Hay un error en el barrio");
        }
        this.barrio = barrio.trim();
    }
}