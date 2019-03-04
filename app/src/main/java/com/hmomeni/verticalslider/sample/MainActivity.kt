package com.hmomeni.verticalslider.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hmomeni.verticalslider.VerticalSlider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verticalSlider.onProgressChangeListener = object : VerticalSlider.OnSliderProgressChangeListener {
            override fun onChanged(progress: Int, max: Int) {
                progressText.text = "%.0f%%".format(Locale.ENGLISH, progress.toFloat() / max.toFloat() * 100)
            }
        }
    }
}
