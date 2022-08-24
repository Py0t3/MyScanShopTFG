package es.ifp.myscanshopv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    protected EditText cajaUser, cajaPass;
    protected Button botonEnter;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        View decorView = getWindow ( ).getDecorView ( );
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility ( uiOptions );

        cajaUser = ( EditText ) findViewById ( R.id.cajaUser_login );
        cajaPass = ( EditText ) findViewById ( R.id.cajaPass_login );
        botonEnter = ( Button ) findViewById ( R.id.botonEnter_login );

        botonEnter.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {

                startActivity ( new Intent ( Login.this, MenuActivity.class ) );
                finish ();
            }
        } );



    }
}