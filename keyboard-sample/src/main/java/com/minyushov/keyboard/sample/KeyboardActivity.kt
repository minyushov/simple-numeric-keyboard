package com.minyushov.keyboard.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minyushov.keyboard.sample.databinding.KeyboardActivityBinding

class KeyboardActivity : AppCompatActivity() {
  private lateinit var binding: KeyboardActivityBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(intent.getIntExtra(EXTRA_THEME, R.style.Theme_MaterialComponents))
    super.onCreate(savedInstanceState)

    binding = KeyboardActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.keyboardView.editText = binding.keyboardInputView
  }

  companion object {
    const val EXTRA_THEME = "keyboard-theme"
  }
}