package clases;

public class Venta {

    protected String id;
    protected String fecha;
    protected String idVendedor;
    protected String idCliente;
    protected String nombreCliente;
    protected String factura;

    public Venta (String id , String fecha ,String idVendedor , String idCliente ,String nombreCliente ,String factura ) {
        this.id = id;
        this.fecha = fecha;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.factura = factura;

    }

    public String getId () {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public String getFecha () {
        return fecha;
    }

    public void setFecha ( String fecha ) {
        this.fecha = fecha;
    }

    public String getIdVendedor () {
        return idVendedor;
    }

    public void setIdVendedor ( String idVendedor ) {
        this.idVendedor = idVendedor;
    }

    public String getIdCliente () {
        return idCliente;
    }

    public void setIdCliente ( String idCliente ) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente () {
        return nombreCliente;
    }

    public void setNombreCliente ( String nombreCliente ) {
        this.nombreCliente = nombreCliente;
    }

    public String getFactura () {
        return factura;
    }

    public void setFactura ( String factura ) {
        this.factura = factura;
    }
}

