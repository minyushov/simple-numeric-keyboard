package com.minyushov.keyboard.sample

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.minyushov.keyboard.KeyboardView
import kotlinx.android.synthetic.main.activity_keyboard.keyboardInputView
import kotlinx.android.synthetic.main.activity_keyboard.keyboardView

class KeyboardActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(intent.getIntExtra(EXTRA_THEME, R.style.Theme_MaterialComponents))
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_keyboard)
    keyboardView.editText = keyboardInputView
  }

  companion object {
    const val EXTRA_THEME = "keyboard-theme"
  }
}