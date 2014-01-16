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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;

import co.lemonlabs.android.slidingdebugmenu.R;
import co.lemonlabs.android.slidingdebugmenu.views.ModuleSimpleProperty;

/**
 * Module that provides basic information about
 * the running application.
 */
public class BuildModule extends MenuModule {

    private static final String TITLE = "Build Information";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public View getContent(Context context) {
        LinearLayout v = new LinearLayout(context);
        v.setOrientation(LinearLayout.VERTICAL);


        ModuleSimpleProperty name = new ModuleSimpleProperty(context, R.string.sdm__module_build_name);
        v.addView(name);

        ModuleSimpleProperty version = new ModuleSimpleProperty(context, R.string.sdm__module_build_version);
        v.addView(version);

        ModuleSimpleProperty packageName = new ModuleSimpleProperty(context, R.string.sdm__module_build_package);
        v.addView(packageName);

        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version.setPropertyValue(String.valueOf(info.versionCode));
            name.setPropertyValue(info.versionName);
            packageName.setPropertyValue(info.packageName);
        } catch (PackageManager.NameNotFoundException e) {

        }

        return v;
    }
}
