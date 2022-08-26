package es.ifp.myscanshopv1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import adaptadores.AdapterGrid;
import clases.Producto;
import clases.Usuario;

public class MainActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView labelUser;
    protected Button botonScan;
    protected Button botonAdd;
    protected Button botonCaja;
    protected Button botonBuscar;
    protected SearchView searchBar;
    protected Intent pasarPantalla;
    protected Bundle paquete;
    protected float euros = 0.0f;
    protected String eurosPaquete="";
    protected String[] partes;
    protected String codigoBarras;
    protected String nombre;
    protected String nombreUsuario;
    protected String productoManual;
    protected String url = "https://vaticinal-center.000webhostapp.com/mostrarProductos.php";
    protected String urlBuscarProducto = "https://vaticinal-center.000webhostapp.com/buscarProducto.php";
    protected Producto p;
    public static ArrayList<Producto> productoArrayList = new ArrayList<> (  );
    protected AdapterGrid adapter;

    protected GridView grid;
    protected ArrayList<Producto> listaProductos;
    protected ArrayList<Producto> totalProductos;
    protected static ArrayList<String> cesta;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        labelUser = (TextView )findViewById ( R.id.labelUser);
        //botonScan = (Button )findViewById ( R.id.botonScan_main );
       // botonAdd = (Button )findViewById ( R.id.botonAddd_main );
        botonCaja = (Button )findViewById ( R.id.botonCaja_main );
        searchBar = (SearchView )findViewById ( R.id.searchBar_main );

       // botonBuscar = (Button )findViewById ( R.id.botonBuscar_main );

        grid = (GridView )findViewById ( R.id.gridList_main );


        paquete=getIntent().getExtras();
        if(paquete!=null) {
            nombreUsuario = paquete.getString ( "nombreUsuario" );
            labelUser.setText ( "Usuario: " + nombreUsuario );
        }
        adapter = new AdapterGrid ( this, productoArrayList );
        grid.setAdapter ( adapter );
        listarProductos ();



        //Selección de producto desde la lista
        grid.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int i , long l ) {

                Producto p= ( Producto ) adapterView.getItemAtPosition ( i );
                AlertDialog.Builder builder= new AlertDialog.Builder ( MainActivity.this );
                builder.setTitle ( p.getNombre ());
                builder.setMessage ( "P.V.P: " + p.getPrecio () + " EUROS\nDescripcion: \n" + p.getDescripcion () );
                builder.setPositiveButton ( "AÑADIR A LA CESTA" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {


                    }
                } );

                builder.setNeutralButton ( "Cancelar", null );
                AlertDialog dialog= builder.create();
                builder.show();


            }


        } );

       /*
       Acción de la barra de búsqueda SearchView. Llama al método buscarProducto. Se pone a la escucha
       y se activa el método onQueryTextChange para que ejecute cada vez que se cambia una letra del texto en la barra.
        */
        searchBar.setOnQueryTextListener ( new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit ( String s ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange ( String busqueda ) {

               buscarProducto (busqueda );
                return true;
            }
        } );


        /*botonBuscar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( MainActivity.this, BuscarActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );*/


        /*//Botón para añadir producto no registrado
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
        } );*/



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
                            adapter.notifyDataSetChanged ( );

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( MainActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( MainActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
            }
        } );

        /*
         Estas dos línes son importante. Sin RequestQueue no hace nada
         */
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

    private  void buscarProducto(String busqueda){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlBuscarProducto , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {
                productoArrayList.clear ( );

                try {
                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );
                        for (int i = 0; i < jsonArray.length ( ); i++) {

                            JSONObject object = jsonArray.getJSONObject ( i );

                            String id = object.getString ( "id" );
                            String url_imagen = object.getString ( "url_imagen" );
                            String nombre = object.getString ( "nombre" );
                            String precio = object.getString ( "precio" );
                            String codigo_barras = object.getString ( "codigo_barras" );
                            String descripcion = object.getString ( "descripcion" );

                            Producto p = new Producto ( id, url_imagen, nombre, precio, codigo_barras, descripcion );
                            productoArrayList.add ( p );
                            adapter.notifyDataSetChanged ( );
                        }


                    } else {
                        Toast.makeText ( MainActivity.this , "Sin registros" , Toast.LENGTH_SHORT ).show ( );
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );
                }

            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( MainActivity.this , error.getMessage ( ) , Toast.LENGTH_SHORT ).show ( );
            }
        } ) {
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "nombre" , busqueda );


                return params;
            }

        };
            /*
             Estas dos línes son importante. Sin RequestQueue no hace nada
             */
        RequestQueue requestQueue = Volley.newRequestQueue ( MainActivity.this );
        requestQueue.add ( stringRequest );

    }


}