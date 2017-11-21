package net.droegemueller.av4ms.core.srv;

import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;
import net.droegemueller.av4ms.core.util.SchedulerProvider;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class ConnectionTestPresenterImpl implements ConnectionTestPresenter {

    @Inject
    ServerInteractor interactor;

    @Inject
    PreferenceRepository preferences;

    private SchedulerProvider scheduler;

    @Inject
    public ConnectionTestPresenterImpl(SchedulerProvider scheduler) {
        this.scheduler = scheduler;
    }


    private ConnectionTestView view;
    private List<Disposable> subscriptions = new ArrayList<>();

    @Override
    public void bind(ConnectionTestView view) {
        this.view = view;
    }

    @Override
    public void unbind() {
        Iterator<Disposable> i = subscriptions.iterator();
        while (i.hasNext()) {
            Disposable disposable = i.next();
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                i.remove();
            }
        }
        interactor.unbind();
        remaining = total = 0;
        view = null;
    }

    private int remaining = 0;
    private int total = 0;

    private synchronized void startCall() {
        ++remaining;
        ++total;
        if (view != null) view.indicateProgress(remaining, total);
    }
    private synchronized void endCall() {
        --remaining;
        if (view != null) view.indicateProgress(remaining, total);
    }


    @Override
    public void doConnectionTest() {
        String chksum = preferences.getConfiguredConnChecksum();

        startCall();
        subscriptions.add(interactor.loadBasicData()
                .observeOn(scheduler.mainThread())
                .subscribe(
                        messwerteResponse -> {
                            endCall();
                            preferences.setPrefLastSuccessfulConnChecksum(chksum);
                            preferences.setPrefChecksummedConnHasBasicMeasurementValues(chksum, true);
                            if (view != null) view.updateBasicData(messwerteResponse);
                        },
                        throwable -> {
                            endCall();
                            preferences.setPrefChecksummedConnHasBasicMeasurementValues(chksum, false);
                            if (view != null) view.showError(throwable);
                        }));

        startCall();
        subscriptions.add(interactor.loadAv4msBasicVersion()
                .observeOn(scheduler.mainThread())
                .subscribe(
                        version -> {
                            endCall();
                            preferences.setPrefChecksummedConnAv4msVersion(chksum, version);
                            if (view != null) view.updateAv4msVersion(version);
                        },
                        throwable -> {
                            endCall();
                            preferences.setPrefChecksummedConnAv4msVersion(chksum, null);
                            if (view != null) view.updateAv4msVersion(null);
                        }));
        startCall();
        subscriptions.add(interactor.loadAppExtensionVersion()
                .observeOn(scheduler.mainThread())
                .subscribe(
                        version -> {
                            endCall();
                            preferences.setPrefChecksummedConnAppExtensionVersion(chksum, version);
                            if (view != null) view.updateAppExtensionVersion(version);
                        },
                        throwable -> {
                            endCall();
                            preferences.setPrefChecksummedConnAppExtensionVersion(chksum, null);
                            if (view != null) view.updateAppExtensionVersion(null);
                        }));
    }
}


