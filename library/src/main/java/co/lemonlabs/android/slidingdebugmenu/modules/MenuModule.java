/*
 * Copyright (C) 2014 Lemon Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.lemonlabs.android.slidingdebugmenu.modules;

import android.content.Context;
import android.view.View;

/**
 * Base class for SlidingDebugMenu modules.
 * <p/>
 * Created by balysv on 10/01/14.
 * www.lemonlabs.co
 */
public abstract class MenuModule {

    public MenuModule() {}

    public abstract String getTitle();

    public abstract View getContent(Context context);

    /**
     * Override this method if you need to do
     * some clean up when activity goes to foreground.
     * E.g. unregister receivers
     */
    public void onStop() {
        // do clean up
    }

    /**
     * Override this method if you need to start
     * some processes that would be killed when
     * onStop() is called
     * E.g. register receivers
     */
    public void onStart() {
        // reinitialize stuff
    }
}
