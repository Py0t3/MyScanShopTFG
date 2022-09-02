package es.ifp.myscanshopv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adaptadores.AdaptadorClientes;
import clases.Cliente;
import clases.Producto;

public class ClientesActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected ListView listView;
    protected FloatingActionButton botonAdd;
    protected String urlMostrarClientes = "https://vaticinal-center.000webhostapp.com/mostrarClientes.php";
    protected ArrayList<Cliente> listaClientes = new ArrayList<> (  );
    protected AdaptadorClientes adaptador;
    protected Intent pasarPantalla;
    protected String id;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_clientes );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_clientes );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView ) findViewById ( R.id.listview_clientes );
        botonAdd = (FloatingActionButton ) findViewById ( R.id.botonAdd_clientes );

        listarClientes();
        adaptador = new AdaptadorClientes ( this, listaClientes );
        listView.setAdapter ( adaptador );

        botonAdd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

            }
        } );

        botonAdd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( ClientesActivity.this, AddNuevoClienteActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int i , long l ) {

                Cliente c =(Cliente) adapterView.getItemAtPosition ( i );
                id = c.getId ();
                pasarPantalla = new Intent ( ClientesActivity.this, InfoClienteActivity.class );
                pasarPantalla.putExtra ( "id", id );
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

                    Toast.makeText ( ClientesActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( ClientesActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
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
        pasarPantalla = new Intent (ClientesActivity.this, MenuActivity.class);
        startActivity(pasarPantalla);
        //overridePendingTransition ( R.anim.right_in, R.anim.right_out );
        finish();
    }
}