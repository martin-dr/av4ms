package net.droegemueller.av4ms.core.srv;

import net.droegemueller.av4ms.core.base.BasePresenter;

public interface ConnectionTestPresenter extends BasePresenter<ConnectionTestView> {
    void doConnectionTest();
}
