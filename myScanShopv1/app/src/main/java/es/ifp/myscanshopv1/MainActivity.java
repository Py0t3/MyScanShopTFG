package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import adaptadores.AdaptadorProductos;
import clases.DataBaseSQLite;
import clases.Producto;

public class MainActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView datosText;
    protected Button botonScan;
    protected Button botonAdd;
    protected Button botonAddCesta;
    protected Button botonCaja;
    protected Button botonBuscar;
    protected Intent pasarPantalla;
    protected Bundle paquete;
    protected DataBaseSQLite db;
    protected TextView labelDatos;
    protected String contenidoLabel="";
    protected float euros = 0.0f;
    protected String eurosPaquete="";
    protected String[] partes;
    protected String codigoBarras;
    protected String nombre;
    protected String productoManual;



    protected GridView grid;
    protected ArrayList<Producto> listaProductos;
    protected ArrayList<Producto> totalProductos;
    protected static ArrayList<String> cesta;

    private ListAdapter adaptador;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datosText = (TextView )findViewById ( R.id.labelDatos_main );
        botonScan = (Button )findViewById ( R.id.botonScan_main );
        botonAdd = (Button )findViewById ( R.id.botonAddd_main );
        botonCaja = (Button )findViewById ( R.id.botonCaja_main );
        botonAddCesta = (Button )findViewById ( R.id.botonAddCesta_main );
        botonBuscar = (Button )findViewById ( R.id.botonBuscar_main );
        labelDatos = (TextView )findViewById ( R.id.labelDatos_main );

        db= new DataBaseSQLite ( this );

        paquete = getIntent ().getExtras ();

        if(paquete !=null && paquete.getString ( "SCAN" )!=null)
        {

            codigoBarras= paquete.getString ( "SCAN" );

            Producto p = db.getProductoByCode ( codigoBarras  );

            labelDatos.setText ( p.getNombre () + "\n" + p.getPrecio ()+ " €" +"\n" +  p.getDescripcion () );


            botonCaja.setText ( "TOTAL: " + ""+euros+ " EUROS" );

        }
        else if(paquete !=null && paquete.getString ( "NOMBRE" )!=null)
        {
            nombre = paquete.getString ( "NOMBRE" );
            Producto p = db.getNote ( "'"+nombre + "'"  );

            labelDatos.setText ( p.getNombre () + "\n" + p.getPrecio ()+ " €" +"\n" +  p.getDescripcion () );


            botonCaja.setText ( "TOTAL: " + ""+euros+ " EUROS" );
        }
        else if(paquete !=null && paquete.getString ( "PRODUCTO" )!=null)
        {
            productoManual = paquete.getString ( "PRODUCTO" ) ;
            labelDatos.setText ( productoManual );
        }

        if(CajaActivity.listaCaja!=null)
        {
            cesta= CajaActivity.listaCaja;
            euros = totalCompraEuros ();
            botonCaja.setText ( "TOTAL: " +euros+ " EUROS" );
            eurosPaquete = ""+euros;
        }
        else if(ScanActivity.cesta!=null)
        {
            cesta = ScanActivity.cesta;
            euros = totalCompraEuros ();
            botonCaja.setText ( "TOTAL: " +euros+ " EUROS" );
            eurosPaquete = ""+euros;
        }
        else
        {
            cesta = new ArrayList<> (  );
        }

        totalProductos = new ArrayList<> (  );

        grid= (GridView ) findViewById ( R.id.gridList_main );
        listaProductos= getListaProductos ();
        adaptador= new AdaptadorProductos ( this, listaProductos );
        grid.setAdapter ( adaptador);

        //Selección de producto desde la lista
        grid.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int i , long l ) {

                Producto p= ( Producto ) adapterView.getItemAtPosition ( i );
                labelDatos.setText ( p.getNombre () + "\n" + p.getPrecio ()+ " €" +"\n" +  p.getDescripcion () );

            }
        } );


        botonBuscar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( MainActivity.this, BuscarActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );


        //Botón para añadir producto no registrado
        botonAdd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                pasarPantalla = new Intent ( MainActivity.this,AddManualActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );


        //Boton para escanear los codigos de barras
        botonScan.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( MainActivity.this, ScanActivity.class );
                pasarPantalla.putExtra ( "TOTALEUROS", eurosPaquete );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        //Botón añadir a la cesta
        botonAddCesta.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                contenidoLabel = labelDatos.getText ().toString ();

                if( contenidoLabel !="")
                {

                    partes = contenidoLabel.split ( "\n" );

                    cesta.add ( partes[0] );
                    euros = totalCompraEuros ();
                    botonCaja.setText ( "TOTAL: " +euros+ " EUROS" );
                    eurosPaquete = ""+euros;
                }
            }
        } );

        //Botón Ir a Caja Total
        botonCaja.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( MainActivity.this, CajaActivity.class );
                pasarPantalla.putExtra ( "TOTALEUROS", eurosPaquete);
                startActivity ( pasarPantalla );
                finish ();
            }
        } );


    }
    //Botón Volver Superior
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        //Vacia la cesta y resetea valores a 0
        CajaActivity.listaCaja = null;
        pasarPantalla = new Intent (MainActivity.this, MenuActivity.class);
        startActivity(pasarPantalla);
        finish();
    }
    //Se crea el ArrayList provisional para ver que funciona la vista de gridview. Se sustituirá por consulta a base de datos
    public ArrayList <Producto> getListaProductos () {
        listaProductos=new ArrayList<> (  );
        listaProductos = db.getAllNotes ();

      return listaProductos;
   }
   public ArrayList<Producto> totalCompra(){

        ArrayList<Producto> compra= new ArrayList<> (  );

       for(int i=0; i< cesta.size () ; i++)
       {
           Producto p = db.getNote ( "'"+cesta.get (i) +"'");

           compra.add ( p );
       }

       return compra;
   }
   public float totalCompraEuros(){

       euros=0.0f;

        for(int i=0; i<totalCompra ().size () ; i++)
        {
            euros = euros + totalCompra ().get ( i ).getPrecio ();
        }

        return euros;
   }


}