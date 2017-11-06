package developer.allef.smartmobi.smartmobii.MockData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import developer.allef.smartmobi.smartmobii.Model.LocalVaga;
import developer.allef.smartmobi.smartmobii.R;

/**
 * Created by allef on 04/11/2017.
 */

public class Locais {

    public static LocalVaga mock(LatLng latLng){








        LocalVaga loca = new LocalVaga();

        loca.setId("-KxFISdGUXO8yjnxnGO8");
        loca.setLatitude(latLng.latitude);
        loca.setLongitude(latLng.longitude);
        loca.setNomeRua("Av. José Moisés Pereira, 557 - Jardim Alvorada, Franca - SP, Brasil");
        loca.setStatusVaga("Disponivel");

        Random rn = new Random();
        int range = 3 - 0+1;
        int randomNum =  rn.nextInt(range) + 0;

        loca.setTipoVaga(randomNum);

        return loca;




    }









}
