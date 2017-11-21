package net.droegemueller.av4ms.core.srv;

import net.droegemueller.av4ms.core.base.BaseView;
import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;

public interface MainActivityView extends BaseView {

    void showBasicData(MesswerteResponse data);

    void showError(Throwable throwable);

    void showProgress();

    void hideProgress();
}
