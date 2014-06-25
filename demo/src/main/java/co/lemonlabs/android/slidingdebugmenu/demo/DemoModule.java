package co.lemonlabs.android.slidingdebugmenu.demo;

import android.content.Context;
import co.lemonlabs.android.slidingdebugmenu.SlidingDebugMenuModule;
import co.lemonlabs.android.slidingdebugmenu.modules.BuildModule;
import co.lemonlabs.android.slidingdebugmenu.modules.LocationModule;
import co.lemonlabs.android.slidingdebugmenu.modules.LogModule;
import co.lemonlabs.android.slidingdebugmenu.modules.MenuModule;
import co.lemonlabs.android.slidingdebugmenu.modules.NetworkModule;
import dagger.Module;
import dagger.Provides;

import java.util.ArrayList;
import java.util.List;

@Module(
    includes = {
        SlidingDebugMenuModule.class
    },
    injects = {
        DemoActivity.class
    }
)
public class DemoModule {

    private final Context context;

    public DemoModule(Context context) {
        this.context = context;
    }

    @Provides Context provideContext() {
        return context;
    }

    @Provides List<MenuModule> provideMenuModules(BuildModule build, LocationModule location, LogModule log, NetworkModule network) {
        List<MenuModule> modules = new ArrayList<>();

        // Custom, app specific module
        modules.add(new LocaleModule());

        modules.add(build);
        modules.add(location);
        modules.add(log);
        modules.add(network);

        return modules;
    }

}
