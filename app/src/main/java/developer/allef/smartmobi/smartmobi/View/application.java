package developer.allef.smartmobi.smartmobi.View;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Allef on 15/06/2017.
 */

public class application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // inicializando o SDK do facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //Timber.i("Signature " +  FacebookSdk.getApplicationSignature(getApplicationContext()));

    }
}
