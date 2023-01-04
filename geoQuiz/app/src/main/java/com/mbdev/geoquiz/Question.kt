package com.mbdev.geoquiz
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var wasPreviouslyAsked: Boolean = false )