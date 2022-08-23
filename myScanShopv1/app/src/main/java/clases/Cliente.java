package clases;

public class Cliente {

    protected String nombre;
    protected int id;
    protected String email;
    protected String dni;
    protected int tlf;

    public Cliente ( String nombre , int id , String email , String dni , int tlf ) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
        this.dni = dni;
        this.tlf = tlf;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre ( String nombre ) {
        this.nombre = nombre;
    }

    public int getId () {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getDni () {
        return dni;
    }

    public void setDni ( String dni ) {
        this.dni = dni;
    }

    public int getTlf () {
        return tlf;
    }

    public void setTlf ( int tlf ) {
        this.tlf = tlf;
    }
}
