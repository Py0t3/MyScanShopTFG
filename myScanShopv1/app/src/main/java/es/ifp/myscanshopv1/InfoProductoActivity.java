package es.ifp.myscanshopv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import clases.Producto;
/**
 * Muestra la información del producto seleccionado
 */
public class InfoProductoActivity extends AppCompatActivity {

    protected ImageView imagen;
    protected TextView labelNombre;
    protected TextView labelID;
    protected TextView labelPrecio;
    protected TextView labelBarCode;
    protected TextView labelDescripcion;
    protected Button botonVolver, botonEliminar, botonEditar;
    private Intent pasarPAntalla;
    private Bundle paquete;
    protected String id;
    protected String url = "https://vaticinal-center.000webhostapp.com/seleccionarProducto.php";
    protected  String urlEliminar = "https://vaticinal-center.000webhostapp.com/eliminarProducto.php";


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_info_producto );


        imagen = (ImageView) findViewById ( R.id.imagen_info );
        labelNombre = (TextView ) findViewById ( R.id.labelNombre_info );
        labelID = (TextView ) findViewById ( R.id.labelID_info );
        labelPrecio = (TextView ) findViewById ( R.id.labelPrecio_info );
        labelBarCode = (TextView ) findViewById ( R.id.labelBar_info );
        labelDescripcion = (TextView ) findViewById ( R.id.labelDescripcion_info );
        botonVolver = (Button ) findViewById ( R.id.botonVolverl_info );
        botonEditar = (Button ) findViewById ( R.id.botonEditar_info );
        botonEliminar = (Button ) findViewById ( R.id.botonEliminar_info );

        paquete = getIntent ().getExtras ();

        if(paquete != null)
        {
            id = paquete.getString ( "id" );

            seleccionarProducto ();


        }
        botonVolver.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPAntalla = new Intent ( InfoProductoActivity.this, InventarioActivity.class );
                startActivity ( pasarPAntalla );
                finish ();
            }
        } );

        botonEliminar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                 eliminarPriducto ();
            }
        } );

        botonEditar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                 pasarPAntalla = new Intent ( InfoProductoActivity.this, ActualizarProductoActivity.class );
                 pasarPAntalla.putExtra ( "id", id );
                 startActivity ( pasarPAntalla );
                 finish ();
            }
        } );

    }

    /**
     * Conecta con la BDD y devuelve la informacion del registro de la tabla "productos"
     */
    public  void seleccionarProducto(){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, url , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {


                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );
                    JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );

                    if (exito.equals ( "1" )) {
                        for (int i = 0; i < jsonArray.length ( ); i++) {

                            JSONObject object = jsonArray.getJSONObject ( i );

                            String id = object.getString ( "id" );
                            String url_imagen = object.getString ( "url_imagen" );
                            String nombre = object.getString ( "nombre" );
                            String precio = object.getString ( "precio" );
                            String codigo_barras = object.getString ( "codigo_barras" );
                            String descripcion = object.getString ( "descripcion" );

                            Producto p = new Producto ( id , url_imagen , nombre , precio , codigo_barras , descripcion );
                            Picasso.get().load(p.getUrlImagen ()).error ( R.drawable.logoxl ).into(imagen);
                            labelNombre.setText ( p.getNombre () );
                            labelID.setText ( "ID: "+p.getIdentificador () );
                            labelPrecio.setText ( "PVP: " + p.getPrecio ()+ " EUROS" );
                            labelBarCode.setText ( "BARCODE: " + p.getCodigoBarras () );
                            labelDescripcion.setText ( "Descripción:\n \n"+ p.getDescripcion () );


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    //Toast.makeText ( InfoProductoActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                //Toast.makeText ( InfoProductoActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
            }
        } ){
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "id" , id );


                return params;
            }
        };

        /*
         Estas dos línes son importante. Sin RequestQueue no hace nada
         */
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );


    }

    /**
     * Conecta con la BDD y realiza un DELETE del registro de la tabla "productos"
     */
    private void eliminarPriducto(){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, urlEliminar , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {


                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        Toast.makeText ( InfoProductoActivity.this , "Producto eliminado correctamente" , Toast.LENGTH_SHORT ).show ( );
                        startActivity ( new Intent ( InfoProductoActivity.this, InventarioActivity.class ) );
                        finish ();
                        }
                    else{

                        Toast.makeText ( InfoProductoActivity.this , "No se pudo eliminar el producto" , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( InfoProductoActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }

            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( InfoProductoActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
            }
        } ){
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "id" , id );

                return params;
            }
        };

        /*
         Estas dos línes son importante. Sin RequestQueue no hace nada
         */
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );

    }
}