package clases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import clases.Producto;

public class DataBaseSQLite extends SQLiteOpenHelper {

    protected SQLiteDatabase db;

    public DataBaseSQLite ( @Nullable Context context  ) {
        super ( context , "tienda", null , 1);
    }

    @Override
    public void onCreate ( SQLiteDatabase db) {

        db.execSQL ( "CREATE TABLE productos (id integer primary key autoincrement not null, nombre varchar,  precio float , imagen text, codigoBarras varchar, descripcion text)" );
        /*
            Registros insertados a modo de ejemplo
         */
        db.execSQL ( "INSERT INTO productos ( nombre, precio, imagen, codigoBarras, descripcion) VALUES ('Producto 1', 10, 'https://loremflickr.com/cache/resized/65535_51749990124_30b97294a3_n_320_240_nofilter.jpg', '7638900424256', 'Producto de prueba' )");
        db.execSQL ( "INSERT INTO productos ( nombre, precio, imagen, codigoBarras, descripcion) VALUES ('Producto 2', 20, 'https://loremflickr.com/cache/resized/65535_51710688969_5ff55dd120_320_240_nofilter.jpg', '7638900424246', 'Producto de prueba' )");
    }

    @Override
    public void onUpgrade ( SQLiteDatabase db , int i , int i1 ) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        this.onCreate (db);
    }

    public boolean insertarProducto(String nombre, float precio, String imagen, String codigoBarras, String descripcion){

        String nota="";
        db= this.getWritableDatabase ();
        db.execSQL("INSERT INTO productos ( nombre, precio, imagen, codigoBarras, descripcion) VALUES ('"+nombre+"',"+precio+",'"+imagen+"','"+codigoBarras+"','"+descripcion+"')");

        return true;

    }

    public int numberOfNotes(){

        int num=0;
        db= this.getReadableDatabase();
        num= (int) DatabaseUtils.queryNumEntries(db,"productos");
        return num;
    }

    @SuppressLint("Range")
    public ArrayList<Producto> getAllNotes (){

        ArrayList<Producto> listaProductos = new ArrayList<> (  );
        Cursor res= null;


        if(numberOfNotes ()>0)
        {
            db = this.getReadableDatabase ( );
            res = db.rawQuery ( "SELECT * FROM productos" , null );
            res.moveToFirst ( );
            while (res.isAfterLast ( ) == false) {

                Producto p = new Producto ( res.getInt ( res.getColumnIndex ( "id" )) , res.getString (1 ) ,
                        res.getFloat(2 ) , res.getString ( 3) ,
                        res.getString ( 4 ) , res.getString (5 ) );

                listaProductos.add ( p );
                res.moveToNext ();
            }
        }



        return listaProductos;
    }
    @SuppressLint("Range")
    public Producto getNote(String nombre) {

        Producto p = null;
        Cursor res = null;
        if (numberOfNotes ( ) > 0) {
            db = this.getReadableDatabase ( );
            res = db.rawQuery ( "SELECT * FROM productos WHERE nombre=" + nombre , null );
            res.moveToFirst ( );

            while (res.isAfterLast ( ) == false) {

                 p = new Producto ( res.getInt ( res.getColumnIndex ( "id" )) , res.getString (1 ) ,
                        res.getFloat(2 ) , res.getString ( 3) ,
                        res.getString ( 4 ) , res.getString (5 ) );
                 res.moveToNext ( );
            }
        }
        return p;
    }
    @SuppressLint("Range")
    public Producto getProductoByCode( String codigoBarras) {

        Producto p = null;
        Cursor res = null;
        if (numberOfNotes ( ) > 0) {
            db = this.getReadableDatabase ( );
            res = db.rawQuery ( "SELECT * FROM productos WHERE codigoBarras=" + "'"+codigoBarras+"'" , null );
            res.moveToFirst ( );

            while (res.isAfterLast ( ) == false) {

                p = new Producto ( res.getInt ( res.getColumnIndex ( "id" )) , res.getString (1 ) ,
                        res.getFloat(2 ) , res.getString ( 3) ,
                        res.getString ( 4 ) , res.getString (5 ) );
                res.moveToNext ( );
            }
        }
        return p;
    }
    public boolean deleteProducto(String nombre){

        db=this.getWritableDatabase ();
        db.execSQL ( "DELETE FROM productos WHERE nombre= "+"'"+nombre + "'" );

        return true;

    }

}
