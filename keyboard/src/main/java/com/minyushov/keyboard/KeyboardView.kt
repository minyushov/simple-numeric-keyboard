package com.minyushov.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView

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

    findViewById<View>(R.id.keyboard_0).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_1).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_2).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_3).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_4).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_5).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_6).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_7).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_8).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_9).setOnClickListener(this)
    findViewById<View>(R.id.keyboard_del).also {
      it.setOnClickListener(this)
      it.setOnLongClickListener { _ ->
        deleteAllCharacters()
        true
      }
    }
  }

  override fun onClick(view: View) {
    when (view.id) {
      R.id.keyboard_del -> deleteLastCharacter()
      else -> {
        val textView = view as? TextView
        if (textView != null) {
          appendCharacter(textView.text.toString())
        }
      }
    }
  }

  private fun appendCharacter(char: String) =
    editText
      ?.takeIf { char.isNotEmpty() }
      ?.also {
        val start = it.selectionStart
        val end = it.selectionEnd
        if (start == end) {
          it.text.insert(end, char)
        } else if (end > start) {
          it.text.replace(start, end, char)
        }
      }

  private fun deleteLastCharacter() =
    editText
      ?.also {
        if (it.text.isEmpty()) {
          it.setText("")
        } else {
          var selectionStart: Int
          val selectionEnd: Int

          if (it.isTextSelectable) {
            selectionStart = it.selectionStart
            selectionEnd = it.selectionEnd
          } else {
            selectionEnd = it.text.length
            selectionStart = selectionEnd
          }

          if (selectionStart == selectionEnd) {
            selectionStart = Math.max(0, selectionStart - 1)
          }

          it.text.delete(selectionStart, selectionEnd)
        }
      }

  private fun deleteAllCharacters() =
    editText
      ?.also { it.text.clear() }

  private class NoSystemKeyboardTouchListener : View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent) =
      view
        .let { view as? EditText }
        ?.run {
          // Backup the input type
          val currentInputType = inputType
          // Disable standard keyboard
          inputType = InputType.TYPE_NULL
          // Call native handler
          onTouchEvent(event)
          // Restore input type
          inputType = currentInputType
          // Consume touch event
          true
        } ?: false
  }

  companion object {
    private val APPCOMPAT_CHECK_ATTRS = intArrayOf(android.support.v7.appcompat.R.attr.colorPrimary)

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