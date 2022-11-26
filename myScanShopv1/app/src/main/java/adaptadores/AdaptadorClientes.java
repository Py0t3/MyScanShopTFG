package adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import es.ifp.myscanshopv1.R;
import java.util.ArrayList;

import clases.Cliente;

/**
 * Adaptador para mostrar la vista de Clentes
 */

public class AdaptadorClientes extends BaseAdapter {

    private Context context;
    protected ArrayList<Cliente> listaClientes;


    public AdaptadorClientes( Context context,ArrayList<Cliente> listaClientes ) {
        this.context = context;
        this.listaClientes= listaClientes;
    }

    @Override
    public int getCount() {

        return listaClientes.size ();
    }

    @Override
    public Cliente getItem(int position) {

        return listaClientes.get ( position );
    }

    @Override
    public long getItemId(int position) {

        int id= Integer.parseInt ( getItem(position).getId () );
        return id;
    }

    @Override
    public View getView( int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate( R.layout.clientes_list_item, viewGroup, false);
        }

        TextView idCliente = (TextView) view.findViewById(R.id.idCliente_item);
        TextView nombrecliente = (TextView) view.findViewById(R.id.nombreCliente_item);
        TextView dniCliente = (TextView) view.findViewById(R.id.dniCliente_item);

        final Cliente item = getItem(position);

        //carga nombre del producto
        idCliente.setText ( item.getId () );
        nombrecliente.setText(item.getNombre());
        dniCliente.setText ( item.getDni ());


        return view;
    }
}
