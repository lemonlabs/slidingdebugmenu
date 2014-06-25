package co.lemonlabs.android.slidingdebugmenu;

import android.content.Context;
import co.lemonlabs.android.slidingdebugmenu.modules.BuildModule;
import co.lemonlabs.android.slidingdebugmenu.modules.LocationModule;
import co.lemonlabs.android.slidingdebugmenu.modules.LogModule;
import co.lemonlabs.android.slidingdebugmenu.modules.MenuModule;
import co.lemonlabs.android.slidingdebugmenu.modules.NetworkModule;
import dagger.Module;
import dagger.Provides;

import java.util.List;

@Module(
    library = true,
    complete = false
)
public class SlidingDebugMenuModule {

    @Provides SlidingDebugMenu provideSlidingDebugMenu(Context context, List<MenuModule> modules) {
        return SlidingDebugMenu.create(context, modules);
    }

    @Provides BuildModule provideBuildModule() {
        return new BuildModule();
    }

    @Provides LocationModule provideLocationModule() {
        return new LocationModule();
    }

    @Provides LogModule provideLogModule() {
        return new LogModule();
    }

    @Provides NetworkModule provideNetworkModule() {
        return new NetworkModule();
    }
}
