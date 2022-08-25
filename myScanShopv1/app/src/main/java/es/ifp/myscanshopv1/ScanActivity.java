package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {
    private String codigoLeido="";
    protected Bundle paquete;
    protected Intent pasarPantalla;

    protected static ArrayList<String> cesta;
    protected String eurosPaquete="";


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_scan );


        cesta = MainActivity.cesta;
        new IntentIntegrator (this).initiateScan ();
        paquete = getIntent ().getExtras ();
        eurosPaquete= paquete.getString ( "TOTALEUROS" );
        Toast.makeText ( this , eurosPaquete , Toast.LENGTH_SHORT ).show ( );
    }
    @Override
    protected void onActivityResult ( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult ( requestCode , resultCode , data );
        IntentResult result = IntentIntegrator.parseActivityResult ( requestCode, resultCode, data );

        codigoLeido= result.getContents ();

        if(result != null)
        {
            //si decidimos cancelar el escaneo o dar a un boton de volver
            if(result.getContents ()==null)
            {
                Toast.makeText ( this , "Lectura cancelada" , Toast.LENGTH_SHORT ).show ( );
                pasarPantalla = new Intent ( ScanActivity.this, es.ifp.myscanshopv1.MainActivity.class );
                startActivity ( pasarPantalla );
            }
            else
            {




            }
        }
        super.onActivityResult ( requestCode , resultCode , data );

    }
}