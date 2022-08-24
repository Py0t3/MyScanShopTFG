package es.ifp.myscanshopv1;

import android.content.Intent;
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

import clases.Usuario;

public class Login extends AppCompatActivity {

    protected EditText cajaUser, cajaPass;
    protected Button botonEnter;
    protected String url = "http://192.168.1.38/web_service/checklogin.php";

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

        botonEnter.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                String nombre = cajaUser.getText ( ).toString ( );
                String pass = cajaPass.getText ( ).toString ( );

                StringRequest stringRequest = new StringRequest ( Request.Method.POST , url , new Response.Listener<String> ( ) {
                    @Override
                    public void onResponse ( String response ) {

                        try {
                            JSONObject jsonObject = new JSONObject ( response );
                            String exito = jsonObject.getString ( "exito" );


                            if (exito.equals ( "1" )) {


                                startActivity ( new Intent ( Login.this , MenuActivity.class ) );
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
}