package es.ifp.myscanshopv1;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddProductoInventario extends AppCompatActivity {

    protected ImageView imagen;
    protected Button botonImagen;
    protected EditText cajaNombre;
    protected EditText cajaPrecio;
    protected EditText cajaCodigo;
    protected EditText cajaDescripcion;
    protected Button botonGuardar;
    protected Button botonCancel;
    protected Intent pasarPantalla;

    protected String nombre="";
    protected float precio=0.0f;
    protected String urlImagen="";
    protected String codigoBarras="";
    protected String descripcion="";
    protected Uri ruta;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE= 2;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_producto_inventario );


        imagen = (ImageView )findViewById ( R.id.image_addInventario );
        botonImagen =  (Button) findViewById ( R.id.botonImagen_addInventario );
        cajaNombre = (EditText ) findViewById ( R.id.cajaNombre_add );
        cajaPrecio = (EditText ) findViewById ( R.id.cajaPrecio_add );
        cajaCodigo = (EditText ) findViewById ( R.id.cajaCodigo_addInventario );
        cajaDescripcion = (EditText )findViewById ( R.id.cajaDescripcion_add );
        botonGuardar = (Button ) findViewById ( R.id.botonGuardar_add );
        botonCancel = (Button ) findViewById ( R.id.botonCancel_addInventario);



        imagen.setImageResource ( R.drawable.logoxl );

        botonImagen.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                if (checkPermission ( )) {
                    // Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
                    dispatchTakePictureIntent ();

                } else {
                    requestPermissions ( );

                }
            }
        } );

        botonGuardar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                nombre = cajaNombre.getText ().toString ();
                precio = Float.parseFloat ( cajaPrecio.getText ().toString ());
                urlImagen = "https://loremflickr.com/320/240/dog";
                codigoBarras =  cajaCodigo.getText ().toString () ;
                descripcion = cajaDescripcion.getText ().toString ();


            }
        } );

        botonCancel.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                onBackPressed ();
            }
        } );
    }
    /*
Método para pasar a la pantalla de la cámara y tomar una foto
(código copiado de la API de android studio)
 */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    /*En la opcion if coloca miniatura de la imagen captada (baja resolución). Hace "algo" con la foto tomada, en este
caso la coloca como Bitmap
En la opcion else if hacemos que coincida el entero con la variable estatica RESULT LOAD IMAGE
por lo que lo que cargará será una imagen de la galeria llamada con el método getImageFromGalllery
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode , resultCode , data );

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras ( );
            Bitmap imageBitmap = ( Bitmap ) extras.get ( "data" );
            imagen.setImageBitmap ( imageBitmap );
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            ruta= data.getData ();
            imagen.setImageURI ( ruta );
        }
    }


    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        pasarPantalla = new Intent ( AddProductoInventario.this, InventarioActivity.class );
        startActivity(pasarPantalla);
        finish();
    }

    //En APIs mas recientes de Android es obligatorio solicitar permiso para utilizar la camara
    //Métodos para solicitar permiso
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return permission1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200) {
            boolean usarCamara = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            //Si el permiso es concedido, se llama al método para capturar imagen
            try {
                dispatchTakePictureIntent ();
            }
            catch (Exception e){
                pasarPantalla = new Intent ( AddProductoInventario.this, AddProductoInventario.class );
            }

        }
        else{
            pasarPantalla = new Intent ( AddProductoInventario.this, AddProductoInventario.class );
        }
    }
}