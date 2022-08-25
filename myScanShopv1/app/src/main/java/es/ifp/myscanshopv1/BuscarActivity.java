package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import adaptadores.AdaptadorInventario;
import clases.Producto;

public class BuscarActivity extends AppCompatActivity {

    private Intent pasarPantalla;
    protected Toolbar toolbar;
    protected TextView label1;
    protected EditText caja1;
    protected Button boton1;
    protected ListView lista1;
    public TextView vistaResultados;
    protected ArrayList<Producto> listaProductos;
    private ListAdapter adaptador;
    protected String busqueda = "";
    protected Producto p;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_buscar );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbart_buscar );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        label1 = (TextView )findViewById ( R.id.label1_buscar );
        caja1 = (EditText ) findViewById ( R.id.caja1_buscar );
        boton1 = (Button ) findViewById ( R.id.boton1_buscar );
        lista1 = (ListView ) findViewById ( R.id.lista1_buscar );

        listaProductos =new ArrayList<> (  );

        adaptador = new AdaptadorInventario ( this, listaProductos);



        lista1.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int i , long l ) {

                Producto p= ( Producto ) adapterView.getItemAtPosition ( i );
                pasarPantalla = new Intent ( BuscarActivity.this, MainActivity.class );
                pasarPantalla.putExtra ( "NOMBRE", p.getNombre () );
                startActivity ( pasarPantalla );
                finish ();

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
        pasarPantalla = new Intent (BuscarActivity.this, MainActivity.class);
        startActivity(pasarPantalla);
        finish();
    }


}