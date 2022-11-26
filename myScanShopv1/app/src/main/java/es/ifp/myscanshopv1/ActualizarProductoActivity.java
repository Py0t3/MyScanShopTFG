package es.ifp.myscanshopv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Activity para actualizar datos de los productos
 */

public class ActualizarProductoActivity extends AppCompatActivity {

    protected ImageView imagen;
    protected Button botonImagen;
    protected EditText cajaNombre;
    protected EditText cajaPrecio;
    protected EditText cajaCodigo;
    protected EditText cajaDescripcion;
    protected Button botonGuardar;
    protected Button botonCancel;
    protected Intent pasarPantalla;
    protected Bundle paquete;
    protected String id;
    protected String nombre="";
    protected String precio="";
    protected String url_imagen="";
    protected String codigo_barras="";
    protected String descripcion="";
    protected String urlInfo = "https://vaticinal-center.000webhostapp.com/seleccionarProducto.php";
    protected String urlActualizar = "https://vaticinal-center.000webhostapp.com/actualizarProducto.php";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_actualizar_producto );

        imagen = (ImageView )findViewById ( R.id.image_actualizar );
        botonImagen =  (Button) findViewById ( R.id.botonImagen_actualizar );
        cajaNombre = (EditText ) findViewById ( R.id.cajaNombre_actualizar );
        cajaPrecio = (EditText ) findViewById ( R.id.cajaPrecio_actualizar );
        cajaCodigo = (EditText ) findViewById ( R.id.cajaCodigo_actualizar );
        cajaDescripcion = (EditText )findViewById ( R.id.cajaDescripcion_actualizar );
        botonGuardar = (Button ) findViewById ( R.id.botonGuardar_actualizar );
        botonCancel = (Button ) findViewById ( R.id.botonCancel_actualizar );

        imagen.setImageResource ( R.drawable.logoxl );
        paquete = getIntent ().getExtras ();

        if(paquete != null)
        {
            id = paquete.getString ( "id" );

            seleccionarProducto ();


        }

        botonCancel.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                startActivity ( new Intent ( ActualizarProductoActivity.this, InventarioActivity.class ) );
                finish ();
            }
        } );

        botonGuardar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                url_imagen ="https://vaticinal-center.000webhostapp.com/images/logoxl.jpg";
                nombre = cajaNombre.getText ().toString ();
                precio = cajaPrecio.getText ().toString ();
                codigo_barras = cajaCodigo.getText ().toString ();
                descripcion = cajaDescripcion.getText ().toString ();

                actualizarProducto (id, url_imagen, nombre,precio,codigo_barras,descripcion );


            }
        } );
    }

    /**
     * Conecta con la BBDD y devuleve los datos del producto
     */
    public  void seleccionarProducto(){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, urlInfo , new Response.Listener<String> ( ) {
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
                            cajaNombre.setText ( p.getNombre () );
                            cajaPrecio.setText (  p.getPrecio ());
                            cajaCodigo.setText (  p.getCodigoBarras () );
                            cajaDescripcion.setText ( p.getDescripcion () );


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( ActualizarProductoActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( ActualizarProductoActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
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
     * Conecta con la BBDD y realiza un UPDATE del registro de la tabla "productos"
     * @param id
     * @param url_imagen
     * @param nombre
     * @param precio
     * @param codigo_barras
     * @param descripcion
     */
    public void actualizarProducto(String id, String url_imagen, String nombre, String precio, String codigo_barras, String descripcion){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlActualizar , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {

                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        Toast.makeText ( ActualizarProductoActivity.this , "Producto editado correctamente" , Toast.LENGTH_SHORT ).show ( );
                        startActivity ( new Intent ( ActualizarProductoActivity.this, InventarioActivity.class ) );
                        finish ();
                    }
                    else{

                        Toast.makeText ( ActualizarProductoActivity.this , "No se pudo editar el producto" , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( ActualizarProductoActivity.this ,"Aqui" + e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }

            }

        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

            }
        } ){
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "url_imagen" , url_imagen );
                params.put ( "nombre" , nombre );
                params.put ( "precio" , precio );
                params.put ( "codigo_barras" , codigo_barras );
                params.put ( "descripcion" , descripcion );
                params.put ( "id" , id );


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

    /**
     * Vuelve a la actividad anterior
     */
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        pasarPantalla = new Intent (ActualizarProductoActivity.this, InfoProductoActivity.class);
        startActivity(pasarPantalla);
        finish();
    }

}