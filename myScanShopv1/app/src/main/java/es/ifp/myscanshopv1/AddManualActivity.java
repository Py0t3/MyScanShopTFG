package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import clases.Producto;

public class AddManualActivity extends AppCompatActivity {


    protected Toolbar toolbar;
    protected EditText cajaNombre;
    protected EditText cajaPrecio;
    protected EditText cajaDescripcion;
    protected TextView labelNombre;
    protected Button botonAdd;
    private Intent pasarPantalla;
    protected String nombre;
    protected String precio;
    protected String descripcion;
    protected Producto p;



    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_manual );
        toolbar = ( Toolbar ) findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cajaNombre = (EditText ) findViewById ( R.id.cajaNombre_actualizar );
        cajaPrecio = (EditText ) findViewById ( R.id.cajaPrecio_actualizar );
        cajaDescripcion = (EditText ) findViewById ( R.id.cajaDescripcion_actualizar );

        botonAdd  =(Button )findViewById ( R.id.botonGuardar_actualizar );

        botonAdd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                //Recoger información y almacenar el objeto clase Producto
                nombre = cajaNombre.getText ().toString ();
                precio = cajaPrecio.getText ().toString ();
                descripcion = cajaDescripcion.getText ().toString ();

                if(nombre.equals ( "" ) || precio.equals ( "" )){

                    Toast.makeText ( AddManualActivity.this , "Los campos 'NOMBRE' y 'PRECIO' son obligatorios" , Toast.LENGTH_SHORT ).show ( );
                }
                else{
                    p= new Producto (nombre, precio, descripcion );
                    MainActivity.cajaArrayList.add ( p );
                    pasarPantalla = new Intent ( AddManualActivity.this, MainActivity.class );
                    startActivity ( pasarPantalla );
                    finish ();
                }
            }
        } );

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
        pasarPantalla = new Intent (AddManualActivity.this, MainActivity.class);
        startActivity(pasarPantalla);
        finish();
    }
}

