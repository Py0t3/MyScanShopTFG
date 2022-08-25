package es.ifp.myscanshopv1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import clases.Producto;
import clases.Usuario;

public class Login extends AppCompatActivity {

    protected EditText cajaUser, cajaPass;
    protected Button botonEnter;
    protected String nombre="";
    protected String pass="";
    protected String url = "https://vaticinal-center.000webhostapp.com/checklogin.php";
    protected Intent intent;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        View decorView = getWindow ( ).getDecorView ( );
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility ( uiOptions );

        cajaUser = ( EditText ) findViewById ( R.id.cajaUser_login );
        cajaPass = ( EditText ) findViewById ( R.id.cajaPass_login );
        botonEnter = ( Button ) findViewById ( R.id.botonEnter_login );

        recuperarPreferencias ();

        botonEnter.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                nombre = cajaUser.getText ( ).toString ( );
                pass = cajaPass.getText ( ).toString ( );

                StringRequest stringRequest = new StringRequest ( Request.Method.POST , url , new Response.Listener<String> ( ) {
                    @Override
                    public void onResponse ( String response ) {


                        try {
                            JSONObject jsonObject = new JSONObject ( response );
                            String exito = jsonObject.getString ( "exito" );

                            if (exito.equals ( "1" )) {
                                JSONArray jsonArray = jsonObject.getJSONArray ( ("datos") );
                                for (int i = 0; i < jsonArray.length ( ); i++) {

                                    JSONObject object = jsonArray.getJSONObject ( i );

                                    String id = object.getString ( "id" );
                                    String user = object.getString ( "user" );
                                    String pass = object.getString ( "password" );
                                    String nombre = object.getString ( "nombre" );
                                    String puesto = object.getString ( "puesto" );

                                    Usuario u = new Usuario ( id, user, pass, nombre, puesto );
                                    String nombreUsuario = u.getNombre ();
                                    intent = new Intent ( Login.this, MenuActivity.class );
                                    intent.putExtra("nombreUsuario",nombreUsuario);
                                    Toast.makeText ( Login.this , "Hola: " + nombreUsuario , Toast.LENGTH_SHORT ).show ( );

                                }

                                startActivity ( intent  );
                                finish ( );

                            } else {
                                Toast.makeText ( Login.this , "Sin registros" , Toast.LENGTH_SHORT ).show ( );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace ( );
                        }

                    }
                } , new Response.ErrorListener ( ) {
                    @Override
                    public void onErrorResponse ( VolleyError error ) {

                        Toast.makeText ( Login.this , error.getMessage ( ) , Toast.LENGTH_SHORT ).show ( );
                    }
                } ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams () throws AuthFailureError {

                        Map<String, String> params = new HashMap<> ( );

                        params.put ( "nombre" , nombre );
                        params.put ( "pass" , pass );

                        return params;
                    }

                };
        /*
         Estas dos línes son importante. Sin RequestQueue no hace nada
         */
                RequestQueue requestQueue = Volley.newRequestQueue ( Login.this );
                requestQueue.add ( stringRequest );


            }
        });
    }
    private void guardarPreferencias(){

        SharedPreferences preferences = getSharedPreferences ( "preferenciLogin", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= preferences.edit ();
        editor.putString ( "usuario", nombre );
        editor.putString ( "pass", pass );
        editor.putBoolean ( "sesion", true );
        editor.commit ();
    }
    private void recuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences ( "preferenciLogin", Context.MODE_PRIVATE );
        cajaUser.setText ( preferences.getString("usuario","") );
        cajaPass.setText ( preferences.getString("pass","") );

    }

}