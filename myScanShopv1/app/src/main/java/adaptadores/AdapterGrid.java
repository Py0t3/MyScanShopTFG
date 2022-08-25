package adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import clases.Producto;
import es.ifp.myscanshopv1.R;

public class AdapterGrid extends ArrayAdapter {
    Context context;
    List<Producto> arrayProductos;

    public AdapterGrid ( @NonNull Context context , List<Producto> arrayProductos ) {
        super ( context , R.layout.grid_item_main, arrayProductos );
        this.context = context;
        this.arrayProductos = arrayProductos;
    }
    @NonNull
    @Override
    public View getView ( int position , @Nullable View convertView , @NonNull ViewGroup parent ) {
        View view = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.grid_item_main,null, true );

        //TextView txtId = view.findViewById ( R.id.txtId );
        ImageView imaGrid = view.findViewById ( R.id.imagenGrid);
        TextView txtNombre = view.findViewById ( R.id.labelgrid);

        String id = arrayProductos.get(position).getIdentificador ();
        //txtId.setText ( arrayProductos.get ( position ).getId () );
        Picasso.get ( ).
                load (arrayProductos.get ( position ).getUrlImagen () ).
                error ( R.drawable.logoxl ).into ( imaGrid );
        txtNombre.setText (arrayProductos.get ( position ).getNombre ());
        return view;
    }
}
