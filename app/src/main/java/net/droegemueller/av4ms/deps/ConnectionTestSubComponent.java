package net.droegemueller.av4ms.deps;

import net.droegemueller.av4ms.activities.ConnectionTestActivity;

import dagger.Subcomponent;

/**
 * Created by Mohsen on 20/10/2016.
 */

@ConnectionTestScope
@Subcomponent(modules = {
        ConnectionTestModule.class
})
public interface ConnectionTestSubComponent {

    void inject(ConnectionTestActivity activity);

}
