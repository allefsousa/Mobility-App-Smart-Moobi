package developer.allef.smartmobi.smartmobi.Helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allef on 03/05/2017.
 */

public class Permissao {
    public static Boolean validapermissoes(int requestCode, Activity activity, String[] permissoes){

        // verificando a versao o android
        if(Build.VERSION.SDK_INT >= 23){
            List<String> ListaPermissoes = new ArrayList<String>();
            for(String permissao : permissoes){
                // verificando se o usuario ja aceitou essa permisao
                Boolean validaPermisao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!validaPermisao){
                    ListaPermissoes.add(permissao);
                }
            }
            if(ListaPermissoes.isEmpty()){
                return true;
            }
            // convertendo um arraylist em uma arrai de string
            String[] novaspermissions = new String[ListaPermissoes.size()];
            ListaPermissoes.toArray(novaspermissions);

            // solicitando as permissoes
            ActivityCompat.requestPermissions(activity,novaspermissions,requestCode);

        }

        return true;

    }



}
