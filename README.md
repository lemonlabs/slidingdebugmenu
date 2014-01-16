SlidingDebugMenu
==================

SlidingDebugMenu is a helper layout for developers that allows easy access to the state of the device and its settings
from anywhere in your application. Use predefined modules or implements your own to suit your development needs!

![Example Image][1]

Including in your project
-------------------------

The library supports the Android gradle build system. Create a `libs` folder in your project and copy
`slidingdebugmenu-0.1.0.aar` to it.

Add that folder to your repositories in `build.gradle`

    repositories {
        flatDir {
            dirs 'libs'
        }
    }

and add the dependency

    dependencies {
        compile 'co.lemonlabs:slidingdebugmenu:0.1.0@aar'
    }

or you can check the demo code.

Usage
-----

The library supports Android 2.3+.



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
