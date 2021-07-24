import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({
    Key key,
  }) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String batteryLevel;
  static const batteryChannel = const MethodChannel("battery");

  Future<void> getBatteryPercentage() async {
    String batteryPercentage;
    try {
      int battery = await batteryChannel.invokeMethod("getBatteryLevel");
      batteryPercentage = battery.toString();
    } on PlatformException catch (e) {
      print("Failed to get batery percentage$e");
    }
    setState(() {
      batteryLevel = batteryPercentage;
    });
  }

  Future setDarkMode() async {
    try {
      await batteryChannel.invokeMethod("darkMode");
    } on PlatformException catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Battery Level"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Battery Percentage',
            ),
            Text(
              '$batteryLevel',
              style: Theme.of(context).textTheme.headline4,
            ),
            FlatButton(
              onPressed: setDarkMode,
              child: Text("Set night mode"),
            ),
          ],
        ),
      ),

      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          await getBatteryPercentage();
        },
        tooltip: 'Increment',
        child: Icon(Icons.battery_alert),
      ),
    );
  }
}
