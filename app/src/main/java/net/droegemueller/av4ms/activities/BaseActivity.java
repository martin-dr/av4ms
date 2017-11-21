package net.droegemueller.av4ms.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.droegemueller.av4ms.App;
import net.droegemueller.av4ms.deps.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies(App.get(this), App.getComponent());

        // can be used for general purpose in all Activities of Application
    }

    protected abstract void injectDependencies(App application, ApplicationComponent component);

    @Override
    public void finish() {
        super.finish();
        releaseSubComponents(App.get(this));
    }

    protected abstract void releaseSubComponents(App application);


}
