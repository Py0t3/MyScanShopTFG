package clases;

public class Venta {

    protected int id;
    protected int idVendedor;
    protected int idCliente;
    protected String urlFactura;
    protected String fecha;

    public Venta ( int id , int idVendedor , int idCliente , String urlFactura , String fecha ) {
        this.id = id;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.urlFactura = urlFactura;
        this.fecha = fecha;
    }

    public int getId () {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public int getIdVendedor () {
        return idVendedor;
    }

    public void setIdVendedor ( int idVendedor ) {
        this.idVendedor = idVendedor;
    }

    public int getIdCliente () {
        return idCliente;
    }

    public void setIdCliente ( int idCliente ) {
        this.idCliente = idCliente;
    }

    public String getUrlFactura () {
        return urlFactura;
    }

    public void setUrlFactura ( String urlFactura ) {
        this.urlFactura = urlFactura;
    }

    public String getFecha () {
        return fecha;
    }

    public void setFecha ( String fecha ) {
        this.fecha = fecha;
    }
}
