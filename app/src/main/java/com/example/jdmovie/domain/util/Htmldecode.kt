package com.example.jdmovie.domain.util

import android.text.Html

fun String.asHtmlText(): String {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
}