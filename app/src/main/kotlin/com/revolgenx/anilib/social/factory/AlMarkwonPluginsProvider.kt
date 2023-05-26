package com.revolgenx.anilib.social.factory

import io.noties.markwon.AbstractMarkwonPlugin

interface AlMarkwonPluginsProvider {
    fun getPlugins(): List<AbstractMarkwonPlugin>
}