package developer.allef.smartmobi.smartmobii.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by allef on 16/07/2017.
 */

public  class  Preferencias {
    private Context context; // variavel que recebera o contexto
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "smartCity.preferencias"; // nome do arquivo que vai salvar as preferencias
    private final int MODE= 0; // quais aplicativos podem ler suas preferencias
    private SharedPreferences.Editor editor;  // variavel para editar as preferencias;

    private final String CHAVE_INTRO = "introUsuario";

    public void salvarDados( boolean vf){
        editor.putBoolean(CHAVE_INTRO,vf);
        editor.commit();
    }
    public boolean getexibir(){
        boolean valorBoolean = preferences.getBoolean("introUsuario", false);
        return valorBoolean;
    }


    public Preferencias(Context contextParametro) {
        context = contextParametro;
        preferences = contextParametro.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferences.edit();
    }
}
