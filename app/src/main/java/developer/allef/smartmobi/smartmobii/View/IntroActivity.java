package developer.allef.smartmobi.smartmobii.View;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import developer.allef.smartmobi.smartmobii.Helper.Preferencias;
import developer.allef.smartmobi.smartmobii.R;

/**
 * Created by allef on 15/07/2017.
 */

public class IntroActivity extends MaterialIntroActivity {
    Preferencias pref;
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

        pref = new Preferencias(IntroActivity.this);

        // exibeslides();
        exibirIntroducao();


        // TODO: 16/08/2017  verificar internet primerio acesso ao app caso nao tenha fechar o app


    }

    @Override
    public void onFinish() {
        pref.salvarDados(true);
        startActivity(new Intent(IntroActivity.this, MenuActivity.class));
        super.onFinish();

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * metodo responsavel por apresentar a apresentação do ap apenas uma vez.
     * salvo utilizando shared preferences
     */
    private void exibirIntroducao() {

        if (pref.getexibir()) {

            startActivity(new Intent(IntroActivity.this, MenuActivity.class));
            finish();


        } else {
            exibeslides();
        }

    }

    private void exibeslides() {
        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.primaryColor)
                .buttonsColor(R.color.introback)
                .title("Smart Mobi")
                .description("Aplicativo tem como base exibir vagas de automoveis preferenciais e Rampas de acessibilidade a cadeirantes.")
                .build());
//


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addSlide(new SlideFragmentBuilder()
                            .backgroundColor(R.color.colorPrimary)
                            .buttonsColor(R.color.colorAccent)
                            .possiblePermissions(arrayPer)
                            .neededPermissions(arrayPer)
                            .title("Permisões")
                            .description("Para Um melhor desempenho é necessarios habilitar alguns recursos do dispositivo.")
                            .build(),
                    new MessageButtonBehaviour(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMessage("Impossivel executar app sem as permissões necessarias.");
                        }
                    }, "Verificar"));


        }else{
            addSlide(new SlideFragmentBuilder()
                    .backgroundColor(R.color.primaryColor)
                    .buttonsColor(R.color.introback)
                    .title("Smart Mobi")
                    .description("Aplicativo tem como base exibir vagas de automoveis preferenciais e Rampas de acessibilidade a cadeirantes.")
                    .build());

        }
    }


}
