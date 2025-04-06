package com.wzh.base.util

import android.text.Html

fun getHtmlText(text: String): String {
    return if (AndroidVersion.hasNougat()) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        text
    }
}

