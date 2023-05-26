package com.revolgenx.anilib.common.ui.state


sealed class InitializationState{
    object Initializing: InitializationState()
    object Completed: InitializationState()
}