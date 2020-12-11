package uz.fozilbekimomov.mysticker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import uz.fozilbekimomov.mysticker.core.utils.setItemStatusBarColor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setItemStatusBarColor(ContextCompat.getColor(this, R.color.white), true)
    }
}