package adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import clases.Producto;
import es.ifp.myscanshopv1.R;

public class AdaptadorCaja extends BaseAdapter {

    private Context context;
    protected ArrayList<Producto> listaCaja;
            ;


    public AdaptadorCaja ( Context context,ArrayList<Producto> listaCaja) {
        this.context = context;
        this.listaCaja = listaCaja;
    }

    @Override
    public int getCount() {

        return listaCaja.size ();
    }

    @Override
    public Producto getItem(int position) {

        return listaCaja.get ( position );
    }

    @Override
    public long getItemId(int position) {

        int id= Integer.parseInt ( getItem(position).getIdentificador () );
        return id;
    }

    @Override
    public View getView( int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate( R.layout.item_cesta, viewGroup, false);
        }


        TextView nombreProducto = (TextView) view.findViewById(R.id.label_nombre_cesta);
        TextView precioProducto = (TextView) view.findViewById(R.id.label_precio_cesta);

        final Producto item = getItem(position);
        nombreProducto.setText(item.getNombre());
        precioProducto.setText ( item.getPrecio () );

        return view;
    }


}
