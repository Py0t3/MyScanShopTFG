package es.ifp.myscanshopv1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adaptadores.AdapterGrid;
import clases.Producto;
import clases.Usuario;

/**
 * Actividad principal de la aplicacion
 */
public class MainActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView labelUser;
    protected FloatingActionButton botonScan;
    protected Button botonAdd;
    protected Button botonCaja;
    protected SearchView searchBar;
    protected Intent pasarPantalla;
    protected String nombre;
    protected String nombreUsuario;
    protected static String url = "https://vaticinal-center.000webhostapp.com/mostrarProductos.php";
    protected String urlBuscarProducto = "https://vaticinal-center.000webhostapp.com/buscarProducto.php";
    protected  String urlCodigoBarras = "https://vaticinal-center.000webhostapp.com/codigoBarras.php";
    public static ArrayList<Producto> productoArrayList = new ArrayList<> (  );
    public static ArrayList<Producto> cajaArrayList = new ArrayList<> (  );
    protected AdapterGrid adapter;
    private Bundle paquete;
    protected static boolean respuestaCodigo= true;

    protected GridView grid;
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
        botonScan = ( FloatingActionButton ) findViewById ( R.id.botonScan_main );
        botonAdd = (Button )findViewById ( R.id.botonAddd_main );
        botonCaja = (Button )findViewById ( R.id.botonCaja_main );
        searchBar = (SearchView )findViewById ( R.id.searchBar_main );
        grid = (GridView )findViewById ( R.id.gridList_main );

        paquete = getIntent ().getExtras ();
        if(paquete!=null){

            leerCodigo ( paquete.getString ( "codigo" ) );
        }

        botonCaja.setText ( "TOTAL: " + CajaActivity.calcularTotal () + " EUROS" );


        nombreUsuario = Login.u.getNombre ();
        labelUser.setText ( "Usuario: " + nombreUsuario );

        adapter = new AdapterGrid ( this, productoArrayList );
        grid.setAdapter ( adapter );
        listarProductos ();






        //Selección de producto desde la lista
        grid.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            /**
             * Creacion de objeto producto y consulta de sus datos. Abre un cuadro de dialogo
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int i , long l ) {

                //se obtiene el objeto
                Producto p= ( Producto ) adapterView.getItemAtPosition ( i );
                AlertDialog.Builder builder= new AlertDialog.Builder ( MainActivity.this );
                builder.setTitle ( p.getNombre ());
                builder.setMessage ( "P.V.P: " + p.getPrecio () + " EUROS\nDescripcion: \n" + p.getDescripcion () );
                builder.setPositiveButton ( "AÑADIR A LA CESTA" , new DialogInterface.OnClickListener ( ) {
                    /**
                     * anyade producto a arraylist de caja
                     * @param dialogInterface
                     * @param i
                     */
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {

                        //se añade a la lista de caja
                        cajaArrayList.add ( p );
                        botonCaja.setText ( "TOTAL: " + CajaActivity.calcularTotal () + " EUROS" );
                    }
                } );

                builder.setNeutralButton ( "Cancelar", null );
                AlertDialog dialog= builder.create();
                builder.show();
            }


        } );

       /**
       *Acción de la barra de búsqueda SearchView. Llama al método buscarProducto. Se pone a la escucha
       *y se activa el método onQueryTextChange para que ejecute cada vez que se cambia una letra del texto en la barra.
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
                startActivity ( pasarPantalla );
                finish ();
            }
        } );



        //Botón Ir a Caja Total
        botonCaja.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( MainActivity.this, CajaActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );


    }
    //Botón Volver Superior
    @Override
    public boolean onSupportNavigateUp() {
        dialogoAlertaSalir ();
        return true;
    }

    /**
     * Llama a la funcion dialogoAlertaSalir()
     */
    @Override
    public void onBackPressed () {
        dialogoAlertaSalir ();
    }

    /**
     * Muestra cuadro dialogo para confirmar la vuelta a la actividad anterior. Vacia el arraylist de caja
     */
    public void dialogoAlertaSalir(){

        AlertDialog.Builder builder= new AlertDialog.Builder ( MainActivity.this );
        builder.setTitle ( "Salir del proceso de venta");
        builder.setMessage ( "¿Seguro que desea salir del proceso de venta? Se borrará la cesta actual" );
        builder.setPositiveButton ( "SALIR" , new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialogInterface , int i ) {
                cajaArrayList.clear ();
                pasarPantalla = new Intent (MainActivity.this, MenuActivity.class);
                startActivity(pasarPantalla);
                finish();
            }
        } );

        builder.setNeutralButton ( "Cancelar", null );
        AlertDialog dialog= builder.create();
        builder.show();
    }

    /**
     * Muestra todos los productos registrados
     */
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

    /**
     * Realiza una conexion con la BDD y cosnsulta datos del producto
     * @param busqueda
     */
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
             Estas dos línes son importantes. Sin RequestQueue no hace nada
             */
        RequestQueue requestQueue = Volley.newRequestQueue ( MainActivity.this );
        requestQueue.add ( stringRequest );

    }

    /**
     * REaliza consulta a la base de datos sobre el codigo de barras leido
     * @param codigo
     */
    public  void leerCodigo(String codigo){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, urlCodigoBarras , new Response.Listener<String> ( ) {

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


                            Producto p = new Producto ( id,url_imagen,nombre,precio,codigo_barras,descripcion);
                            AlertDialog.Builder builder= new AlertDialog.Builder ( MainActivity.this );
                            builder.setTitle ( p.getNombre ());
                            builder.setMessage ( "P.V.P: " + p.getPrecio () + " EUROS\nDescripción: \n" + p.getDescripcion () );
                            builder.setPositiveButton ( "AÑADIR A LA CESTA" , new DialogInterface.OnClickListener ( ) {
                                @Override
                                public void onClick ( DialogInterface dialogInterface , int i ) {


                                    MainActivity.cajaArrayList.add ( p );
                                    startActivity ( new Intent ( MainActivity.this, MainActivity.class ) );
                                    finish ();
                                }
                            } );

                            builder.setNeutralButton ( "Cancelar", null );
                            AlertDialog dialog= builder.create();
                            builder.show();

                        }


                    } else{
                        Toast.makeText ( MainActivity.this , "Producto no registrado" , Toast.LENGTH_SHORT ).show ( );
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
        } ){
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "codigo_barras" , codigo );


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );


    }

}