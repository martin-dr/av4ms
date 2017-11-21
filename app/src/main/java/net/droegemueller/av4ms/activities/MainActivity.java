package net.droegemueller.av4ms.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import net.droegemueller.av4ms.App;
import net.droegemueller.av4ms.R;
import net.droegemueller.av4ms.Av4msBasicReadData;
import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;
import net.droegemueller.av4ms.core.srv.MainActivityPresenter;
import net.droegemueller.av4ms.core.srv.MainActivityView;
import net.droegemueller.av4ms.core.srv.PreferenceRepository;
import net.droegemueller.av4ms.deps.ApplicationComponent;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainActivityView, SlotMainFragment.OnSlotInteractionListener, HomeMainFragment.OnFragmentInteractionListener, LogsMainFragment.OnFragmentInteractionListener, NotificationsMainFragment.OnFragmentInteractionListener {

    private Timer autoUpdate;

    private int successCount = 0;

    private int failCount = 0;

    @Inject
    Context context;

    @Inject
    Resources resources;

    @Inject
    MainActivityPresenter presenter;

    @Inject
    PreferenceRepository preferences;

    private Fragment mainFragment = null;
    private SlotMainFragment slots[] = {null, null, null, null};

    private int currentFragmentState = 0;

    public static final String STATE_CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    public static final String FRAGMENT_MAIN = "fragment_main";
    public static final String FRAGMENT_SLOT_0 = "fragment_slot_0";
    public static final String FRAGMENT_SLOT_1 = "fragment_slot_1";
    public static final String FRAGMENT_SLOT_2 = "fragment_slot_2";
    public static final String FRAGMENT_SLOT_3 = "fragment_slot_3";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (currentFragmentState != 0) {
                            currentFragmentState = 0;
                            restoreCurrentFragmentState(null);
                            return true;
                        }
                        return false;
                    case R.id.navigation_log:
                        if (currentFragmentState != 1) {
                            currentFragmentState = 1;
                            restoreCurrentFragmentState(null);
                            return true;
                        }
                        return false;
                    case R.id.navigation_notifications:
                        if (currentFragmentState != 2) {
                            currentFragmentState = 2;
                            restoreCurrentFragmentState(null);
                            return true;
                        }
                        return false;
                }
                return false;
            };

    private void restoreCurrentFragmentState(Bundle savedInstanceState) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (mainFragment != null) transaction.remove(mainFragment);
        for (SlotMainFragment slot : slots) {
            if (slot != null) transaction.remove(slot);
        }
        if (currentFragmentState == 0) {
            if (savedInstanceState != null) mainFragment = fm.getFragment(savedInstanceState, FRAGMENT_MAIN);
            if (mainFragment == null) mainFragment = HomeMainFragment.newInstance();
            transaction.replace(R.id.mainContentFragment, mainFragment);
            if (savedInstanceState != null) slots[0] = (SlotMainFragment) fm.getFragment(savedInstanceState, FRAGMENT_SLOT_0);
            if (slots[0] == null) slots[0] = SlotMainFragment.newInstance(1);
            transaction.replace(R.id.mainContentFragment1, slots[0]);
            if (savedInstanceState != null) slots[1] = (SlotMainFragment) fm.getFragment(savedInstanceState, FRAGMENT_SLOT_1);
            if (slots[1] == null) slots[1] = SlotMainFragment.newInstance(2);
            transaction.replace(R.id.mainContentFragment2, slots[1]);
            if (savedInstanceState != null) slots[2] = (SlotMainFragment) fm.getFragment(savedInstanceState, FRAGMENT_SLOT_2);
            if (slots[2] == null) slots[2] = SlotMainFragment.newInstance(3);
            transaction.replace(R.id.mainContentFragment3, slots[2]);
            if (savedInstanceState != null) slots[3] = (SlotMainFragment) fm.getFragment(savedInstanceState, FRAGMENT_SLOT_3);
            if (slots[3] == null) slots[3] = SlotMainFragment.newInstance(4);
            transaction.replace(R.id.mainContentFragment4, slots[3]);
        } else if (currentFragmentState == 1) {
            transaction.replace(R.id.mainContentFragment, mainFragment = new LogsMainFragment());
        } else if (currentFragmentState == 2) {
            transaction.replace(R.id.mainContentFragment, mainFragment = new NotificationsMainFragment());
            for (int i = 0; i < slots.length; i++) {
                slots[i] = null;
            }
        }
        transaction.commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_options:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_connection_test:
                startActivity(new Intent(this, ConnectionTestActivity.class));
                break;
            // action with ID action_settings was selected
            case R.id.action_about:
                Toast.makeText(this, "About ..", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_FRAGMENT, currentFragmentState);
        if (mainFragment != null) getFragmentManager().putFragment(outState, FRAGMENT_MAIN, mainFragment);
        if (slots[0] != null) getFragmentManager().putFragment(outState, FRAGMENT_SLOT_0, slots[0]);
        if (slots[1] != null) getFragmentManager().putFragment(outState, FRAGMENT_SLOT_1, slots[1]);
        if (slots[2] != null) getFragmentManager().putFragment(outState, FRAGMENT_SLOT_2, slots[2]);
        if (slots[3] != null) getFragmentManager().putFragment(outState, FRAGMENT_SLOT_3, slots[3]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentFragmentState = savedInstanceState.getInt(STATE_CURRENT_FRAGMENT);
        }
        restoreCurrentFragmentState(savedInstanceState);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        presenter.bind(this);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> presenter.updateData());
            }
        }, 0, 1000); // updates each ... millisecs
    }

    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }

    @Override
    public void showBasicData(MesswerteResponse data) {
        try {
            successCount++;
            failCount = 0;
            Av4msBasicReadData d = Av4msBasicReadData.fromResponse(data);
            for (SlotMainFragment slot : slots) {
                if (slot != null) {
                    slot.updateSlotData(d);
                }
            }
            if (mainFragment != null && mainFragment instanceof HomeMainFragment) {
                HomeMainFragment homeMainFragment = (HomeMainFragment) mainFragment;
                homeMainFragment.updateData(d);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void showError(Throwable throwable) {
        int previoussuccessCount = successCount;
        successCount = 0;
        failCount++;

        boolean isBatteryFragmentActive = mainFragment != null && mainFragment instanceof HomeMainFragment;

        if (isBatteryFragmentActive && (previoussuccessCount == 0 && failCount > 10 || failCount > 30)) {
            // TODO: Change fragment
        } else if (isBatteryFragmentActive && failCount % 10 == 0) {
            Toast.makeText(this, throwable.toString(), Toast.LENGTH_SHORT).show();
        } else if (!isBatteryFragmentActive) {
            // TODO: Update Fehlerseite
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    protected void injectDependencies(App application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void releaseSubComponents(App application) {
        application.releaseMainActivitySubComponent();
    }

    @Override
    public void requestUpdate() {
        presenter.updateData();
    }
}
