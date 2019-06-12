package com.minyushov.keyboard.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.openDark
import kotlinx.android.synthetic.main.activity_main.openLight

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    openDark.setOnClickListener {
      startActivity(
        Intent(this, KeyboardActivity::class.java)
          .putExtra(KeyboardActivity.EXTRA_THEME, R.style.Theme_MaterialComponents)
      )
    }

    openLight.setOnClickListener {
      startActivity(
        Intent(this, KeyboardActivity::class.java)
          .putExtra(KeyboardActivity.EXTRA_THEME, R.style.Theme_MaterialComponents_Light)
      )
    }
  }
}