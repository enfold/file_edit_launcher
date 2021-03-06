package com.enfold.file_edit_launcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.io.File

/** FileEditLauncherPlugin */



/** This is a Flutter Plugin that opens a file chooser allowing the user to edit and view the passed file. */

class FileEditLauncherPlugin : MethodCallHandler, FlutterPlugin, ActivityAware, PluginRegistry.ActivityResultListener  {

  companion object{
    const val CHANNEL_NAME = "file_edit_launcher"
  }

  private var context: Context? = null
  private var activity: Activity? = null
  private var channel: MethodChannel? = null
  private var result: Result? = null
  private var filePath: String? = null
  private var file : File? = null
  private var typeString: String? = null
  private var isResultSubmitted = false

  //Called when the plugin is attached to the FlutterEngine. We retrieve the application context here used
  //for launching the intent in startActivity()
  override fun onAttachedToEngine(binding: FlutterPluginBinding) {
    channel = MethodChannel(
      binding.binaryMessenger, CHANNEL_NAME
    )
    context = binding.applicationContext
    channel!!.setMethodCallHandler(this)
  }

  //This plugin is ActivityAware allowing us to overrides the onActivityResult(). Here we register that listener.
  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }


  override fun onMethodCall(call: MethodCall, result: Result) {
    isResultSubmitted = false
    if (call.method == "launch_file_editor") {
      this.result = result

      filePath = call.argument("file_path")
      if(filePath != null) {
        file = File(filePath!!)

            if (!isFileAvailable) {
              return
            }

            startActivity()
      } else {
        result.error("File Missing", "The file path provided points to a file that doesn't exist.", null)
      }
    } else {
      result.notImplemented()
      isResultSubmitted = true
    }
  }

  //This code needs to be kept for the refactor to support api:30+
  /*
  private fun pathRequiresPermission(): Boolean {
    return try {
      val appDirCanonicalPath = File(context!!.applicationInfo.dataDir).canonicalPath
      val fileCanonicalPath = file!!.canonicalPath
      !fileCanonicalPath.startsWith(appDirCanonicalPath)
    } catch (e: IOException) {
      e.printStackTrace()
      true
    }
  }

  private val isMediaStorePath: Boolean
    get() {
      var isMediaStorePath = false
      val mediaStorePath = arrayOf(
        "/DCIM/",
        "/Pictures/",
        "/Movies/",
        "/Alarms/",
        "/Audiobooks/",
        "/Music/",
        "/Notifications/",
        "/Podcasts/",
        "/Ringtones/",
        "/Download/"
      )
      for (s in mediaStorePath) {
        if (filePath!!.contains(s)) {
          isMediaStorePath = true
          break
        }
      }
      return isMediaStorePath
    }
*/

  private val isFileAvailable: Boolean
    get() {
      if (filePath == null) {
        result?.error("File Path Null", "the file path cannot be null", null)
        return false
      }
      if (!file?.exists()!!) {
        result!!.error("File Missing", "the $filePath file does not exists", null)
        return false
      }
      return true
    }
  
  private fun startActivity() {
    if (!isFileAvailable) {
      return
    }
    val intent = Intent(Intent.ACTION_EDIT)
    intent.addCategory(Intent.CATEGORY_DEFAULT)

    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

    val packageName = context!!.packageName
    val uri = FileProvider.getUriForFile(
      context!!,
      "$packageName.fileProvider", file!!
      )
    intent.setDataAndType(uri, typeString)
    val chooser = Intent.createChooser(intent, "Pick Editor")
    activity!!.startActivityForResult(chooser, 178563)
  }

  override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
    if (channel == null) {

      return
    }
    channel!!.setMethodCallHandler(null)
    channel = null

  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {}

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if(requestCode == 178563) {
      result!!.success("Done Editing")

      return true
    }
    return false
  }

}
