package es.ifp.myscanshopv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import clases.Producto;

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

        Toast.makeText ( this , eurosPaquete , Toast.LENGTH_SHORT ).show ( );
    }
    @Override
    protected void onActivityResult ( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult ( requestCode , resultCode , data );
        IntentResult result = IntentIntegrator.parseActivityResult ( requestCode, resultCode, data );

        codigoLeido= result.getContents ();

        if(result != null)
        {
            Producto p = new Producto ( "","" ,"Producto escaneado","35.4","","afsjaflfsldfjslfsdfjl");
            AlertDialog.Builder builder= new AlertDialog.Builder ( ScanActivity.this );
            builder.setTitle ( p.getNombre ());
            builder.setMessage ( "P.V.P: " + p.getPrecio () + " EUROS\nDescripcion: \n" + p.getDescripcion () );
            builder.setPositiveButton ( "AÑADIR A LA CESTA" , new DialogInterface.OnClickListener ( ) {
                @Override
                public void onClick ( DialogInterface dialogInterface , int i ) {

                    MainActivity.cajaArrayList.add ( p );
                    pasarPantalla = new Intent ( ScanActivity.this, ScanActivity.class );
                    startActivity ( pasarPantalla );


                }
            } );

            builder.setNeutralButton ( "Cancelar", null );
            AlertDialog dialog= builder.create();
            builder.show();

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
    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );

        pasarPantalla = new Intent ( ScanActivity.this, MainActivity.class);
        startActivity(pasarPantalla);
        finish();
    }
}