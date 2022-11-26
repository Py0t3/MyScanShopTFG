package es.ifp.myscanshopv1;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Actividad para crear un nuevo registro en la tabla "productos"
 */
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

    protected String nombre = "";
    protected String precio = "";
    protected String urlImagen = "";
    protected String codigoBarras = "";
    protected String descripcion = "";
    protected String url = "https://vaticinal-center.000webhostapp.com/insertarProducto.php";
    protected String fotoProducto;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 2;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_producto_inventario );


        imagen = ( ImageView ) findViewById ( R.id.image_add );
        botonImagen = ( Button ) findViewById ( R.id.botonImagen_add );
        cajaNombre = ( EditText ) findViewById ( R.id.cajaNombre_add );
        cajaPrecio = ( EditText ) findViewById ( R.id.cajaPrecio_add );
        cajaCodigo = ( EditText ) findViewById ( R.id.cajaCodigo_add );
        cajaDescripcion = ( EditText ) findViewById ( R.id.cajaDescripcion_add );
        botonGuardar = ( Button ) findViewById ( R.id.botonGuardar_add );
        botonCancel = ( Button ) findViewById ( R.id.botonCancel_add );


        imagen.setImageResource ( R.drawable.logoxl );

        botonImagen.setOnClickListener ( new View.OnClickListener ( ) {
            /**
             * seleciona una imagen para el producto
             * @param view
             */
            @Override
            public void onClick ( View view ) {

                if (checkPermission ( )) {
                    // Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
                    dispatchTakePictureIntent ( );

                } else {
                    requestPermissions ( );

                }
            }
        } );
        /**
         * Lllama a la función insertarProducto()
         */
        botonGuardar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                nombre = cajaNombre.getText ( ).toString ( );
                precio = cajaPrecio.getText ( ).toString ( );
                codigoBarras = cajaCodigo.getText ( ).toString ( );
                descripcion = cajaDescripcion.getText ( ).toString ( );

                insertarProducto (fotoProducto, urlImagen, nombre, precio, codigoBarras, descripcion );

            }
        } );

        botonCancel.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                onBackPressed ( );
            }
        } );
    }

    /*
Método para pasar a la pantalla de la cámara y tomar una foto
(código copiado de la API de android studio)
 */
    private void dispatchTakePictureIntent () {
        Intent takePictureIntent = new Intent ( MediaStore.ACTION_IMAGE_CAPTURE );
        if (takePictureIntent.resolveActivity ( getPackageManager ( ) ) != null) {
            startActivityForResult ( takePictureIntent , REQUEST_IMAGE_CAPTURE );
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
            fotoProducto = convertirImgString ( imageBitmap );
            imagen.setImageBitmap ( imageBitmap );

        }
    }


    /*
    El método por defecto crea un BitMap para usar la imagen pero no crea ningun File. Con el siguiente método
    conseguimos guardar el archivo al menos de forma temporal.
     */

    public String convertirImgString(Bitmap bitmap){

        ByteArrayOutputStream array = new ByteArrayOutputStream (  );
        bitmap.compress ( Bitmap.CompressFormat.JPEG,100,array );
        byte[] imagenByte = array.toByteArray ();
        String imagenString = Base64.encodeToString ( imagenByte,Base64.DEFAULT );

        return  imagenString;
    }


    //Botón Volver Inferior
    @Override
    public void onBackPressed () {
        super.onBackPressed ( );
        pasarPantalla = new Intent ( AddProductoInventario.this, InventarioActivity.class );
        startActivity(pasarPantalla);
        finish();
    }

    /**
     * En APIs mas recientes de Android es obligatorio solicitar permiso para utilizar la camara
     * métodos para solicitar permiso. Comprueba si el permiso está concedido
     */
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return permission1 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Pide permiso para accder a la cámara
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 200);
    }

    /**
     * Abre una nueva actividad con la cámara trasera del dispositivo
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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

    /**
     * Conecta con la BDD y inserta un nuevo registro en la tabla "productos"
     * @param imagen
     * @param url_imagen
     * @param nombre
     * @param precio
     * @param codigo_barras
     * @param descripcion
     */
    public void insertarProducto(String imagen, String url_imagen, String nombre, String precio, String codigo_barras, String descripcion){

        StringRequest stringRequest = new StringRequest ( Request.Method.POST , url , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {

                try {

                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {
                        Toast.makeText ( AddProductoInventario.this , "Producto insertado correctamente" , Toast.LENGTH_SHORT ).show ( );
                        startActivity ( new Intent ( AddProductoInventario.this, InventarioActivity.class ) );
                        finish ();
                    }
                    else{

                        Toast.makeText ( AddProductoInventario.this , "No se pudo insertar el producto" , Toast.LENGTH_SHORT ).show ( );
                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( AddProductoInventario.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }

            }

        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

            }
        } ){
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "imagen" , imagen );
                params.put ( "url_imagen" , url_imagen );
                params.put ( "nombre" , nombre );
                params.put ( "precio" , precio );
                params.put ( "codigo_barras" , codigo_barras );
                params.put ( "descripcion" , descripcion );

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

}