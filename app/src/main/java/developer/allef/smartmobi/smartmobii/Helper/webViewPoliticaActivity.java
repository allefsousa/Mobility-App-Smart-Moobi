package developer.allef.smartmobi.smartmobii.Helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.R;
import developer.allef.smartmobi.smartmobii.View.MenuActivity;

public class webViewPoliticaActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView web;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_politica);
        ButterKnife.bind(this);


        web.loadUrl("https://allefsousa.github.io/SmartMobiPoliticaPrivacidade/");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(webViewPoliticaActivity.this,MenuActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        startActivity(intent);


    }
}
