package uz.fozilbekimomov.mysticker.core.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/9/20
 * @project MySticker
 */



const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101
const val REQUEST_IMAGE_CAPTURE = 1
const val TAG = "MySticker"
const val PREF_NAME="mysticker.mp3"
const val USER_NAME="user_name"
const val USER_NUMBER="user_number"
const val DATA_KEY = "data"



fun Activity.setItemStatusBarColor(@ColorInt color: Int, darkStatusBarTint: Boolean) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

    val window: Window = (window).also {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        it.statusBarColor = color
    }

    val decor = window.decorView
    if (darkStatusBarTint) {
        decor.systemUiVisibility = decor.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        // We want to change tint color to white again.
        // You can also record the flags in advance so that you can turn UI back completely if
        // you have set other flags before, such as translucent or full screen.
        decor.systemUiVisibility = 0
    }
}
