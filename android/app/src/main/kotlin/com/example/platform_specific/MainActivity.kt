package com.example.platform_specific

import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private var methodChannel: MethodChannel?=null;
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        methodChannel= MethodChannel(flutterEngine.dartExecutor.binaryMessenger,"battery")
        methodChannel!!.setMethodCallHandler{
            call, result ->
            if(call.method=="getBatteryLevel")
            {
                result.success(getBatteryLevel())
            }
            if(call.method=="darkMode")
            {
                print("Dark Mode")
                result.success(setNightMode(context))
            }
            else
                result.notImplemented()
        }

    }
    override fun onDestroy() {
        methodChannel!!.setMethodCallHandler(null)
        super.onDestroy()
    }
    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }
    fun setNightMode(target: Context) {
        val uiManager = target.getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
//        if (VERSION.SDK_INT <= 22) {
//            uiManager.enableCarMode(0)
//        }
//
//        if (state) {
//            uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
//        } else {
//            uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
//        }
    }
}
