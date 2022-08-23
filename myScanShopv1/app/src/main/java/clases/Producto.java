package clases;

public class Producto {

    private int identificador;
    private String nombre;
    private float precio;
    private String urlImagen;
    private String codigoBarras;
    private String descripcion;


    public Producto ( int id,String nombre, float precio, String urlImagen, String codigoBarras, String descripcion){

        this.identificador =id;
        this.nombre = nombre;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.codigoBarras = codigoBarras;
        this.descripcion = descripcion;
    }

    public Producto ( String nombre, String urlImagen){

        this.nombre = nombre;
        this.urlImagen = urlImagen;
    }
    public Producto ( String nombre,float precio, String urlImagen, String descripcion)
    {

        this.nombre = nombre;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.descripcion = descripcion;
    }

    public Producto ( String nombre, float precio){

        this.nombre = nombre;
        this.precio = precio;
    }
    public Producto ( int identificador, String nombre, float precio){

        this.identificador= identificador;
        this.nombre = nombre;
        this.precio = precio;


    }
    public String getNombre(){

        return nombre;
    }

    public void setIdentificador ( int id ) {
        this.identificador = id;
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

    public int getIdentificador() {
        return nombre.hashCode();
    }

    public float getPrecio () {
        return precio;
    }

    public void setPrecio ( float precio ) {
        this.precio = precio;
    }
}
