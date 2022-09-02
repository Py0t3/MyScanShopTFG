package clases;

public class Cliente {

    protected String id;
    protected String nombre;
    protected String email;
    protected String tlf;
    protected String direccion;
    protected String dni;



    public Cliente ( String id ,String nombre ,  String email , String tlf, String direccion, String dni ) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tlf = tlf;
        this.direccion = direccion;
        this.dni = dni;
    }


    public String getId () {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre ( String nombre ) {
        this.nombre = nombre;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getTlf () {
        return tlf;
    }

    public void setTlf ( String tlf ) {
        this.tlf = tlf;
    }

    public String getDireccion () {
        return direccion;
    }

    public void setDireccion ( String direccion ) {
        this.direccion = direccion;
    }

    public String getDni () {
        return dni;
    }

    public void setDni ( String dni ) {
        this.dni = dni;
    }
}
