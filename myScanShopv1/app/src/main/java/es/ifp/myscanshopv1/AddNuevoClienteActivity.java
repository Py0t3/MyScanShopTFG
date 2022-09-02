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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNuevoClienteActivity extends AppCompatActivity {

    protected EditText nombreET, emailET, telefonoET, direccionET, dniET;
    protected Button botonGuardar, botonCancelar;
    protected Intent pasarPantalla;
    protected String urlInsertarCliente = "https://vaticinal-center.000webhostapp.com/insertarCliente.php";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_nuevo_cliente );

        nombreET = (EditText ) findViewById ( R.id.nombre_addCliente );
        emailET = (EditText ) findViewById ( R.id.email_addClient );
        telefonoET = (EditText ) findViewById ( R.id.telefono_addCLiente );
        direccionET = (EditText ) findViewById ( R.id.direccion_addCliente );
        dniET = (EditText ) findViewById ( R.id.dni_addCliente );
        botonGuardar = (Button ) findViewById ( R.id.botonAdd_addCliente );
        botonCancelar = (Button ) findViewById ( R.id.botonCancel_addCliente );

        botonCancelar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( AddNuevoClienteActivity.this, ClientesActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        botonGuardar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                String nombre = nombreET.getText ().toString ();
                String email = emailET.getText ().toString ();
                String telefono = telefonoET.getText ().toString ();
                String direccion = direccionET.getText ().toString ();
                String dni = dniET.getText ().toString ();

                StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlInsertarCliente , new Response.Listener<String> ( ) {
                    @Override
                    public void onResponse ( String response ) {

                        try {

                            JSONObject jsonObject = new JSONObject ( response );
                            String exito = jsonObject.getString ( "exito" );

                            if (exito.equals ( "1" )) {
                                Toast.makeText ( AddNuevoClienteActivity.this , "Cliente insertado correctamente" , Toast.LENGTH_SHORT ).show ( );
                                startActivity ( new Intent ( AddNuevoClienteActivity.this, ClientesActivity.class ) );
                                finish ();
                            }
                            else{

                                Toast.makeText ( AddNuevoClienteActivity.this , "No se pudo guardar el cliente" , Toast.LENGTH_SHORT ).show ( );
                            }


                        } catch (JSONException e) {
                            e.printStackTrace ( );

                            Toast.makeText ( AddNuevoClienteActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
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
                        params.put ( "telefono" , telefono );
                        params.put ( "direccion" , direccion );
                        params.put ( "dni" , dni );

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue ( AddNuevoClienteActivity.this );
                requestQueue.add ( stringRequest );
            }
        } );
    }
}