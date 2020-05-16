import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class ShortcutsAPI {
  static const MethodChannel _channel =
      const MethodChannel('shortcuts');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<Map> getShortcuts(String packageName) async{
    final Map shortcuts = await _channel.invokeMethod('getShortcuts', {'packageName': packageName});
    return shortcuts;

  }

  static Future<Image> getShortcutIcon({String packageName, String shortcutID, double width = 50, double height = 50}) async
  {
    var args= {'packageName': packageName, 'shortcutID': shortcutID};
    final Uint8List icon = await _channel.invokeMethod("getShortcutIcon", args);
    return Image.memory(icon, width: width, height: height,);
  }

  static void launchShortcut({String packageName, String shortcutID}) {
    var args = {'packageName': packageName, 'shortcutID': shortcutID};
    _channel.invokeMethod("launchShortcut", args);
  }



}
