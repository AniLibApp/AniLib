package com.revolgenx.anilib.radio.repository.room

fun RadioStation.getDefaultStream(): RadioChannel {
    return channel.first { it.default }
}

fun RadioStation.updatePlayState() {
    playbackStateListener?.invoke(playbackState)
}
