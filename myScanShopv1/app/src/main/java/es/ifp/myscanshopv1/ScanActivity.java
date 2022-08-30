package es.ifp.myscanshopv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import clases.Producto;

public class ScanActivity extends AppCompatActivity {
    private static String codigoLeido="";
    protected Intent pasarPantalla;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_scan );

        //new IntentIntegrator (this).initiateScan ();
        IntentIntegrator integrador= new IntentIntegrator ( ScanActivity.this );
        integrador.initiateScan ();


    }
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {

        IntentResult result= IntentIntegrator.parseActivityResult ( requestCode, resultCode,data );
        codigoLeido= result.getContents ();

        if(result != null)
        {
            //si decidimos cancelar el escaneo o dar a un boton de volver
            if(result.getContents ()==null)
            {
                Toast.makeText ( this , "Lectura cancelada" , Toast.LENGTH_SHORT ).show ( );
                pasarPantalla = new Intent ( ScanActivity.this, MainActivity.class);
                startActivity(pasarPantalla);
                finish();


            }
            else
            {
                pasarPantalla = new Intent ( ScanActivity.this, MainActivity.class);
                pasarPantalla.putExtra ( "codigo", codigoLeido );
                startActivity(pasarPantalla);
                finish ();

            }
        }
        super.onActivityResult ( requestCode , resultCode , data );

    }


    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );

        pasarPantalla = new Intent ( ScanActivity.this, MainActivity.class);
        startActivity(pasarPantalla);
        finish();
    }
}