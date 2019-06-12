package com.minyushov.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton0
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton1
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton2
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton3
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton4
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton5
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton6
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton7
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton8
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButton9
import kotlinx.android.synthetic.main.v_keyboard.view.keyboardButtonBackspace

class KeyboardView
@JvmOverloads
constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

  var editText: EditText? = null
    @SuppressLint("ClickableViewAccessibility")
    set(value) {
      field = value
      field?.setOnTouchListener(NoSystemKeyboardTouchListener())
    }

  init {
    checkAppCompatTheme(context)

    TypedValue()
      .apply { context.theme.resolveAttribute(R.attr.keyboardTheme, this, true) }
      .resourceId
      .let { ContextThemeWrapper(context, if (it != 0) it else R.style.KeyboardTheme) }
      .also { View.inflate(it, R.layout.v_keyboard, this) }

    keyboardButton0.setOnClickListener(this)
    keyboardButton1.setOnClickListener(this)
    keyboardButton2.setOnClickListener(this)
    keyboardButton3.setOnClickListener(this)
    keyboardButton4.setOnClickListener(this)
    keyboardButton5.setOnClickListener(this)
    keyboardButton6.setOnClickListener(this)
    keyboardButton7.setOnClickListener(this)
    keyboardButton8.setOnClickListener(this)
    keyboardButton9.setOnClickListener(this)
    keyboardButtonBackspace.apply {
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
        selectionStart = Math.max(0, selectionStart - 1)
      }

      editText.text.delete(selectionStart, selectionEnd)
    }
  }

  private fun deleteAllCharacters() {
    val editText = this.editText ?: return
    editText.text.clear()
  }

  private class NoSystemKeyboardTouchListener : OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
      val editText = view as? EditText ?: return false
      // Backup the input type
      val currentInputType = editText.inputType
      // Disable standard keyboard
      editText.inputType = InputType.TYPE_NULL
      // Call native handler
      editText.onTouchEvent(event)
      // Restore input type
      editText.inputType = currentInputType
      // Consume touch event
      return true
    }
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