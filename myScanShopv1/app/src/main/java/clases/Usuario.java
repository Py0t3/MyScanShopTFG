package clases;

public class Usuario {

    protected String nombre;
    protected String id;
    protected String puesto;

    public Usuario (  String id,String nombre , String puesto) {
        this.nombre = nombre;
        this.id = id;
        this.puesto= puesto;
    }

    public String getNombre () {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setNombre ( String nombre ) {
        this.nombre = nombre;
    }

    public void setPuesto( String puesto ) {
        this.puesto = puesto;
    }

    public String getId () {


        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }
}
