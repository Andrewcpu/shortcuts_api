package net.andrewcpu.shortcuts

import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


/** ShortcutsPlugin */
class ShortcutsPlugin : FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel: MethodChannel
  private lateinit var shortcutAPI: ShortcutAPI
  private lateinit var mContext: Context

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "shortcuts")
    channel.setMethodCallHandler(this)
    mContext = flutterPluginBinding.applicationContext
    shortcutAPI = ShortcutAPI(mContext)
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "shortcuts")
      channel.setMethodCallHandler(ShortcutsPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "getShortcuts") {
      var packageName: String = call.argument<String>("packageName")!!
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        var map: MutableMap<String, String> = mutableMapOf<String, String>()
        for (i in shortcutAPI.getShortcutFromApp(packageName)) {
          map.put(i.id, i.label)
        }
        result.success(map)
      } else {
        result.error("1", "Cannot get shortcuts.", "Not up to date.")
      }
    } else if (call.method == "getShortcutIcon") {
      var packageName: String = call.argument<String>("packageName")!!
      var shortcutID: String = call.argument<String>("shortcutID")!!
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        result.success(shortcutAPI.getShortcutIconFromApp(packageName, shortcutID))
      } else
        result.error("1", "Cannot get shortcuts.", "Not up to date.")
    } else if (call.method == "launchShortcut") {
      var packageName: String = call.argument<String>("packageName")!!
      var shortcutID: String = call.argument<String>("shortcutID")!!
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        shortcutAPI.startShortcut(packageName, shortcutID)
      } else {
        result.error("1", "Cannot get shortcuts.", "Not up to date.")
      }
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
