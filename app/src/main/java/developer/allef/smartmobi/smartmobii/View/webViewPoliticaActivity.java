package developer.allef.smartmobi.smartmobii.View;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.R;

public class webViewPoliticaActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView web;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_politica);
        ButterKnife.bind(this);


        web.loadUrl("https://allefsousa.github.io/SmartMobi/");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(webViewPoliticaActivity.this,MenuActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        startActivity(intent);


    }
}
