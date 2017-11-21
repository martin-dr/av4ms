package net.droegemueller.av4ms.core.srv;

import net.droegemueller.av4ms.core.base.BaseView;
import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;

import org.apache.commons.lang3.tuple.Pair;

public interface ConnectionTestView extends BaseView {

    void updateBasicData(MesswerteResponse data);
    void updateAv4msVersion(Pair<Integer,Integer> version);
    void updateAppExtensionVersion(Pair<Integer,Integer> version);

    void showError(Throwable throwable);

    void indicateProgress(int remaining, int total);
}
