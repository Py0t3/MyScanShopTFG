package adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import clases.Producto;
import es.ifp.myscanshopv1.R;

public class AdaptadorInventario extends BaseAdapter {

    private Context context;
    protected ArrayList<Producto> listaProductos;


    public AdaptadorInventario ( Context context,ArrayList<Producto> listaProductos ) {
        this.context = context;
        this.listaProductos= listaProductos;
    }

    @Override
    public int getCount() {

        return listaProductos.size ();
    }

    @Override
    public Producto getItem(int position) {

        return listaProductos.get ( position );
    }

    @Override
    public long getItemId(int position) {

        return getItem(position).getIdentificador ();
    }

    @Override
    public View getView( int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate( R.layout.inventario_list_item, viewGroup, false);
        }

        ImageView imagenInventario = (ImageView) view.findViewById(R.id.imagen_listview);
        TextView nombreProducto = (TextView) view.findViewById(R.id.label1_listview);
        TextView precioProducto = (TextView) view.findViewById(R.id.label2_listview);
        TextView descripcionProducto = (TextView) view.findViewById(R.id.label3_listview);

        final Producto item = getItem(position);

        //carga nombre del producto
        nombreProducto.setText(item.getNombre());
        precioProducto.setText ( item.getPrecio () + " EUROS" );
        //carga imagen desde url usando lib picasso
        Picasso.get().load(item.getUrlImagen ()).error ( R.drawable.logoxl ).into(imagenInventario);
        descripcionProducto.setText ( item.getDescripcion () );

        return view;
    }


}
