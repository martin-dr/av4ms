package net.droegemueller.av4ms.core.srv;

import net.droegemueller.av4ms.core.base.BaseInteractor;
import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;

import org.apache.commons.lang3.tuple.Pair;

import io.reactivex.Observable;

public interface ServerInteractor extends BaseInteractor {
    Observable<MesswerteResponse> loadBasicData();

    Observable<Pair<Integer, Integer>> loadAv4msBasicVersion();

    Observable<Pair<Integer, Integer>> loadAppExtensionVersion();
}
