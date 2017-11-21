package net.droegemueller.av4ms.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.droegemueller.av4ms.App;
import net.droegemueller.av4ms.R;
import net.droegemueller.av4ms.Av4msBasicReadData;
import net.droegemueller.av4ms.Util;
import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;
import net.droegemueller.av4ms.core.srv.ConnectionTestPresenter;
import net.droegemueller.av4ms.core.srv.ConnectionTestView;
import net.droegemueller.av4ms.core.srv.PreferenceRepository;
import net.droegemueller.av4ms.core.srv.ServerInteractorException;
import net.droegemueller.av4ms.deps.ApplicationComponent;

import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT;

public class ConnectionTestActivity extends BaseActivity implements ConnectionTestView {

    @Inject
    Context context;

    @Inject
    Resources resources;

    @Inject
    ConnectionTestPresenter presenter;

    @Inject
    PreferenceRepository preferences;

    @BindView(R.id.connection_test_button) Button button;
    @BindView(R.id.connection_test_button_goto_settings) Button buttonSettings;
    @BindView(R.id.connection_test_text) TextView textView;
    @BindView(R.id.connection_test_text_basic_version) TextView textViewBasicVersion;
    @BindView(R.id.connection_test_text_extension_version) TextView textViewExtensionVersion;
    @BindView(R.id.connection_test_text_manual_hint) TextView textViewManualHint;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @OnClick(R.id.connection_test_button)
    void onShowClick(View view) {
        textView.setText("");
        buttonSettings.setVisibility(View.VISIBLE);
        presenter.doConnectionTest();
    }

    @OnClick(R.id.connection_test_button_goto_settings)
    void onGotoConnectionSettings(View view) {
        final Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(EXTRA_SHOW_FRAGMENT, SettingsActivity.DataSyncPreferenceFragment.class.getName());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_test);
        ButterKnife.bind(this);
        presenter.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);


        updateHint();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHint();
    }

    @Override
    protected void onDestroy() {
        presenter.unbind();
        super.onDestroy();
    }

    @Override
    protected void injectDependencies(App application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void releaseSubComponents(App application) {
        application.releaseConnectionTestSubComponent();
    }

    @Override
    public void showMessage(String message) {

    }

    public static String stateText(Resources resources, Av4msBasicReadData.ChargerSlotStandardData s) {
        int key;
        if (s.connected) {
            switch (s.slotState) {
                case Charging:
                    key = R.string.slot_state_charging;
                    break;
                case Discharging:
                    key = R.string.slot_state_discharging;
                    break;
                case EmptySlot:
                    key = R.string.slot_state_empty;
                    break;
                case Full:
                    key = R.string.slot_state_full;
                    break;
                case Error:
                    key = R.string.slot_state_error;
                    break;
                default:
                    key = R.string.slot_state_unknown;
                    break;
            }
        } else {
            key = R.string.slot_state_disconnected;
        }
        return resources.getString(key);

    }

    @Override
    public void indicateProgress(int remaining, int total) {

    }

    @Override
    public void updateBasicData(MesswerteResponse data) {
        String fmt = getResources().getString(R.string.connection_text_success_template);
        String serverData = getServerConnectionDisplayString();
        String result = String.format(fmt, serverData);
        textView.setText(result);
    }

    @Override
    public void updateAv4msVersion(Pair<Integer, Integer> version) {
        String s;
        if (version != null) {
            Integer v1 = version.getLeft() == null ? 0 : version.getLeft();
            Integer v2 = version.getRight() == null ? 0 : version.getRight();
            s = String.format(getResources().getString(R.string.connection_text_av4ms_basic_version), v1, v2);
        }
        else {
            s = String.format(getResources().getString(R.string.connection_text_av4ms_basic_version_na));
        }
        textViewBasicVersion.setMovementMethod(LinkMovementMethod.getInstance());
        Util.setHtmlTextview(textViewBasicVersion, s);
    }

    public void updateHint() {
        String url = preferences.getPrefServerUrl("");
        String s = "";
        if (!url.endsWith("/")) url = url + "/";
        if (url.length() > 0) {
            url += "static/index.html";
            s = String.format(getResources().getString(R.string.connection_manual_connection_hint), url);
        }
        textViewManualHint.setMovementMethod(LinkMovementMethod.getInstance());
        Util.setHtmlTextview(textViewManualHint, s);
    }

    @Override
    public void updateAppExtensionVersion(Pair<Integer, Integer> version) {
        String s;
        if (version != null) {
            Integer v1 = version.getLeft() == null ? 0 : version.getLeft();
            Integer v2 = version.getRight() == null ? 0 : version.getRight();
            s = String.format(getResources().getString(R.string.connection_text_app_extension_version), v1, v2);
        }
        else {
            s = String.format(getResources().getString(R.string.connection_text_app_extension_version_na));
        }
        textViewExtensionVersion.setMovementMethod(LinkMovementMethod.getInstance());
        Util.setHtmlTextview(textViewExtensionVersion, s);
    }


    private String getServerConnectionDisplayString() {
        String serverData;
        if (preferences.getPrefServerDoAuth(false)) {
            serverData = String.format(getResources().getString(R.string.connection_text_server_detail_template_auth),
                    preferences.getPrefServerUrl(""),
                    preferences.getPrefServerUsername(""));
        }
        else {
            serverData = String.format(getResources().getString(R.string.connection_text_server_detail_template_noauth),
                    preferences.getPrefServerUrl(""));
        }
        return serverData;
    }

    @Override
    public void showError(Throwable throwable) {
        String fmt = getResources().getString(R.string.connection_text_failure_template);
        String msg;
        if (throwable instanceof ServerInteractorException.BadUrl) {
            msg = getResources().getString(R.string.connection_err_bad_url);
        } else if (throwable instanceof ServerInteractorException.NotAuthenticated) {
            msg = getResources().getString(R.string.connection_err_not_authenticated);
        } else if (throwable instanceof ServerInteractorException.NotConfigured) {
            msg = getResources().getString(R.string.connection_err_not_configured);
        } else if (throwable instanceof ServerInteractorException.NotFound) {
            msg = getResources().getString(R.string.connection_err_not_found);
        } else {
            msg = throwable.getLocalizedMessage();
        }
        String result = String.format(fmt, getServerConnectionDisplayString(), msg);
        textView.setText(result);
    }
}
