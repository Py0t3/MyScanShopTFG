package es.ifp.myscanshopv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import adaptadores.AdaptadorClientes;
import clases.Cliente;
import clases.Producto;

public class SeleccionarClienteActivity extends AppCompatActivity {

    protected ListView lista;
    protected androidx.appcompat.widget.Toolbar toolbar;
    protected SearchView searchView;
    protected Button botonSinId, botonNuevoCliente;
    protected AdaptadorClientes adaptador;
    protected ArrayList<Cliente>listaClientes = new ArrayList<> (  );
    protected String urlMostrarClientes = "https://vaticinal-center.000webhostapp.com/mostrarClientes.php";
    protected String urlBuscarCliente = "https://vaticinal-center.000webhostapp.com/buscarCliente.php";
    protected Intent pasarPantalla;
    protected Bundle paquete;
    protected String activityOrigen;
    public static Cliente c;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_seleccionar_cliente );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( androidx.appcompat.widget.Toolbar ) findViewById ( R.id.toolbar_selectCliente);
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lista = (ListView ) findViewById ( R.id.lista_selctCliente );
        searchView = (SearchView ) findViewById ( R.id.search_selectCliente );
        botonNuevoCliente = (Button) findViewById ( R.id.botonNuevoCliente_selecTCliente );
        botonSinId = (Button) findViewById ( R.id.botonSinId_selectCliente );


        adaptador = new AdaptadorClientes ( this, listaClientes );
        lista.setAdapter ( adaptador );
        listarClientes();

        paquete  = getIntent ().getExtras ();
        if(paquete!=null){

            activityOrigen = paquete.getString ( "Activity" );
        }

        //Acción de la barra de búsqueda
        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit ( String s ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange ( String busqueda ) {
                buscarCliente (busqueda );
                return true;
            }
        } );

        lista.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int i , long l ) {

                c =(Cliente) adapterView.getItemAtPosition ( i );
                AlertDialog.Builder builder= new AlertDialog.Builder ( SeleccionarClienteActivity.this );
                builder.setTitle ( c.getNombre ());
                builder.setMessage ( "Id de Cliente " + c.getId () + "\nNombre: " + c.getNombre ()
                        + "\nEmail: : " + c.getEmail () + "\nTeléfono: " + c.getTlf ()+ "\nDirección: : " + c.getDireccion ());
                builder.setPositiveButton ( "SELECCIONAR" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {

                        if(activityOrigen.equals ( "caja" )){
                            pasarPantalla = new Intent ( SeleccionarClienteActivity.this, CajaActivity.class );
                        }
                        else if(activityOrigen.equals ( "menu" )) {
                            pasarPantalla = new Intent ( SeleccionarClienteActivity.this, MenuActivity.class );

                        }
                        startActivity ( pasarPantalla );
                        finish ();

                    }
                } );
                builder.setNegativeButton ( "EDITAR" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {
                        pasarPantalla = new Intent ( SeleccionarClienteActivity.this, ActualizarClienteActivity.class );
                        pasarPantalla.putExtra ( "id", c.getId () );
                        pasarPantalla.putExtra ( "Activity", activityOrigen );
                        startActivity ( pasarPantalla );
                        finish ();
                    }
                } );

                builder.setNeutralButton ( "Cancelar", null );
                AlertDialog dialog= builder.create();
                builder.show();


            }
        } );



        botonSinId.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                c = new Cliente ( "0","Cliente Invitado","","","","" );
                if(activityOrigen.equals ( "caja" )){
                    pasarPantalla = new Intent ( SeleccionarClienteActivity.this, CajaActivity.class );
                }
                else if(activityOrigen.equals ( "menu" )) {
                    pasarPantalla = new Intent ( SeleccionarClienteActivity.this, MenuActivity.class );

                }
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        botonNuevoCliente.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                pasarPantalla = new Intent ( SeleccionarClienteActivity.this, AddNuevoClienteActivity.class );
                pasarPantalla.putExtra ( "Activity", activityOrigen );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );



    }
    public void listarClientes(){

        StringRequest stringRequest = new StringRequest ( Request.Method.GET, urlMostrarClientes , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {
                listaClientes.clear ( );

                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );
                    JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );

                    if (exito.equals ( "1" )) {
                        for (int i = 0; i < jsonArray.length ( ); i++) {

                            JSONObject object = jsonArray.getJSONObject ( i );

                            String id = object.getString ( "id" );
                            String nombre = object.getString ( "nombre" );
                            String email = object.getString ( "email" );
                            String telefono = object.getString ( "telefono" );
                            String direccion = object.getString ( "direccion" );
                            String dni = object.getString ( "dni" );

                            Cliente c = new Cliente ( id, nombre , email , telefono , direccion, dni);
                            listaClientes.add ( c );
                            adaptador.notifyDataSetChanged ( );

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( SeleccionarClienteActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( SeleccionarClienteActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
            }
        } );

        /*
         Estas dos línes son importante. Sin RequestQueue no hace nada
         */
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }
    private  void buscarCliente(String busqueda) {

        StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlBuscarCliente , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {
                listaClientes.clear ( );

                try {
                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );
                        for (int i = 0; i < jsonArray.length ( ); i++) {

                            JSONObject object = jsonArray.getJSONObject ( i );

                            String id = object.getString ( "id" );
                            String nombre = object.getString ( "nombre" );
                            String email = object.getString ( "email" );
                            String telefono = object.getString ( "telefono" );
                            String direccion = object.getString ( "direccion" );
                            String dni = object.getString ( "dni" );

                            Cliente c = new Cliente ( id , nombre , email , telefono , direccion , dni );
                            listaClientes.add ( c );
                            adaptador.notifyDataSetChanged ( );
                        }


                    } else {
                        Toast.makeText ( SeleccionarClienteActivity.this , "Sin registros" , Toast.LENGTH_SHORT ).show ( );
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );
                }

            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( SeleccionarClienteActivity.this , error.getMessage ( ) , Toast.LENGTH_SHORT ).show ( );
            }
        } ) {
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "nombreOdni" , busqueda );

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue ( SeleccionarClienteActivity.this );
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
        if(activityOrigen.equals ( "caja" )){
            pasarPantalla = new Intent ( SeleccionarClienteActivity.this, CajaActivity.class );
        }
        else if(activityOrigen.equals ( "menu" )) {
            pasarPantalla = new Intent ( SeleccionarClienteActivity.this, MenuActivity.class );

        }
        startActivity(pasarPantalla);
        finish();
    }
}