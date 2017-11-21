package net.droegemueller.av4ms.deps;

import net.droegemueller.av4ms.core.srv.ServerInteractor;
import net.droegemueller.av4ms.core.srv.ConnectionTestPresenter;
import net.droegemueller.av4ms.core.srv.ConnectionTestPresenterImpl;
import net.droegemueller.av4ms.core.srv.ServerInteractorImpl;

import dagger.Module;
import dagger.Provides;


@Module
public class ConnectionTestModule {
    @Provides
    @ConnectionTestScope
    public ServerInteractor provideInteractor(ServerInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ConnectionTestScope
    public ConnectionTestPresenter providePresenter(ConnectionTestPresenterImpl presenter) {
        return presenter;
    }
}
