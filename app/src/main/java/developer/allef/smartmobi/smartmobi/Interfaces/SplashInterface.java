package developer.allef.smartmobi.smartmobi.Interfaces;

import android.content.Intent;

/**
 * Created by Allef on 04/05/2017.
 */

public interface SplashInterface {

    interface View   {
      void  StartProximaActivity(Intent intent);


    }

    interface Presenter{
        void BuscandoLocalizacao();
        void conexaoBD();
        void verificaArray();
    }
}
