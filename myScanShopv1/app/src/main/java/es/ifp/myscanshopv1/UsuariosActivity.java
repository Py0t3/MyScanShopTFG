package es.ifp.myscanshopv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsuariosActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    private Intent pasarPantalla;
    protected ListView lista1;
    protected FloatingActionButton botonAdd;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_usuarios );

        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_users );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lista1 = (ListView ) findViewById ( R.id.lista_users );
        botonAdd = (FloatingActionButton ) findViewById ( R.id.BotonAdd_users );


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
        startActivity ( new Intent ( UsuariosActivity.this, MenuActivity.class ) );
        finish ();
    }
}