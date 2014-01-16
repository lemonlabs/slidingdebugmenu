SlidingDebugMenu
==================

SlidingDebugMenu is a helper layout for developers that allows easy access to the state of the device and its settings
from anywhere in your application. Use predefined modules or implement your own to suit your development needs!

![Example Image][1]

Including in your project
-------------------------

To be added

Usage
-----

The library supports Android 2.3+ and is currently only compatible with ActionBarCompat.

To use the default settings simply add this to your Activity's onCreate()

    menu = SlidingDebugMenu.attach(this)

and add lifecycle callbacks in your onStop() and onStart().

    @Override
    public void onStop() {
        menu.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        menu.onStart();
    }

Default modules
---------------

<i>Network module</i> allows switching state of your wifi, mobile networks and bluetooth provided your device
supports them and display their current state.

<i>Log module</i> controls which level of log should be output, provided you use our Log wrapper class.

<i>Location module</i> allows to easily retrieve current location and its parameters, access location settings
and show your location in a map. This module uses GooglePlayServices, so it will not work on devices without it.

<i>Build module</i> provides some basic info about the installed application.

Customization
-------------

To create a custom module extend MenuModule class and override getTitle() and getContent() methods. Optionally,
you should override onStart() and onStop() if your module depends on the lifecycle of an Activity, like registering
broadcast receivers, an EventBus, etc.

To attach a custom module to the debug drawer you can use one of:

    menu.addModule(MenuModule module, boolean callOnStart)
    menu.addModule(int position, MenuModule module, boolean callOnStart)
after attaching the drawer to an activity. Modules added this way will not persist after configuration changes or
reinitialization of the menu.

OR

Use a static module editor before attaching it to the activity.

    SlidingDebugMenu.edit()
        .add(CustomModule.class)
        .remove(BuildModule.class)
        .commit();

Using this method will automatically reinitialize every module on reinitialization or they could be shared between
multiple activities.

NOTE: Reflection is used to instantiate the modules, so currently only empty constructors are allowed. A dependency
injection framework like Dagger would be useful in future releases.


See javadocs and examples for further customization of the menu.


Acknowledgements
--------------------
* [jfeinstein10/SlidingMenu][3] for the core layout used in the project
* [This presentation by Jake Wharton][4] for inspiration because we could not for his implementation and made our own.

Developed By
--------------------
[Lemon Labs][5] - <team@lemonlabs.lt>

License
-----------

    Copyright 2014 Lemon Labs

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[Ionicons][6] is licensed under the [MIT license](http://opensource.org/licenses/MIT).

[1]: https://raw.github.com/lemonlabs/slidingdebugmenu/master/images/image1.png
[3]: https://github.com/jfeinstein10/SlidingMenu
[4]: https://speakerdeck.com/jakewharton/android-apps-with-dagger
[5]: http://www.lemonlabs.co
[6]: http://ionicons.com/
=======
