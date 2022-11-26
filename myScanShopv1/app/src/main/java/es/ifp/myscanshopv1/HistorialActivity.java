package es.ifp.myscanshopv1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import adaptadores.AdaptadorHistorial;
import clases.Producto;
import clases.Venta;

/**
 * Muestra los datos almacenados en la tabla "ventas" de la BDD
 */
public class HistorialActivity extends AppCompatActivity {

    protected androidx.appcompat.widget.Toolbar toolbar;
    protected ListView lista;
    protected AdaptadorHistorial adaptador;
    protected ArrayList<Venta> listaVentas = new ArrayList<> (  );
    protected String urlMostrarVentas = "https://vaticinal-center.000webhostapp.com/mostrarVentas.php";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_historial );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_historial );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lista = (ListView ) findViewById ( R.id.lista_historial );
        listarVentas ();
        adaptador = new AdaptadorHistorial ( this, listaVentas );
        lista.setAdapter ( adaptador );

        lista.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int position , long l ) {

                Venta v = (Venta ) adapterView.getItemAtPosition ( position );
                AlertDialog.Builder builder= new AlertDialog.Builder ( HistorialActivity.this );
                builder.setTitle ( "Venta Nº " + v.getId ());
                builder.setMessage ( "Fecha de la operacion: "+ v.getFecha () +
                        "\nId del Vendedor: "+ v.getIdVendedor () +"\nNombre del cliente: "+ v.getNombreCliente () );
                builder.setPositiveButton ( "DESCARGAR TICKET" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {
                        Uri factura = Uri.parse ( v.getFactura () );
                        Intent intent = new Intent ( Intent.ACTION_VIEW, factura );
                        startActivity ( intent );

                    }
                } );

                builder.setNeutralButton ( "SALIR", null );
                AlertDialog dialog= builder.create();
                builder.show();
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
       startActivity ( new Intent ( HistorialActivity.this, MenuActivity.class ) );
       finish ();
    }

    /**
     * Conecta con la base de datos para hacer un SELECT de todos los registros de la tabla "ventas"
     */
    public void listarVentas(){

        StringRequest stringRequest = new StringRequest ( Request.Method.GET, urlMostrarVentas , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {
                listaVentas.clear ( );

                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );
                    JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );

                    if (exito.equals ( "1" )) {
                        for (int i = 0; i < jsonArray.length ( ); i++) {

                            JSONObject object = jsonArray.getJSONObject ( i );

                            String id = object.getString ( "id" );
                            String fecha = object.getString ( "fecha" );
                            String id_vendedor = object.getString ( "id_vendedor" );
                            String id_cliente = object.getString ( "id_cliente" );
                            String nombre_cliente = object.getString ( "nombre_cliente" );
                            String factura = object.getString ( "factura" );

                            Venta v = new Venta ( id , fecha , id_vendedor , id_cliente , nombre_cliente , factura );
                            listaVentas.add ( v );
                            adaptador.notifyDataSetChanged ( );

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( HistorialActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( HistorialActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
            }
        } );

        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }
}