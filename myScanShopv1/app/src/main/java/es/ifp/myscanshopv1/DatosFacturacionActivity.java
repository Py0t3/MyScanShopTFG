package es.ifp.myscanshopv1;

import static es.ifp.myscanshopv1.Login.datosEmpresa;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class DatosFacturacionActivity extends AppCompatActivity {

    protected Intent pasarPantalla;
    protected Toolbar toolbar;
    protected EditText nombreEmpresa, nifEmpresa, direccionEmpresa, cpEmpresa, poblacionEmpresa,
            paisEmpresa, tlfEmpresa, emailEmpresa;
    protected Button botonGuardar, botonBorrar;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_datos_facturacion );
        toolbar = ( Toolbar ) findViewById(R.id.toolbar_facturacion);
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombreEmpresa = (EditText ) findViewById ( R.id.nombreEmpresa_datos );
        nifEmpresa = (EditText ) findViewById ( R.id.nif_datos );
        direccionEmpresa = (EditText ) findViewById ( R.id.direccion_datos );
        cpEmpresa = (EditText ) findViewById ( R.id.cp_datos );
        poblacionEmpresa = (EditText ) findViewById ( R.id.poblacion_datos );
        paisEmpresa = (EditText ) findViewById ( R.id.pais_datos );
        tlfEmpresa = (EditText ) findViewById ( R.id.telefono_datos );
        emailEmpresa = (EditText ) findViewById ( R.id.email_datos );
        botonGuardar = (Button ) findViewById ( R.id.botonGuardar_datos );
        botonBorrar = (Button ) findViewById ( R.id.botonBorrar_datos );



        recuperarPreferencias ();
        botonGuardar.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                Login.datosEmpresa[0] = nombreEmpresa.getText ().toString ();
                Login.datosEmpresa[1] = nifEmpresa.getText ().toString ();
                Login.datosEmpresa[2] = direccionEmpresa.getText ().toString ();
                Login.datosEmpresa[3] = cpEmpresa.getText ().toString ();
                Login.datosEmpresa[4] = poblacionEmpresa.getText ().toString ();
                Login.datosEmpresa[5] = paisEmpresa.getText ().toString ();
                Login.datosEmpresa[6] = tlfEmpresa.getText ().toString ();
                Login.datosEmpresa[7] = emailEmpresa.getText ().toString ();
                guardarPreferencias ();
                Toast.makeText ( DatosFacturacionActivity.this , "Datos guardados correctamente" , Toast.LENGTH_SHORT ).show ( );
                pasarPantalla = new Intent ( DatosFacturacionActivity.this, MenuActivity.class );
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
        pasarPantalla = new Intent (DatosFacturacionActivity.this, MenuActivity.class);
        startActivity(pasarPantalla);
        finish();
    }
    private void guardarPreferencias(){

        SharedPreferences preferences = getSharedPreferences ( "datosFacturacion", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= preferences.edit ();
        editor.putString ( "nombre", datosEmpresa[0] );
        editor.putString ( "nif", datosEmpresa[1] );
        editor.putString ( "adress", datosEmpresa[2] );
        editor.putString ( "cp", datosEmpresa[3] );
        editor.putString ( "city", datosEmpresa[4] );
        editor.putString ( "pais", datosEmpresa[5] );
        editor.putString ( "tlf", datosEmpresa[6] );
        editor.putString ( "email", datosEmpresa[7] );
        editor.putBoolean ( "sesion", true );
        editor.commit ();
    }
    private void recuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences ( "datosFacturacion", Context.MODE_PRIVATE );
        nombreEmpresa.setText ( preferences.getString("nombre","") );
        nifEmpresa.setText ( preferences.getString("nif","") );
        direccionEmpresa.setText ( preferences.getString("adress","") );
        cpEmpresa.setText ( preferences.getString("cp","") );
        poblacionEmpresa.setText ( preferences.getString("city","") );
        paisEmpresa.setText ( preferences.getString("pais","") );
        tlfEmpresa.setText ( preferences.getString("tlf","") );
        emailEmpresa.setText ( preferences.getString("email","") );

    }
}