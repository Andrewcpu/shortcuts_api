import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:shortcuts/shortcuts.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  Map shortcuts = Map();
  Map icons = Map();
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    shortcuts = await ShortcutsAPI.getShortcuts("com.spotify.music");
    for(String shortcut in shortcuts.keys){
      Image i = await ShortcutsAPI.getShortcutIcon(packageName: "com.spotify.music", shortcutID: shortcut);
      icons.putIfAbsent(shortcut, () => i);
    }
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await ShortcutsAPI.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> content = [ Text('Running on: $_platformVersion\n')];
    content.addAll(shortcuts.keys.map((e) => ListTile(leading: icons[e], title: Text(shortcuts[e]),)));
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(children: content,),
        ),
      ),
    );
  }
}
