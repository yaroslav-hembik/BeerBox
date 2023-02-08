package it.hembik.domain

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.hideKeyboard() {
    clearFocus()
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String.formatSearchInput(): String {
    return this.replace(" ", "_")
}

fun String.formatFiltersString(): String {
    return this.replace(" & ", "_")
}
