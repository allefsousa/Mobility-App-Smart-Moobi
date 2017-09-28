package developer.allef.smartmobi.smartmobii.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by allef on 09/08/2017.
 */

public class monitorHora {

    public static int retornoDataHora(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        // OU
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();

        Date data_atual = cal.getTime();

        String data_completa = dateFormat.format(data_atual);

        String hora_atual = dateFormat_hora.format(data_atual);

         String[] obtendoHoraexata = hora_atual.split(":");
        int horaInteira = 0;
        horaInteira = Integer.parseInt(String.valueOf(obtendoHoraexata[0]));




        return horaInteira;
    }

}
