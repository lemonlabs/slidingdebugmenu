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

package co.lemonlabs.android.slidingdebugmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.lemonlabs.android.slidingdebugmenu.modules.BuildModule;
import co.lemonlabs.android.slidingdebugmenu.modules.LocationModule;
import co.lemonlabs.android.slidingdebugmenu.modules.LogModule;
import co.lemonlabs.android.slidingdebugmenu.modules.MenuModule;
import co.lemonlabs.android.slidingdebugmenu.modules.NetworkModule;
import co.lemonlabs.android.slidingdebugmenu.views.ModuleTitle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SlidingDebugMenu extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "SlidingDebugMenu";

    /**
     * Shared set of module classes that will be loaded when attaching the menu
     * to an Activity.
     */
    private static Set<Class<? extends MenuModule>> modulesToLoad =
            Collections.synchronizedSet(new LinkedHashSet<Class<? extends MenuModule>>());

    /**
     * List of default module classes
     */
    static {
        modulesToLoad.add(NetworkModule.class);
        modulesToLoad.add(LogModule.class);
        modulesToLoad.add(LocationModule.class);
        modulesToLoad.add(BuildModule.class);
    }

    /**
     * Get the set of classes that will be loaded on menu initialization. Editing
     * this set is possible but {@link #edit()} is preferred.
     * <p/>
     * Changes made are only applied when attaching the menu to an Activity
     *
     * @return
     */
    public static Set<Class<? extends MenuModule>> getModulesToLoad() {
        return modulesToLoad;
    }

    /**
     * Edit the set of {@link MenuModule} classes that will load on menu initialization.
     * If using custom modules, they must have an empty public constructor to be added.
     * Call {@link ModuleSetBuilder#commit()} after making changes.
     * <p/>
     * Committed changes are only applied when attaching the menu to an Activity.
     * Otherwise, use {@link #addModule(MenuModule)} and {@link #removeModule(MenuModule)}
     * to modify the menu
     * <p/>
     * TODO: Use a dependency injector to manage modules
     *
     * @return
     */
    public static ModuleSetBuilder edit() {
        return new ModuleSetBuilder();
    }

    /**
     * Attach a new instance of SlidingDebugMenu to an activity with the given Set of
     * {@link MenuModule} classes. Use {@link #edit()} to modify modules to load.
     * <p/>
     * By default the menu will be on the right. To adjust layout parameters, use
     * {@link #attach(android.app.Activity, android.support.v4.widget.DrawerLayout, int)}
     * and provide one of {@link android.view.Gravity} constants.
     *
     * @param context Activity to attach to
     * @param container DrawerLayout to place itself into
     *
     * @return
     */
    public static SlidingDebugMenu attach(Activity context, DrawerLayout container) {
        return attach(context, container, Gravity.RIGHT);
    }

    /**
     * Attach a new instance of SlidingDebugMenu to an activity with the given Set of
     * {@link MenuModule} classes. Use {@link #edit()} to modify modules to load.
     * <p/>
     *
     * @param context Activity to attach to
     * @param container DrawerLayout to place itself into
     * @param gravity Where to place debug layout, e.g. android.view.Gravity.RIGHT
     *
     * @return
     */
    public static SlidingDebugMenu attach(Activity context, DrawerLayout container, int gravity) {
        SlidingDebugMenu sdm = (SlidingDebugMenu) LayoutInflater.from(context).inflate(R.layout.sdm__drawer, container, false);
        ((DrawerLayout.LayoutParams) sdm.getLayoutParams()).gravity = gravity;

        for (Class<? extends MenuModule> moduleClass : modulesToLoad) {
            try {
                sdm.addModule(moduleClass.newInstance(), false);
            } catch (InstantiationException | IllegalAccessException e) {
                final String msg = "Could not add module " + moduleClass.getSimpleName() + ": " + e.getMessage();
                throw new IllegalStateException(msg);
            }
        }

        container.addView(sdm);
        return sdm;
    }

    /**
     * Root layout of the menu
     */
    private ViewGroup mContainer;

    /**
     * Drawer settings layout
     */
    private View mDrawerSettings;

    /**
     * Drawer subtitle
     */
    private TextView mDrawerSubtitle;

    /**
     * List of currently added and visible modules
     */
    private List<MenuModule> mModules;

    /**
     * Map of modules to their views in the menu
     */
    private Map<MenuModule, ModuleViewHolder> mModuleViews;

    public SlidingDebugMenu(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        mContainer = (ViewGroup) findViewById(R.id.sdm__drawer_container);
        mDrawerSettings = findViewById(R.id.sdm__drawer_settings);
        mDrawerSubtitle = (TextView) findViewById(R.id.sdm__drawer_title_description);

        // Set drawer title icon and name
        final ApplicationInfo appInfo = getContext().getApplicationInfo();
        ((ImageView) findViewById(R.id.sdm__drawer_title_icon)).setImageDrawable(appInfo.loadIcon(getContext().getPackageManager()));
        ((TextView) findViewById(R.id.sdm__drawer_title)).setText(appInfo.labelRes);

        // Init drawer settings
        findViewById(R.id.sdm__settings_developer).setOnClickListener(this);
        findViewById(R.id.sdm__settings_battery).setOnClickListener(this);
        findViewById(R.id.sdm__settings_drawer).setOnClickListener(this);
        findViewById(R.id.sdm__settings_uninstall).setOnClickListener(this);
        findViewById(R.id.sdm__settings_app_info).setOnClickListener(this);

        mModules = new ArrayList<>();
        mModuleViews = new HashMap<>();
    }

    /**
     * Add a new module to the end of the menu
     *
     * @param module      module to add
     */
    public void addModule(MenuModule module) {
        addModule(mModules.size(), module);
    }

    /**
     * Add a new module at the specified position of the menu
     *
     * @param position    the index at which to insert
     * @param module      module to add
     */
    public void addModule(int position, MenuModule module) {
        mModules.add(position, module);
        // drawer has a title view and each module has 2 views
        mModuleViews.put(module, addModuleViews(module, position * 2 + 1));
    }

    /**
     * Add module title and content to drawer layout at the given index
     * of the container and return a wrapper of the views
     *
     * @param module module to get views from
     * @param index  position of view root at which to insert
     * @return
     */
    private ModuleViewHolder addModuleViews(MenuModule module, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int botMargin = (int) getResources().getDimension(R.dimen.sdm__module_subtitle_margin);
        final int topMargin = (int) getResources().getDimension(R.dimen.sdm__drawer_module_margin);
        params.setMargins(0, topMargin, 0, botMargin);

        ModuleTitle title = new ModuleTitle(getContext(), module.getTitle());
        View content = module.getContent(getContext());
        mContainer.addView(title, index, params);
        mContainer.addView(content, index + 1);

        return new ModuleViewHolder(title, content);
    }

    /**
     * Remove a module from the menu
     *
     * @param module module to remove
     */
    public void removeModule(MenuModule module) {
        if (mModules.remove(module)) {
            ModuleViewHolder holder = mModuleViews.get(module);
            mContainer.removeView(holder.title);
            mContainer.removeView(holder.content);
            module.onStop();
        }
    }

    /**
     * Remove a module from the menu by its position
     *
     * @param position the index which is to be removed
     */
    public void removeModule(int position) {
        MenuModule module = mModules.get(position);
        if (mModules.remove(module)) {
            ModuleViewHolder holder = mModuleViews.get(module);
            mContainer.removeView(holder.title);
            mContainer.removeView(holder.content);
            module.onStop();
        }
    }

    /**
     * Set visibility of drawer settings at the bottom of the menu
     *
     * @param visible
     */
    public void setDrawerSettingsVisible(boolean visible) {
        mDrawerSettings.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * Set drawer subtitle
     *
     * @param text
     */
    public void setDrawerSubtitle(String text) {
        mDrawerSubtitle.setText(text);
    }

    /**
     * Get a list of currently loaded and visible modules
     *
     * @return
     */
    public List<MenuModule> getLoadedModules() {
        return mModules;
    }

    /**
     * Get a module by its position in the menu
     *
     * @param position
     * @return
     */
    public MenuModule getModule(int position) {
        return mModules.get(position);
    }

    /**
     * Get first occurrence of a module by given class. Returns null otherwise.
     *
     * @param moduleClass instance of module to look for
     * @return
     */
    public MenuModule getModule(Class<? extends MenuModule> moduleClass) {
        for (MenuModule module : mModules) {
            if (moduleClass.isInstance(module)) return module;
        }

        return null;
    }

    /**
     * Removes all modules and calls their {@link MenuModule#onStop()} method
     */
    public void onStop() {
        for (MenuModule module : mModules) {
            module.onStop();
        }
    }

    /**
     * Starts all modules and calls their {@link MenuModule#onStart()} method
     */
    public void onStart() {
        for (MenuModule module : mModules) {
            module.onStart();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        Context context = getContext();

        if (id == R.id.sdm__settings_developer) {
            // open dev settings
            Intent devIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(devIntent, 0);
            if (resolveInfo != null) context.startActivity(devIntent);
            else Toast.makeText(context, "Developer settings not available on device", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.sdm__settings_battery) {
            // try to find an app to handle battery settings
            Intent batteryIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(batteryIntent, 0);
            if (resolveInfo != null) context.startActivity(batteryIntent);
            else Toast.makeText(context, "No app found to handle power usage intent", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.sdm__settings_drawer) {
            // open android settings
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));

        } else if (id == R.id.sdm__settings_app_info) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);

        } else if (id == R.id.sdm__settings_uninstall) {
            // open dialog to uninstall app
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        }
    }


    /**
     * Helper class to edit the modules that will be loaded
     * on initialization of a SlidingDebugMenu instance
     */
    public static class ModuleSetBuilder {

        private Set<Class<? extends MenuModule>> modulesCopy;

        public ModuleSetBuilder() {
            modulesCopy = new LinkedHashSet<>(modulesToLoad);
        }

        /**
         * Add a new {@link MenuModule} class
         *
         * @param moduleClass
         * @return
         */
        public ModuleSetBuilder add(Class<? extends MenuModule> moduleClass) {
            modulesCopy.add(moduleClass);
            return this;
        }

        /**
         * Add a Collection of {@link MenuModule} classes
         *
         * @param moduleClasses
         * @return
         */
        public ModuleSetBuilder addAll(Collection<Class<? extends MenuModule>> moduleClasses) {
            modulesCopy.addAll(moduleClasses);
            return this;
        }

        /**
         * Remove a {@link MenuModule} class
         *
         * @param moduleClass
         * @return
         */
        public ModuleSetBuilder remove(Class<? extends MenuModule> moduleClass) {
            modulesCopy.remove(moduleClass);
            return this;
        }

        /**
         * Clear the set
         *
         * @return
         */
        public ModuleSetBuilder clear() {
            modulesCopy.clear();
            return this;
        }

        /**
         * Apply the changes made
         */
        public void commit() {
            modulesToLoad.clear();
            modulesToLoad.addAll(modulesCopy);
            modulesCopy.clear();
        }
    }

    /**
     * Holder for module views
     */
    public static class ModuleViewHolder {
        public final ModuleTitle title;
        public final View content;

        public ModuleViewHolder(ModuleTitle title, View content) {
            this.title = title;
            this.content = content;
        }
    }
}
