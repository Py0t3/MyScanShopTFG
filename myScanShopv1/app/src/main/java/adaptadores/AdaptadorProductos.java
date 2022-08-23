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

public class AdaptadorProductos extends BaseAdapter {

    private Context context;
    protected ArrayList<Producto> listaProductos;

    public AdaptadorProductos ( Context context,ArrayList<Producto> listaProductos ) {
        this.context= context;
        this.listaProductos= listaProductos;
    }

    @Override
    public int getCount() {

        return listaProductos.size ();
    }

    @Override
    public Producto getItem( int position) {

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
            view = inflater.inflate( R.layout.grid_item_main, viewGroup, false);
        }

        ImageView imagenGrid = (ImageView) view.findViewById(R.id.imagenGrid_start);
        TextView nombreProdcuto = (TextView) view.findViewById(R.id.labelgrid_start);

        final Producto item = getItem(position);
       //carga imagen desde url usando lib picasso
        Picasso.get().load(item.getUrlImagen ()).error ( R.drawable.logoxl ).into(imagenGrid);
        //carga nombre del producto
        nombreProdcuto.setText(item.getNombre ());

        return view;
    }

}
