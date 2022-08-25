package clases;

public class Usuario {

    protected String id;
    protected String user;
    protected String password;
    protected String nombre;
    protected String puesto;

    public Usuario (  String id, String user, String password,String nombre , String puesto) {
        this.nombre = nombre;
        this.id = id;
        this.puesto= puesto;
    }
    public String getUser () {
        return user;
    }

    public void setUser ( String user ) {
        this.user = user;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword ( String password ) {
        this.password = password;
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
