package net.droegemueller.av4ms.deps;

import net.droegemueller.av4ms.activities.ConnectionTestActivity;
import net.droegemueller.av4ms.activities.MainActivity;

import dagger.Subcomponent;

@ConnectionTestScope
@Subcomponent(modules = {
        MainActivityModule.class
})
public interface MainActivitySubComponent {
    void inject(MainActivity activity);
}
