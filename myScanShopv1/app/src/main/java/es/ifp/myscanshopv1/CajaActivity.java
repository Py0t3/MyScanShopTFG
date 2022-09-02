package es.ifp.myscanshopv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.File;
import java.io.FileOutputStream;

import adaptadores.AdaptadorCaja;
import clases.Producto;

public class CajaActivity extends AppCompatActivity {

    private Intent pasarPantalla;
    protected Toolbar toolbar;
    protected TextView label1;
    protected TextView totalFactura;
    protected Button botonFactura;
    protected ListView lista1;
    protected CheckBox checkBox;
    private ListAdapter adaptador;
    protected static ArrayList<Producto> listaCaja;
    protected ArrayList<Producto> listaTotal;
    protected static float totalEuros = 0.0f;
    private Bundle paquete;
    protected Producto p;

    protected String euros="";

    String tituloText = "Factura";
    protected  String datosFracturacion= "";


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_caja );
        //Sustitución de la actionbar por defecto por una customizada
        toolbar = ( Toolbar ) findViewById ( R.id.toolbar_caja );
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        botonFactura = (Button )findViewById ( R.id.botonFactura_caja );
        checkBox = (CheckBox )findViewById ( R.id.check_caja );
        lista1 = (ListView ) findViewById ( R.id.lista_caja );
        totalFactura = (TextView )findViewById ( R.id.totalFactura_caja ) ;

        recuperarDatosFacturacion ();

        listaCaja= new ArrayList<> (  );


        listaTotal = new ArrayList<> (  );


        totalFactura.setText ( Float.toString ( calcularTotal ()));

        adaptador= new AdaptadorCaja ( this, MainActivity.cajaArrayList);
        lista1.setAdapter ( adaptador );






        /*Probar capturar lista para PDF. Se crea ArrayList de tipo String y se le añaden
        los Strings. Aqui se añaden de forma manual para probar el creador de PDF , en la realidad
        se hará pidiendo los datos a la BDD
         */


        //Habilitar botón enviar factura. Si se ha pagado se habilita el botón.
        checkBox.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
               if(checkBox.isChecked ())
               {
                   botonFactura.setEnabled ( true );
               }
               else
               {
                   botonFactura.setEnabled ( false );
               }
            }
        } );


        botonFactura.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                if(checkPermission()) {

                    generarPdf();
                    //Limpia la cesta y deja los valores a 0
                    listaCaja = null;
                    //ScanActivity.cesta= null;
                   // MainActivity.cesta= null;
                    pasarPantalla = new Intent (CajaActivity.this, MenuActivity.class);
                    startActivity ( pasarPantalla );
                    finish ();

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
    public void generarPdf() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        TextPaint titulo = new TextPaint();
        TextPaint datosEmpresa = new TextPaint (  );
        TextPaint contenidoCompra = new TextPaint();
        TextPaint totalDescripcion = new TextPaint();

        PdfDocument.PageInfo paginaInfo = new PdfDocument.PageInfo.Builder(816, 1054, 1).create();
        PdfDocument.Page pagina1 = pdfDocument.startPage(paginaInfo);

        Canvas canvas = pagina1.getCanvas();

        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(20);
        canvas.drawText(tituloText, 10, 150, titulo);

        datosEmpresa.setTypeface ( Typeface.create ( Typeface.DEFAULT, Typeface.NORMAL ) );
        datosEmpresa.setTextSize (16 );
        int j = 180;
        for(int i = 0 ; i < Login.datosEmpresa.length ; i++) {
            canvas.drawText(Login.datosEmpresa[i].toString (), 10, j,datosEmpresa);
            j += 20;
        }

        contenidoCompra.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        contenidoCompra.setTextSize(14);


        int y = 200;
        for(int i = 0 ; i < listaCaja.size (); i++) {
            canvas.drawText(listaCaja.get ( i ).toString (), 10, y, contenidoCompra);
            y += 50;
        }
        totalDescripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        totalDescripcion.setTextSize(16);
        //totalEuros = "TOTAL:        " + totalFactura.getText ().toString () + " EUROS";
        //canvas.drawText(totalEuros, 10, y, totalDescripcion);


        pdfDocument.finishPage(pagina1);

        File file = new File(getExternalFilesDir ( null ), "Factura.pdf");
       // Toast.makeText(this, getExternalFilesDir(null).toString (), Toast.LENGTH_SHORT).show();
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Factura generada correctamente", Toast.LENGTH_SHORT).show();

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
        //pasarPantalla.putExtra ( "TOTALEUROS", euros );
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



}