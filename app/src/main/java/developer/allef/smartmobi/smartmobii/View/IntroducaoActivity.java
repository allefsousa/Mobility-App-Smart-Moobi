package developer.allef.smartmobi.smartmobii.View;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import developer.allef.smartmobi.smartmobii.R;

/**
 * Created by allef on 15/07/2017.
 */

public class IntroducaoActivity extends MaterialIntroActivity {
    private String[] arrayPer = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.primaryColor)
                .buttonsColor(R.color.introIdosoBacgraund)
                .image(R.drawable.openapp)
                .title("Smart Mobi")
                .description("Aplicativo tem como base exibir vagas de automoveis preferenciais e Rampas de acessibilidade a cadeirantes.")
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.introprimaryColor)
                .buttonsColor(R.color.introIdosoBacgraund)
                .image(R.drawable.bb)
                .title("Detalhes sobre o aplicativo")
                .description("No mapa é possivel visualizar os locais mais proximos de você com vagas preferenciais,Você tambem pode adicionar novas localizações e avaliar as que ja exitem." +
                        "Marcar as vaga como ocupada quando tiver utilizando.")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.introtres)
                .buttonsColor(R.color.introIdosoBacgraund)
                .image(R.drawable.linhaa)
                .title("Linha do Tempo")
                .description("Com o Smart Mobi é possivel publicar imagens que mostrem se as pessoas em sua cidade respeitam ou nao os seus direitos. " +
                        " Fique a vontade para compartilhar o que quiser !")
                .build());

        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.n)
                .backgroundColor(R.color.primaryColor)
                .buttonsColor(R.color.introIdosoBacgraund)
                .title("Sugestões, Duvidas, Criticas")
                .description("Assim como o dia e a noite o aplicativo tambem muda conforme o horario. Vamos adorar receber o seu feedback, entre em contato com agente.")
                .build());






    }
//    @Override
//    public void onFinish() {
//        super.onFinish();
//        startActivity(new Intent(IntroducaoActivity.this, MenuActivity.class));
//
//    }


}
