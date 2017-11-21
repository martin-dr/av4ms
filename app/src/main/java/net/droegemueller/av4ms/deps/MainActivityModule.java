package net.droegemueller.av4ms.deps;

import net.droegemueller.av4ms.core.srv.ConnectionTestPresenter;
import net.droegemueller.av4ms.core.srv.ConnectionTestPresenterImpl;
import net.droegemueller.av4ms.core.srv.MainActivityPresenter;
import net.droegemueller.av4ms.core.srv.MainActivityPresenterImpl;
import net.droegemueller.av4ms.core.srv.ServerInteractor;
import net.droegemueller.av4ms.core.srv.ServerInteractorImpl;

import dagger.Module;
import dagger.Provides;


@Module
public class MainActivityModule {
    @Provides
    @MainActivityScope
    public ServerInteractor provideInteractor(ServerInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @MainActivityScope
    public MainActivityPresenter providePresenter(MainActivityPresenterImpl presenter) {
        return presenter;
    }
}
