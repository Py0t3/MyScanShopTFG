package es.ifp.myscanshopv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.security.PrivateKey;

import clases.DataBaseSQLite;
import clases.Producto;

public class InfoProductoActivity extends AppCompatActivity {

    protected ImageView imagen;
    protected TextView labelNombre;
    protected TextView labelID;
    protected TextView labelPrecio;
    protected TextView labelBarCode;
    protected TextView labelDescripcion;
    protected Button botonVolver;
    private Intent pasarPAntalla;
    private Bundle paquete;
    protected DataBaseSQLite db;
    protected String urlImagen;
    protected String nombre;
    protected int id;
    protected float precio;
    protected String barCode;
    protected String descripcion;
    protected Producto p;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_info_producto );

        db = new DataBaseSQLite ( this );
        imagen = (ImageView) findViewById ( R.id.imagen_info );
        labelNombre = (TextView ) findViewById ( R.id.labelNombre_info );
        labelID = (TextView ) findViewById ( R.id.labelID_info );
        labelPrecio = (TextView ) findViewById ( R.id.labelPrecio_info );
        labelBarCode = (TextView ) findViewById ( R.id.labelBar_info );
        labelDescripcion = (TextView ) findViewById ( R.id.labelDescripcion_info );
        botonVolver = (Button ) findViewById ( R.id.botonVolverl_info );

        paquete = getIntent ().getExtras ();

        if(paquete != null)
        {
            nombre = paquete.getString ( "NOMBRE" );
            p = db.getNote ( "'"+ nombre + "'" );
            Picasso.get().load(p.getUrlImagen ()).error ( R.drawable.logoxl ).into(imagen);
            labelNombre.setText ( p.getNombre () );
            labelID.setText ( "ID: "+p.getIdentificador () );
            labelPrecio.setText ( "PVP: " + p.getPrecio ()+ " EUROS" );
            labelBarCode.setText ( "BARCODE: " + p.getCodigoBarras () );
            labelDescripcion.setText ( "Descripción:\n \n"+ p.getDescripcion () );

        }
        botonVolver.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPAntalla = new Intent ( InfoProductoActivity.this, InventarioActivity.class );
                startActivity ( pasarPAntalla );
                finish ();
            }
        } );

    }
}