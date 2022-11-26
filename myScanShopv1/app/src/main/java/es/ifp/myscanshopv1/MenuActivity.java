package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Menu principal de la aplicacion
 */
public class MenuActivity extends AppCompatActivity {

    private Intent pasarPantalla;
    protected Button botonPdV;
    protected Button botonInventario;
    protected Button botonClientes;
    protected Button botonDatos, botonCerrarSesion, botonHistorial;
    protected TextView nombreUsuario;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_menu );

        botonPdV = (Button ) findViewById ( R.id.botonPdv_menuP );
        botonInventario = (Button ) findViewById ( R.id.botonInventario_menuP);
        botonClientes = (Button ) findViewById ( R.id.botonClientes_menuP );
        botonDatos = (Button ) findViewById ( R.id.botonDatos_menuP );
        botonHistorial = (Button ) findViewById ( R.id.botonHistorial_menu );
        botonCerrarSesion = (Button ) findViewById ( R.id.botonSesion_menu );
        nombreUsuario = (TextView ) findViewById ( R.id.nombreUSer_menu );


        nombreUsuario.setText ( "Usuario: " + Login.u.getNombre () );

        botonCerrarSesion.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Login.u = null;
                startActivity ( new Intent ( MenuActivity.this, Login.class ) );
                finish ();
            }
        } );


       botonPdV.setOnClickListener ( new View.OnClickListener ( ) {
           @Override
           public void onClick ( View view ) {

               pasarPantalla = new Intent ( MenuActivity.this, MainActivity.class );
               startActivity ( pasarPantalla );
               finish ();
           }
       } );
        botonInventario.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( MenuActivity.this, InventarioActivity.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

        botonDatos.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                startActivity ( new Intent ( MenuActivity.this, DatosFacturacionActivity.class ) );
                finish ();
            }
        } );

        botonClientes.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                pasarPantalla =  new Intent ( MenuActivity.this, SeleccionarClienteActivity.class  );
                pasarPantalla.putExtra ( "Activity", "menu" );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );
        botonHistorial.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                pasarPantalla =  new Intent ( MenuActivity.this, HistorialActivity.class  );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );

    }
    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );

    }
}