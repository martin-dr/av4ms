package net.droegemueller.av4ms.core.srv;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServerAccessModule {

    @Provides
    @Singleton
    public ServerInteractor provideInteractor(ServerInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @Singleton
    public ConnectionTestPresenter provideConnectionTestPresenter(ConnectionTestPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @Singleton
    public MainActivityPresenter provideMainActivityPresenter(MainActivityPresenterImpl presenter) {
        return presenter;
    }

}
