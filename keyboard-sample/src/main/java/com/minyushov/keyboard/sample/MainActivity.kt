package com.minyushov.keyboard.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minyushov.keyboard.sample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
  private lateinit var binding: MainActivityBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.openDark.setOnClickListener {
      startActivity(
        Intent(this, KeyboardActivity::class.java)
          .putExtra(KeyboardActivity.EXTRA_THEME, R.style.Theme_MaterialComponents)
      )
    }

    binding.openLight.setOnClickListener {
      startActivity(
        Intent(this, KeyboardActivity::class.java)
          .putExtra(KeyboardActivity.EXTRA_THEME, R.style.Theme_MaterialComponents_Light)
      )
    }
  }
}