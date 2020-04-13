package com.revolgenx.anilib.preference

import android.content.Context

const val EXO_ORIENTATION_KEY = "exo_orientation_key"
fun orientation(context: Context,value:Int) = context.putInt(EXO_ORIENTATION_KEY, value)
fun orientation(context: Context) = context.getInt(EXO_ORIENTATION_KEY, 10)