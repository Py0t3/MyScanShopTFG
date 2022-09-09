package es.ifp.myscanshopv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellSignalStrength;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RegistroActivity extends AppCompatActivity {

    protected EditText cajaNombre, cajaUser, cajaPassword, confirmaPass, cajaPuesto, cajaEmail;
    protected String nombre, user, password, passwordDos, puesto, email;
    protected Button botonEnviar, botonCancelar;
    protected Intent pasarPantalla;


    protected String urlInsertarUsuario = "https://vaticinal-center.000webhostapp.com/insertarUsuario.php";


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_registro );

        cajaNombre = (EditText ) findViewById ( R.id.cajaNombre_registro );
        cajaUser = (EditText ) findViewById ( R.id.cajaUsuario_registro );
        cajaPassword = (EditText ) findViewById ( R.id.cajaPass_registro );
        confirmaPass = (EditText ) findViewById ( R.id.confirmPass_registro );
        cajaPuesto = (EditText ) findViewById ( R.id.cajaPuesto_registro );
        cajaEmail = (EditText ) findViewById ( R.id.email_registro );
        botonEnviar = (Button ) findViewById ( R.id.botonRegistrar_registro );
        botonCancelar = (Button ) findViewById ( R.id.botonCancelar_registro );

        botonCancelar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                startActivity ( new Intent ( RegistroActivity.this, Login.class ) );
                finish ();
            }
        } );

        botonEnviar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                nombre = cajaNombre.getText ( ).toString ( );
                user = cajaUser.getText ( ).toString ( );
                password = cajaPassword.getText ( ).toString ( );
                passwordDos = confirmaPass.getText ( ).toString ( );
                puesto = cajaPuesto.getText ( ).toString ( );
                email = cajaEmail.getText ( ).toString ( );

                if (password.equals ( passwordDos )) {
                    StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlInsertarUsuario , new Response.Listener<String> ( ) {
                        @Override
                        public void onResponse ( String response ) {

                            try {

                                JSONObject jsonObject = new JSONObject ( response );
                                String exito = jsonObject.getString ( "exito" );

                                if (exito.equals ( "1" )) {
                                    Toast.makeText ( RegistroActivity.this , "Usuario insertado correctamente" , Toast.LENGTH_SHORT ).show ( );
                                    pasarPantalla = new Intent ( RegistroActivity.this , Login.class );
                                    startActivity ( pasarPantalla );
                                    finish ( );
                                } else {

                                    Toast.makeText ( RegistroActivity.this , "No se pudo guardar el cliente" , Toast.LENGTH_SHORT ).show ( );
                                }


                            } catch (JSONException e) {
                                e.printStackTrace ( );

                                Toast.makeText ( RegistroActivity.this , e.getMessage ( ).toString ( ) , Toast.LENGTH_LONG ).show ( );
                            }

                        }

                    } , new Response.ErrorListener ( ) {
                        @Override
                        public void onErrorResponse ( VolleyError error ) {

                        }
                    } ) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams () throws AuthFailureError {

                            Map<String, String> params = new HashMap<> ( );

                            params.put ( "user" , user );
                            params.put ( "password" , password );
                            params.put ( "nombre" , nombre );
                            params.put ( "puesto" , puesto );
                            params.put ( "email" , email );

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue ( RegistroActivity.this );
                    requestQueue.add ( stringRequest );
                }
                else {
                    Toast.makeText ( RegistroActivity.this , "Los campos de password no coinciden" , Toast.LENGTH_SHORT ).show ( );
                }
            }

        } );

    }
    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        pasarPantalla = new Intent (RegistroActivity.this, Login.class);
        startActivity(pasarPantalla);
        finish();
    }
}