package net.droegemueller.av4ms;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import net.droegemueller.av4ms.deps.ApplicationComponent;
import net.droegemueller.av4ms.deps.ApplicationModule;
import net.droegemueller.av4ms.deps.ConnectionTestModule;
import net.droegemueller.av4ms.deps.ConnectionTestSubComponent;
import net.droegemueller.av4ms.deps.DaggerApplicationComponent;
import net.droegemueller.av4ms.deps.MainActivityModule;
import net.droegemueller.av4ms.deps.MainActivitySubComponent;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static ApplicationComponent component;
    private ConnectionTestSubComponent connectionTestSubComponent;
    private MainActivitySubComponent mainActivitySubComponent;

    public static ApplicationComponent getComponent() {
        return component;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public ConnectionTestSubComponent getConnectionTestSubComponent() {
        if (connectionTestSubComponent == null)
            createConnectionTestSubComponent();
        return connectionTestSubComponent;
    }

    public MainActivitySubComponent getMainActivitySubComponent() {
        if (mainActivitySubComponent == null)
            createMainActivitySubComponent();
        return mainActivitySubComponent;
    }

    private ConnectionTestSubComponent createConnectionTestSubComponent() {
        connectionTestSubComponent = component.plus(new ConnectionTestModule());
        return connectionTestSubComponent;
    }
    private MainActivitySubComponent createMainActivitySubComponent() {
        mainActivitySubComponent = component.plus(new MainActivityModule());
        return mainActivitySubComponent;
    }

    public static String getMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
// if we can’t find it in the manifest, just return null
        }

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String apiKey = getMetadata(this, "io.fabric.ApiKey");
        if (apiKey != null && !apiKey.equals("ffffffffffffffffffffffffffffffffffffffff")) {
            CrashlyticsCore core = new CrashlyticsCore.Builder()
                    .disabled(false && BuildConfig.DEBUG)
                    .build();
            Fabric.with(this, new Crashlytics.Builder().core(core).build());
        }
        component =  DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }


    public void releaseConnectionTestSubComponent() {
        connectionTestSubComponent = null;
    }
    public void releaseMainActivitySubComponent() {
        mainActivitySubComponent = null;
    }
}
