package adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import es.ifp.myscanshopv1.R;
import clases.Venta;

/**
 * adaptador para mostrar la vista del historial de ventas
 */
public class AdaptadorHistorial extends BaseAdapter {

    private Context context;
    protected ArrayList<Venta> listaVentas;

    public AdaptadorHistorial ( Context context, ArrayList<Venta> listaVentas ) {
        this.context = context;
        this.listaVentas= listaVentas;
    }
    @Override
    public int getCount () {

        return listaVentas.size ( );
    }

    @Override
    public Venta getItem ( int i ) {

        return listaVentas.get ( i );
    }

    @Override
    public long getItemId ( int i ) {


        int id= Integer.parseInt ( getItem(i).getId () );
        return id;
    }

    @Override
    public View getView( int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate( R.layout.item_historial, viewGroup, false);
        }


        TextView fecha = (TextView) view.findViewById(R.id.fecha_itemHist);
        TextView nombreCliente = (TextView) view.findViewById(R.id.nombreCliente_itemHist);
        TextView idVendedor = (TextView) view.findViewById(R.id.idVendedor_itemHist);


        final Venta item = getItem(position);


        fecha.setText(item.getFecha ());
        nombreCliente.setText ( item.getNombreCliente ());
        idVendedor.setText ( item.getIdVendedor () );


        return view;
    }

}
