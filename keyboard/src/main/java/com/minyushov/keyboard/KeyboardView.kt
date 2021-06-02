package com.minyushov.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.minyushov.keyboard.databinding.KeyboardViewBinding
import kotlin.math.max

class KeyboardView
@JvmOverloads
constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

  private val binding: KeyboardViewBinding

  var editText: EditText? = null
    @SuppressLint("ClickableViewAccessibility")
    set(value) {
      field = value
      field?.showSoftInputOnFocus = false
    }

  init {
    checkAppCompatTheme(context)

    val theme = TypedValue()
      .apply { context.theme.resolveAttribute(R.attr.keyboardTheme, this, true) }
      .resourceId

    val themedContext = ContextThemeWrapper(context, if (theme != 0) theme else R.style.KeyboardTheme)
    binding = KeyboardViewBinding.inflate(LayoutInflater.from(themedContext), this)

    binding.keyboardButton0.setOnClickListener(this)
    binding.keyboardButton1.setOnClickListener(this)
    binding.keyboardButton2.setOnClickListener(this)
    binding.keyboardButton3.setOnClickListener(this)
    binding.keyboardButton4.setOnClickListener(this)
    binding.keyboardButton5.setOnClickListener(this)
    binding.keyboardButton6.setOnClickListener(this)
    binding.keyboardButton7.setOnClickListener(this)
    binding.keyboardButton8.setOnClickListener(this)
    binding.keyboardButton9.setOnClickListener(this)
    binding.keyboardButtonBackspace.apply {
      setOnClickListener(this@KeyboardView)
      setOnLongClickListener {
        deleteAllCharacters()
        true
      }
    }
  }

  override fun onClick(view: View) {
    when (view.id) {
      R.id.keyboardButtonBackspace -> deleteLastCharacter()
      else -> {
        val textView = view as? TextView
        if (textView != null) {
          appendCharacter(textView.text.toString())
        }
      }
    }
  }

  private fun appendCharacter(char: String) {
    val editText = this.editText ?: return
    if (char.isNotEmpty()) {
      val start = editText.selectionStart
      val end = editText.selectionEnd
      if (start == end) {
        editText.text.insert(end, char)
      } else if (end > start) {
        editText.text.replace(start, end, char)
      }
    }
  }

  private fun deleteLastCharacter() {
    val editText = this.editText ?: return
    if (editText.text.isEmpty()) {
      editText.setText("")
    } else {
      var selectionStart: Int
      val selectionEnd: Int

      if (editText.isTextSelectable) {
        selectionStart = editText.selectionStart
        selectionEnd = editText.selectionEnd
      } else {
        selectionEnd = editText.text.length
        selectionStart = selectionEnd
      }

      if (selectionStart == selectionEnd) {
        selectionStart = max(0, selectionStart - 1)
      }

      if (selectionEnd >= selectionStart) {
        editText.text.delete(selectionStart, selectionEnd)
      }
    }
  }

  private fun deleteAllCharacters() {
    val editText = this.editText ?: return
    editText.text.clear()
  }

  companion object {
    private val APPCOMPAT_CHECK_ATTRS = intArrayOf(androidx.appcompat.R.attr.colorPrimary)

    private fun checkAppCompatTheme(context: Context) {
      val array = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS)
      val failed = !array.hasValue(0)
      array.recycle()
      if (failed) {
        throw IllegalArgumentException("You need to use a Theme.AppCompat theme " + "(or descendant).")
      }
    }
  }
}