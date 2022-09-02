package es.ifp.myscanshopv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import clases.Cliente;
import clases.Producto;

public class InfoClienteActivity extends AppCompatActivity {

    protected TextView labelId, labelNombre, labelEmail, labelTelefono, labelDireccion, labelDni;
    protected Button botonVolver, botonEditar, botonEliminar;
    protected Intent pasarPantalla;
    protected Bundle paquete;
    protected String urlEliminarCliente = "https://vaticinal-center.000webhostapp.com/eliminarCliente.php";
    protected String urlSeleccionarCliente = "https://vaticinal-center.000webhostapp.com/seleccionarCliente.php";
    protected String id;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_info_cliente );

        labelId = (TextView ) findViewById ( R.id.id_datosCliente );
        labelNombre = (TextView ) findViewById ( R.id.nombre_datosCliente );
        labelEmail = (TextView ) findViewById ( R.id.email_datosCliente );
        labelTelefono = (TextView ) findViewById ( R.id.telefono_datosCliente );
        labelDireccion = (TextView ) findViewById ( R.id.direccion_datosCliente );
        labelDni = (TextView ) findViewById ( R.id.dni_datosCliente );
        botonVolver = (Button ) findViewById ( R.id.botonVolver_datosCliente );
        botonEditar = (Button ) findViewById ( R.id.botonEditar_datosCliente );
        botonEliminar = (Button ) findViewById ( R.id.botonEliminar_datosCliente );

        paquete = getIntent ().getExtras ();

        if(paquete != null)
        {
            id = paquete.getString ( "id" );

            seleccionarCliente ();

        }

        botonVolver.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( InfoClienteActivity.this, ClientesActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        botonEditar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( InfoClienteActivity.this, ActualizarClienteActivity.class );
                pasarPantalla.putExtra ( "id", id );
                startActivity ( pasarPantalla );
                finish ();

            }
        } );

        botonEliminar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                eliminarCliente ();
            }
        } );
    }
    public  void seleccionarCliente(){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, urlSeleccionarCliente , new Response.Listener<String> ( ) {
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
                            String nombre = object.getString ( "nombre" );
                            String email = object.getString ( "email" );
                            String telefono = object.getString ( "telefono" );
                            String direccion = object.getString ( "direccion" );
                            String dni = object.getString ( "dni" );

                            Cliente c = new Cliente ( id , nombre , email , telefono , direccion , dni );
                            labelId.setText ( "Id de Cliente: " + c.getId () );
                            labelNombre.setText ("Nombre: " + c.getNombre () );
                            labelEmail.setText ( "Email: " + c.getEmail () );
                            labelTelefono.setText ( "Tlf: " + c.getTlf () );
                            labelDireccion.setText ( "Dirección: "+ c.getDireccion () );
                            labelDni.setText ( "Dni: " + c.getDni () );


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                    //Toast.makeText ( InfoClienteActivity.this , "Error al cargar datos" , Toast.LENGTH_SHORT ).show ( );
                   // Toast.makeText ( InfoClienteActivity.this , e.getMessage () , Toast.LENGTH_SHORT ).show ( );
                }


            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {
                Toast.makeText ( InfoClienteActivity.this , "Error al cargar datos" , Toast.LENGTH_SHORT ).show ( );

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
    private void eliminarCliente(){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST, urlEliminarCliente , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {


                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        Toast.makeText ( InfoClienteActivity.this , "Cliente borrado correctamente" , Toast.LENGTH_SHORT ).show ( );
                        startActivity ( new Intent ( InfoClienteActivity.this, ClientesActivity.class ) );
                        finish ();
                    }
                    else{

                        Toast.makeText ( InfoClienteActivity.this , "No se pudo borrar el cliente" , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( InfoClienteActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }

            }
        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

                Toast.makeText ( InfoClienteActivity.this , error.getMessage (), Toast.LENGTH_SHORT ).show ( );
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