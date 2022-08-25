package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MenuActivity extends AppCompatActivity {

    private Intent pasarPantalla;
    protected Button botonPdV;
    protected Button botonInventario;
    protected Button botonClientes;
    protected Button botonUsuarios;
    protected Bundle paquete;
    protected String nombreUsuario;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_menu );

        botonPdV = (Button ) findViewById ( R.id.botonPdv_menuP );
        botonInventario = (Button ) findViewById ( R.id.botonInventario_menuP);
        botonClientes = (Button ) findViewById ( R.id.botonClientes_menuP );
        botonUsuarios = (Button ) findViewById ( R.id.botonUsuarios_menuP );

        paquete=getIntent().getExtras();
        if(paquete!=null) {
            nombreUsuario = paquete.getString ( "nombreUsuario" );
        }

       botonPdV.setOnClickListener ( new View.OnClickListener ( ) {
           @Override
           public void onClick ( View view ) {

               pasarPantalla = new Intent ( MenuActivity.this, MainActivity.class );
               pasarPantalla.putExtra ( "nombreUsuario", nombreUsuario );
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

        botonUsuarios.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                startActivity ( new Intent ( MenuActivity.this, UsuariosActivity.class ) );
                finish ();
            }
        } );

    }
}