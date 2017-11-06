package developer.allef.smartmobi.smartmobii.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import developer.allef.smartmobi.smartmobii.R;

/**
 * Created by allef on 05/11/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String [] tiposVagas;
    private int [] imagens;

    public SpinnerAdapter(@NonNull Context context, String[] tiposvaga, int[] imagens) {

        super(context, R.layout.adaptersppiner,tiposvaga);
        this.context = context;
        this.tiposVagas = tiposvaga;
        this.imagens = imagens;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adaptersppiner,null);

        }
        TextView nome = convertView.findViewById(R.id.textoSpinner);
        ImageView image = convertView.findViewById(R.id.imageSpinner);

        nome.setText(tiposVagas[position]);
        image.setImageResource(imagens[position]);


        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adaptersppiner,null);

        }
        TextView nome = convertView.findViewById(R.id.textoSpinner);
        ImageView image = convertView.findViewById(R.id.imageSpinner);

        nome.setText(tiposVagas[position]);
        image.setImageResource(imagens[position]);


        return convertView;
    }
}
