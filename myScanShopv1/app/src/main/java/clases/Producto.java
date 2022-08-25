package clases;

public class Producto {

    private String id;
    private String nombre;
    private String precio;
    private String urlImagen;
    private String codigoBarras;
    private String descripcion;


    public Producto ( String id,String urlImagen,String nombre, String precio, String codigoBarras, String descripcion){

        this.id =id;
        this.nombre = nombre;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.codigoBarras = codigoBarras;
        this.descripcion = descripcion;
    }

    public String getNombre(){

        return nombre;
    }

    public void setIdentificador ( String id ) {
        this.id = id;
    }

    public void setUrlImagen ( String urlImagen ) {
        this.urlImagen = urlImagen;
    }

    public String getDescripcion () {
        return descripcion;
    }

    public void setDescripcion ( String descripcion ) {
        this.descripcion = descripcion;
    }

    public String getCodigoBarras () {
        return codigoBarras;
    }

    public void setCodigoBarras ( String codigoBarras ) {
        this.codigoBarras = codigoBarras;
    }

    public void setNombre( String nombre){
        this.nombre = nombre;
    }

    public String getUrlImagen () {
        return urlImagen;
    }

    public void setIdDrawable ( String urlImagen ) {
        this.urlImagen = urlImagen;
    }

    public String getIdentificador() {
        return id;
    }

    public String getPrecio () {
        return precio;
    }

    public void setPrecio ( String precio ) {
        this.precio = precio;
    }
}
