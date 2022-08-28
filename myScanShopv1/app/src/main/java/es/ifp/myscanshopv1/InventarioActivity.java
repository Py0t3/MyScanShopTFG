package es.ifp.myscanshopv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adaptadores.AdaptadorInventario;
import clases.Producto;

public class InventarioActivity extends AppCompatActivity {


    protected Toolbar toolbar;
    private Intent pasarPantalla;
    protected ListView lista1;
    protected ImageButton botonAdd;
    protected AdaptadorInventario adaptador;
    protected String id="";
    protected static String url = "https://vaticinal-center.000webhostapp.com/mostrarProductos.php";
    public static ArrayList<Producto> productoArrayList = new ArrayList<> (  );


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_inventario );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_inventario );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lista1 = (ListView ) findViewById ( R.id.listViewProducto_inventario );
        botonAdd = (ImageButton ) findViewById ( R.id.boton_add_inventario );
        listarProductos ();
        adaptador = new AdaptadorInventario ( this, productoArrayList );
        lista1.setAdapter ( adaptador );



        botonAdd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( InventarioActivity.this, AddProductoInventario.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        lista1.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int position , long l ) {

                Producto  p= ( Producto ) adapterView.getItemAtPosition ( position );
                id = p.getIdentificador ();
                pasarPantalla = new Intent ( InventarioActivity.this, InfoProductoActivity.class );
                pasarPantalla.putExtra ( "id",id );
                startActivity ( pasarPantalla );
                finish ();


            }

        } );
    }
    public void listarProductos(){

        StringRequest stringRequest = new StringRequest ( Request.Method.GET, url , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {
                productoArrayList.clear ( );

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
                            productoArrayList.add ( p );
                            adaptador.notifyDataSetChanged ( );

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( InventarioActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( InventarioActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
            }
        } );

        /*
         Estas dos línes son importante. Sin RequestQueue no hace nada
         */
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
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
        pasarPantalla = new Intent (InventarioActivity.this, MenuActivity.class);
        startActivity(pasarPantalla);
        //overridePendingTransition ( R.anim.right_in, R.anim.right_out );
        finish();
    }




}