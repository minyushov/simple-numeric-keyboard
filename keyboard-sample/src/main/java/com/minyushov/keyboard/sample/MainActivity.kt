package com.minyushov.keyboard.sample

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.minyushov.keyboard.KeyboardView

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.a_main)

    findViewById<EditText>(R.id.main_edit_text)
      .also { findViewById<KeyboardView>(R.id.main_keyboard).editText = it }
  }
}