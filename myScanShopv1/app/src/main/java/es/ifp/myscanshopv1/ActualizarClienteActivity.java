package es.ifp.myscanshopv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

import clases.Cliente;

public class ActualizarClienteActivity extends AppCompatActivity {

    protected EditText labelNombre, labelEmail, labelTlf, labelDireccion, labelDni;
    protected Button botonActualizar, botonCancelar, botonBorrar;
    protected Intent pasarPantalla;
    protected Bundle paquete;
    protected String urlActualizarCliente= "https://vaticinal-center.000webhostapp.com/actualizarCliente.php";
    protected String urlSeleccionarCliente= "https://vaticinal-center.000webhostapp.com/seleccionarCliente.php";
    protected String urlBorrarCliente= "https://vaticinal-center.000webhostapp.com/eliminarCliente.php";
    protected String id, nombre, email, telefono, direccion, dni, activityOrigen;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_actualizar_cliente );

        labelNombre = (EditText ) findViewById ( R.id.labelNombre_updateCliente );
        labelEmail = (EditText ) findViewById ( R.id.labelEmail_updateCliente );
        labelTlf = (EditText ) findViewById ( R.id.labelTlf_updateCliente );
        labelDireccion = (EditText ) findViewById ( R.id.labelDireccion_updateCliente );
        labelDni = (EditText ) findViewById ( R.id.labelDni_updateCliente );
        botonActualizar = (Button ) findViewById ( R.id.botonActualizar_updateCliente );
        botonCancelar = (Button ) findViewById ( R.id.botonCancelar_updateCliente );
        botonBorrar = (Button ) findViewById ( R.id.botonBorrar_updateCliente );

        paquete = getIntent ().getExtras ();
        if(paquete!=null){

            id = paquete.getString ( "id" );
            activityOrigen = paquete.getString ( "Activity" ) ;


            seleccionarCliente ();
        }


        botonCancelar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( ActualizarClienteActivity.this, SeleccionarClienteActivity.class );
                pasarPantalla.putExtra ( "Activity", activityOrigen );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );
        botonActualizar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                nombre = labelNombre.getText ().toString ();
                email = labelEmail.getText ().toString ();
                telefono = labelTlf.getText ().toString ();
                direccion = labelDireccion.getText ().toString ();
                dni = labelDni.getText ().toString ();

                actualizarCliente ( id, nombre, email,telefono,direccion,dni );


            }
        } );

        botonBorrar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                borrarCliente ();
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

                            String nombre = object.getString ( "nombre" );
                            String email = object.getString ( "email" );
                            String telefono = object.getString ( "telefono" );
                            String direccion = object.getString ( "direccion" );
                            String dni = object.getString ( "dni" );

                            Cliente c = new Cliente ( id , nombre , email , telefono , direccion , dni );

                            labelNombre.setText (c.getNombre () );
                            labelEmail.setText (  c.getEmail () );
                            labelTlf.setText ( c.getTlf () );
                            labelDireccion.setText ( c.getDireccion () );
                            labelDni.setText ( c.getDni () );




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
                Toast.makeText ( ActualizarClienteActivity.this , "Error al cargar datos" , Toast.LENGTH_SHORT ).show ( );

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
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

    public void actualizarCliente(String id, String nombre, String email, String tlf, String direccion, String dni){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlActualizarCliente , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {

                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        Toast.makeText ( ActualizarClienteActivity.this , "Datos actualizados correctamente" , Toast.LENGTH_SHORT ).show ( );
                        pasarPantalla = new Intent ( ActualizarClienteActivity.this, SeleccionarClienteActivity.class );
                        pasarPantalla.putExtra ( "Activity", activityOrigen );
                        startActivity ( pasarPantalla );
                        finish ();
                    }
                    else{

                        Toast.makeText ( ActualizarClienteActivity.this , "No se pudieron actualizar los datos " , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( ActualizarClienteActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
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

                params.put ( "nombre" , nombre );
                params.put ( "email" , email );
                params.put ( "telefono" , tlf );
                params.put ( "direccion" , direccion );
                params.put ( "dni" , dni );
                params.put ( "id" , id );


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

    public void borrarCliente(){
        StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlBorrarCliente , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {

                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        Toast.makeText ( ActualizarClienteActivity.this , "Cliente borrado correctamente" , Toast.LENGTH_SHORT ).show ( );
                        pasarPantalla = new Intent ( ActualizarClienteActivity.this, SeleccionarClienteActivity.class );
                        pasarPantalla.putExtra ( "Activity", activityOrigen );
                        startActivity ( pasarPantalla );
                        finish ();
                    }
                    else{

                        Toast.makeText ( ActualizarClienteActivity.this , "No se pudieron actualizar los datos" , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( ActualizarClienteActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
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
                params.put ( "id" , id );


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        pasarPantalla = new Intent (ActualizarClienteActivity.this, SeleccionarClienteActivity.class);
        pasarPantalla.putExtra ( "Activity", activityOrigen );
        startActivity(pasarPantalla);
        finish();
    }
}