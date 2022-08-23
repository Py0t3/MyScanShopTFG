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
import clases.DataBaseSQLite;
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
    protected DataBaseSQLite db;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_buscar );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbart_buscar );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db= new DataBaseSQLite ( this );
        label1 = (TextView )findViewById ( R.id.label1_buscar );
        caja1 = (EditText ) findViewById ( R.id.caja1_buscar );
        boton1 = (Button ) findViewById ( R.id.boton1_buscar );
        lista1 = (ListView ) findViewById ( R.id.lista1_buscar );

        listaProductos =new ArrayList<> (  );

        adaptador = new AdaptadorInventario ( this, listaProductos);

        boton1.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {


                busqueda = caja1.getText ().toString ();

                for(int i =0; i< db.getAllNotes ().size () ; i++)
                {
                    if(busqueda.equals ( db.getAllNotes ().get ( i ).getCodigoBarras () ))
                    {
                        Producto p = db.getProductoByCode ( busqueda );
                        listaProductos.add ( p );
                        lista1.setAdapter ( adaptador );
                        break;
                    }
                    else if(busqueda.equals ( db.getAllNotes ().get ( i ).getNombre() ))
                    {
                        Producto p = db.getNote ( "'"+busqueda+"'" );
                        listaProductos.add ( p );
                        lista1.setAdapter ( adaptador );
                        break;

                    }

                }
                if(listaProductos.size ()==0)
                {
                    Toast.makeText ( BuscarActivity.this , "Producto no encontrado" , Toast.LENGTH_SHORT ).show ( );
                }
            }
        } );

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