package uz.fozilbekimomov.mysticker.core

import android.app.Application
import androidx.multidex.MultiDex


/**
 * Created by <a href="mailto: fozilbekimomov@gmail.com" >Fozilbek Imomov</a>
 *
 * @author fozilbekimomov
 * @version 1.0
 * @date 12/9/20
 * @project MySticker
 */


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}