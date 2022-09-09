package es.ifp.myscanshopv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import adaptadores.AdaptadorCaja;
import clases.Cliente;
import clases.Producto;

public class CajaActivity extends AppCompatActivity {

    private Intent pasarPantalla;
    protected Toolbar toolbar;
    protected TextView totalFactura;
    protected Button botonFactura, botonCliente;
    protected ListView lista1;
    private ListAdapter adaptador;
    protected static float totalEuros = 0.0f;
    private Bundle paquete;
    protected Producto p;
    protected String numFactura= "";
    protected int numEnteroFactura = 0;
    public static String facturaNombre ="";
    protected String euros="";
    protected String urlRegistrarFactura="https://vaticinal-center.000webhostapp.com/registrarVenta.php";
    protected String urlobtenerId="https://vaticinal-center.000webhostapp.com/obtenerIdVenta.php";

    protected ProgressBar progressBar;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_caja );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_caja );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        botonFactura = (Button ) findViewById ( R.id.botonFactura_caja );
        botonCliente = (Button ) findViewById ( R.id.botonIdCliente_caja );
        lista1 = (ListView ) findViewById ( R.id.lista_caja );
        totalFactura = (TextView )findViewById ( R.id.totalFactura_caja ) ;
        progressBar = (ProgressBar )findViewById ( R.id.progressBar_caja ) ;

        recuperarDatosFacturacion ();
        obtenerIdVenta ();

        botonFactura.setEnabled ( false );


        euros =Float.toString ( calcularTotal ());
        totalFactura.setText ( Float.toString ( calcularTotal ()));

        adaptador= new AdaptadorCaja ( this, MainActivity.cajaArrayList);
        lista1.setAdapter ( adaptador );

        paquete = getIntent ().getExtras ();
        if(SeleccionarClienteActivity.c!=null){

            botonCliente.setText ( SeleccionarClienteActivity.c.getNombre () );
            if(!MainActivity.cajaArrayList.isEmpty ())
            {
                botonFactura.setEnabled ( true );
            }

        }



        /*
        Se requiere identificar al cliente para registrar la venta
         */
        botonCliente.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                pasarPantalla = new Intent ( CajaActivity.this, SeleccionarClienteActivity.class );
                pasarPantalla.putExtra ( "Activity", "caja" );
                startActivity ( pasarPantalla );
                finish ();
            }
        } );


        /*
        Boton para generar factura y finalizar proceso de venta. Inhabilitado hasta que no se
        selecciona cliente
         */
        botonFactura.setOnClickListener ( new View.OnClickListener ( ) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick ( View view ) {

                if(checkPermission()) {
                    progressBar.setVisibility ( View.VISIBLE );
                    generarPdf();

                } else {
                    requestPermissions();
                }
            }
        } );




    }
    public static float calcularTotal(){
        totalEuros = 0.00f;

        if(MainActivity.cajaArrayList !=null){

            for(int i = 0; i < MainActivity.cajaArrayList.size () ; i++){

                totalEuros += Float.parseFloat ( MainActivity.cajaArrayList.get ( i ).getPrecio () );
            }
        }

        return totalEuros;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generarPdf() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        TextPaint NombreEmpresa = new TextPaint();
        TextPaint datosEmpresa = new TextPaint (  );
        TextPaint numeroDeTicket = new TextPaint (  );
        TextPaint separador = new TextPaint (  );
        TextPaint contenidoCompra = new TextPaint();
        TextPaint totalDescripcion = new TextPaint();
        TextPaint fecha = new TextPaint();
        TextPaint agradecimiento = new TextPaint();
        TextPaint vendedor = new TextPaint();
        TextPaint tituloProducto = new TextPaint();
        TextPaint tituloPrecio = new TextPaint();



        PdfDocument.PageInfo paginaInfo = new PdfDocument.PageInfo.Builder(816, 1054, 1).create();
        PdfDocument.Page pagina1 = pdfDocument.startPage(paginaInfo);

        Canvas canvas = pagina1.getCanvas();

        NombreEmpresa.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        NombreEmpresa.setTextSize(20);
        canvas.drawText(Login.datosEmpresa[0], 50, 110, NombreEmpresa);

        //Datos de la empresa
        datosEmpresa.setTypeface ( Typeface.create ( Typeface.DEFAULT, Typeface.NORMAL ) );
        datosEmpresa.setTextSize (16 );
        if(Login.datosEmpresa[0].equals ( "" )){

            Login.datosEmpresa[0] = "Mi empresa";
        }
        if(Login.datosEmpresa[1].equals ( "" )){
            Login.datosEmpresa[1] = "00000000X";
        }
        if(Login.datosEmpresa[2].equals ( "" )){
            Login.datosEmpresa[2] = "Calle 0000 sin número";
        }
        if(Login.datosEmpresa[3].equals ( "" )) {
            Login.datosEmpresa[3] = "00000";
        }
        if(Login.datosEmpresa[4].equals ( "" )){
            Login.datosEmpresa[4] = "XXXXX";
        }
        if(Login.datosEmpresa[5].equals ( "" )){
            Login.datosEmpresa[5] = "XXXXX";
        }
        if(Login.datosEmpresa[6].equals ( "" )){
            Login.datosEmpresa[6] = "00000000";
        }
        if(Login.datosEmpresa[7].equals ( "" )){
            Login.datosEmpresa[7] = "empresa@mail.com";
        }

        canvas.drawText ( "NIF: "+ Login.datosEmpresa[1].toString (), 50, 130, datosEmpresa );
        canvas.drawText (  Login.datosEmpresa[2].toString (), 50, 150, datosEmpresa );
        canvas.drawText (  Login.datosEmpresa[3].toString () + " , "+ Login.datosEmpresa[4], 50, 170, datosEmpresa );
        canvas.drawText (  Login.datosEmpresa[5].toString () , 50, 190, datosEmpresa );
        canvas.drawText (  "Tlf: " + Login.datosEmpresa[6].toString () , 50, 210, datosEmpresa );
        canvas.drawText (  "Email: " + Login.datosEmpresa[7].toString () , 50, 230, datosEmpresa );


        //Titulos
        int y =260;
        tituloProducto.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        tituloProducto.setTextSize(16);
        canvas.drawText("PRODUCTOS", 50, y, tituloProducto);
        tituloPrecio.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        tituloPrecio.setTextSize(16);
        canvas.drawText("PRECIO", 300, y, tituloProducto);


        separador.setTypeface ( Typeface.defaultFromStyle ( Typeface.NORMAL ) );
        separador.setTextSize ( 16 );
        canvas.drawText ( "--------------------------------------------------------------------" +
                        "--------------------",
                50, 270,separador );

        contenidoCompra.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        contenidoCompra.setTextSize(16);


        //Items de la compra
         y = 290;
        for(int i = 0 ; i < MainActivity.cajaArrayList.size (); i++) {

            canvas.drawText(MainActivity.cajaArrayList.get ( i ).getNombre ().toUpperCase( Locale.ROOT ), 50, y, contenidoCompra);
            y += 20;
        }
        y = 290;
        totalDescripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        totalDescripcion.setTextSize(16);

              for(int i = 0 ; i < MainActivity.cajaArrayList.size (); i++) {

            canvas.drawText( MainActivity.cajaArrayList.get ( i ).getPrecio () + " Euros", 300, y, contenidoCompra);
            y += 20;
        }
        canvas.drawText ( "--------------------------------------------------------------------" +
                        "---------------------",
                50, y,separador );
        y+=20;
        totalDescripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        totalDescripcion.setTextSize(18);
        canvas.drawText ( "TOTAL", 50, y , totalDescripcion );
        canvas.drawText ( euros + " EUROS", 300, y , totalDescripcion );
        y+=30;
        //Agradecimiento
        agradecimiento.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        agradecimiento.setTextSize(18);
        canvas.drawText("GRACIAS POR SU VISITA", 50, y, agradecimiento);
        y+=30;
        //Número de Factura
        numeroDeTicket.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        numeroDeTicket.setTextSize(16);
        numEnteroFactura = Integer.parseInt ( numFactura ) + 1;
        canvas.drawText("Ticket número: " + numEnteroFactura, 50,y, numeroDeTicket);
        y+=20;
        //vendedor
        vendedor.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        vendedor.setTextSize ( 16 );
        canvas.drawText ( "Le ha atendido: " + Login.u.getNombre (), 50, y, vendedor );
        y+=20;
        //Fecha
        fecha.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        fecha.setTextSize(16);
        String fechaActual = DateTimeFormatter.ofPattern ( "dd MMM yyyy, hh:mm:ss a" )
                .format ( LocalDateTime.now () );
        canvas.drawText ( fechaActual, 50, y, fecha );


        pdfDocument.finishPage(pagina1);

        //Crea un archivo nuevo. El nombre del archivo se alcmacena en la variable facturaNombre para
        //después subirlo al servidor
        facturaNombre = "Factura"+numEnteroFactura+".pdf";
        //String facturaNombre2 = "FacturaNombre"+numEnteroFactura+".pdf";
        File file = new File(getExternalFilesDir ( null ),facturaNombre );
        try {
            //Se escribe el pdf en el archivo
            pdfDocument.writeTo(new FileOutputStream(file));
            //Se pasa el archivo a mapa de bytes para poder codificarlo en String y así pasarlo por parámetros al webservice
            byte [] bytes = Files.readAllBytes(file.toPath());
            //Se codifica a String en formato base64
            String facturaString = Base64.encodeToString ( bytes, Base64.DEFAULT );
            registrarVenta ( Login.u.getId (), SeleccionarClienteActivity.c.getId (),SeleccionarClienteActivity.c.getNombre () ,facturaString, facturaNombre );

        } catch (Exception e) {
            e.printStackTrace();
        }

        pdfDocument.close();
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
        pasarPantalla = new Intent (CajaActivity.this, MainActivity.class);
        startActivity(pasarPantalla);

        finish();
    }
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200) {
            if(grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void recuperarDatosFacturacion(){

        SharedPreferences preferences = getSharedPreferences ( "datosFacturacion", Context.MODE_PRIVATE );

        Login.datosEmpresa[0] = preferences.getString("nombre","") ;
        Login.datosEmpresa[1] = preferences.getString("nif","") ;
        Login.datosEmpresa[2] = preferences.getString("adress","") ;
        Login.datosEmpresa[3] = preferences.getString("cp","") ;
        Login.datosEmpresa[4] =  preferences.getString("city","") ;
        Login.datosEmpresa[5] = preferences.getString("pais","") ;
        Login.datosEmpresa[6] = preferences.getString("tlf","")  ;
        Login.datosEmpresa[7] = preferences.getString("email","")  ;

    }
    public void registrarVenta( String id_vendedor, String id_cliente,String nombre_cliente , String factura, String facturaNombre) {


        StringRequest stringRequest = new StringRequest ( Request.Method.POST , urlRegistrarFactura , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {

                try {
                    JSONObject jsonObject = new JSONObject ( response );
                    String exito = jsonObject.getString ( "exito" );

                    if (exito.equals ( "1" )) {

                        enviarCorreo ();


                    }
                    else{

                        Toast.makeText ( CajaActivity.this , "No se pudo registrar la venta" , Toast.LENGTH_SHORT ).show ( );
                        startActivity ( new Intent ( CajaActivity.this, MenuActivity.class ) );
                        finish ();
                    }



                } catch (JSONException e) {
                    e.printStackTrace ( );

                    Toast.makeText ( CajaActivity.this , e.getMessage ().toString () , Toast.LENGTH_LONG ).show ( );
                }



            }

        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

            }
        } ) {
            @Nullable
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {

                Map<String, String> params = new HashMap<> ( );

                params.put ( "id_vendedor" , id_vendedor );
                params.put ( "id_cliente" , id_cliente );
                params.put ( "nombre_cliente" , nombre_cliente );
                params.put ( "factura", factura );
                params.put ( "facturaNombre", facturaNombre );


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );

    }
    public void obtenerIdVenta() {

        StringRequest stringRequest = new StringRequest ( Request.Method.GET , urlobtenerId , new Response.Listener<String> ( ) {
            @Override
            public void onResponse ( String response ) {

                numFactura = response;


            }

        } , new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse ( VolleyError error ) {

            }
        } ) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        requestQueue.add ( stringRequest );
    }

    private void enviarCorreo(){

        /*
          Código para abrir gestor de correo y enviar factura
         */
        if(SeleccionarClienteActivity.c.getEmail ().equals ( "" ) )
        {
            startActivity (new Intent( CajaActivity.this, MenuActivity.class) );
            finish ();
            MainActivity.cajaArrayList.clear ();
            SeleccionarClienteActivity.c = null;
            Toast.makeText ( CajaActivity.this , "Factura generada correctamente" , Toast.LENGTH_SHORT ).show ( );

        }
        else{
            startActivity (new Intent( CajaActivity.this, MenuActivity.class) );
            finish ();
            Intent intent  =new Intent(Intent.ACTION_SENDTO);
            intent.setData ( Uri.parse ( "mailto:") );
            intent.putExtra ( Intent.EXTRA_EMAIL, new String[]{SeleccionarClienteActivity.c.getEmail ()} );
            intent.putExtra ( Intent.EXTRA_SUBJECT, "Su ticket de compra" );
            intent.putExtra ( Intent.EXTRA_TEXT, "Hola, en el siguiente enlace, podrá descargar el ticket de su compra." +
                    "\n https://vaticinal-center.000webhostapp.com/facturas/" + facturaNombre );
            startActivity ( intent );
            MainActivity.cajaArrayList.clear ();
            SeleccionarClienteActivity.c = null;
            Toast.makeText ( CajaActivity.this , "Factura generada correctamente" , Toast.LENGTH_SHORT ).show ( );

        }


    }

}