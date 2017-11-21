package net.droegemueller.av4ms.core.util;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler mainThread();

    Scheduler backgroundThread();

}

