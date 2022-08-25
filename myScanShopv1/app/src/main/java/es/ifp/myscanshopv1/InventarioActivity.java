package es.ifp.myscanshopv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import adaptadores.AdaptadorInventario;
import clases.Producto;

public class InventarioActivity extends AppCompatActivity {


    protected Toolbar toolbar;
    private Intent pasarPantalla;
    protected ListView lista1;
    protected ImageButton botonAdd;
    private ListAdapter adaptador;
    protected ArrayList<Producto> listaProductos;
    private String contenidoItem="";
    private String[]partes;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_inventario );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_inventario );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lista1 = (ListView ) findViewById ( R.id.listViewProducto_inventario );
        botonAdd = (ImageButton ) findViewById ( R.id.boton_add_inventario );


        botonAdd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( InventarioActivity.this, AddProductoInventario.class );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );


        adaptador= new AdaptadorInventario ( this , listaProductos);
        lista1.setAdapter ( adaptador );

        lista1.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView<?> adapterView , View view , int position , long l ) {

                Producto p= ( Producto ) adapterView.getItemAtPosition ( position );
                AlertDialog.Builder builder= new AlertDialog.Builder ( InventarioActivity.this );
                builder.setTitle ( "Producto seleccionado");
                builder.setPositiveButton ( "Ver" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {

                        pasarPantalla = new Intent ( InventarioActivity.this, InfoProductoActivity.class );
                        pasarPantalla.putExtra ( "NOMBRE", p.getNombre () );
                        startActivity ( pasarPantalla );
                        finish ();
                    }
                } );
                builder.setNegativeButton ( "Borrar" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialogInterface , int i ) {

                       pasarPantalla = new Intent (InventarioActivity.this, InventarioActivity.class);
                       startActivity(pasarPantalla);

                    }
                } );
                builder.setNeutralButton ( "Cancelar", null );
                AlertDialog dialog= builder.create();
                builder.show();


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
        pasarPantalla = new Intent (InventarioActivity.this, MenuActivity.class);
        startActivity(pasarPantalla);
        //overridePendingTransition ( R.anim.right_in, R.anim.right_out );
        finish();
    }



}