package es.ifp.myscanshopv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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

import adaptadores.AdaptadorUsers;
import clases.Usuario;

public class UsuariosActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    private Intent pasarPantalla;
    protected ListView lista1;
    protected FloatingActionButton botonAdd;
    protected ArrayList<Usuario> listaUsuarios = new ArrayList<> (  );
    protected AdaptadorUsers adaptador;
    protected String url = "http://192.168.1.38/web_service/mostrar.php";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_usuarios );

        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_users );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lista1 = (ListView ) findViewById ( R.id.lista_users );
        botonAdd = (FloatingActionButton ) findViewById ( R.id.BotonAdd_users );


        adaptador= new AdaptadorUsers ( this, listaUsuarios );
        lista1.setAdapter ( adaptador );
        listarUsers();

    }
    //Método para recibir con GET todas las entradas de la tabla. Lo importante es el uso de la clase StringRequest
    //JSONObject para decodificar los datos
    private void listarUsers(){

        StringRequest stringRequest = new StringRequest ( Request.Method.GET, url , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {
                listaUsuarios.clear ( );

                try {
                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );
                    JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );

                    if (exito.equals ( "1" )) {
                        for (int i = 0; i < jsonArray.length ( ); i++) {

                            JSONObject object = jsonArray.getJSONObject ( i );

                            String id = object.getString ( "id" );
                            String nombre = object.getString ( "nombre" );
                            String puesto = object.getString ( "puesto" );

                            Usuario u = new Usuario ( id , nombre ,puesto);
                            listaUsuarios.add ( u );
                            adaptador.notifyDataSetChanged ( );
                            Toast.makeText ( UsuariosActivity.this , u.getNombre () , Toast.LENGTH_SHORT ).show ( );
                        }



                    } else{
                        Toast.makeText ( UsuariosActivity.this , "Sin registros" , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( UsuariosActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
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
        startActivity ( new Intent ( UsuariosActivity.this, MenuActivity.class ) );
        finish ();
    }
}