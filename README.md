# shortcuts

Launcher Shortcuts are the list of actions that come up when you long press on a supporting applications Icon. This plugin allows you to interact with the Android shortcuts API.

It is meant to be used in conjunction with other plugins that allow you to retrieve the application list.


# ShortcutsAPI
- static Future<Map> getShortcuts(String packageName) : Returns a map of the Shortcut ID : Shortcut Text
- static Future<Image> getShortcutIcon({String packageName, String shortcutID, double width = 50, double height = 50}) : Returns an Icon for the shortcut with the defined sizes.
- static void launchShortcut({String packageName, String shortcutID}) : Launches shortcut by ID & packageName

* REQUIRES INTENT FILTER IN ANDROIDMANIFEST.XML, APP MUST BE DEFAULT HOME APP TO BE ABLE TO QUERY FOR SHORTCUTS *
```xml
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
```