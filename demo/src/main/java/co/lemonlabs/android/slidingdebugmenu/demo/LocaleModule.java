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

package co.lemonlabs.android.slidingdebugmenu.demo;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import co.lemonlabs.android.slidingdebugmenu.modules.MenuModule;
import co.lemonlabs.android.slidingdebugmenu.views.ModuleSimpleProperty;

import java.util.Calendar;
import java.util.Locale;

/**
 * Demo custom module
 */
public class LocaleModule extends MenuModule {

    private static final String TITLE = "Locale Information";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public View getContent(Context context) {
        LinearLayout v = new LinearLayout(context);
        v.setOrientation(LinearLayout.VERTICAL);

        Locale defaultLocale = context.getResources().getConfiguration().locale;

        ModuleSimpleProperty locale = new ModuleSimpleProperty(context, "Locale");
        locale.setPropertyValue(defaultLocale.toString());
        v.addView(locale);

        ModuleSimpleProperty date = new ModuleSimpleProperty(context, "Date");
        date.setPropertyValue(Calendar.getInstance(defaultLocale).getTime().toString());
        v.addView(date);

        return v;
    }
}
