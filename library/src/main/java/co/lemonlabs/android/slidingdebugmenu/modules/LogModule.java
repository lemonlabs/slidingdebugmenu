/*
 * Copyright (C) 2013 Lemon Labs
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import co.lemonlabs.android.slidingdebugmenu.R;
import co.lemonlabs.android.slidingdebugmenu.util.Log;
import co.lemonlabs.android.slidingdebugmenu.views.ModuleSpinnerProperty;

/**
 * Module to change Log level during runtime using
 * {@link co.lemonlabs.android.slidingdebugmenu.util.Log}.
 */
public class LogModule extends MenuModule {

    private static final String[] LOG_LEVELS = new String[]{"Verbose", "Debug", "Info", "Warn", "Error", "WTF"};
    private static final String TITLE = "Logging";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public View getContent(Context context) {
        LinearLayout v = new LinearLayout(context);
        v.setOrientation(LinearLayout.VERTICAL);

        ModuleSpinnerProperty levelProperty = new ModuleSpinnerProperty(context, R.string.sdm__module_log_level);
        v.addView(levelProperty);

        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(
                context,
                R.layout.sdm__spinner_dropdown_item,
                LOG_LEVELS
        );

        levelProperty.setSpinnerAdapter(levelAdapter);
        levelProperty.setOnSpinnerItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // const value for VERBOSE is 2
                Log.LOG_LEVEL = position + 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return v;
    }
}
