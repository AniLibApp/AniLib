package com.revolgenx.anilib.radio.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.media.MediaBrowserServiceCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.CloseableStaticBitmap
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.metadata.icy.IcyInfo
import com.revolgenx.anilib.R
import com.revolgenx.anilib.radio.data.PlaybackState
import com.revolgenx.anilib.radio.repository.room.getDefaultStream
import com.revolgenx.anilib.radio.source.RadioChildrenType
import com.revolgenx.anilib.radio.source.RadioStationSource
import com.revolgenx.anilib.util.immutableFlagEmpty
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.atomic.AtomicBoolean

class RadioService : MediaBrowserServiceCompat(),
    Player.EventListener,
    LifecycleOwner,
    KoinComponent {


    companion object {

        const val NOW_PLAYING_CHANNEL_ID =
            "com.revolgenx.anilib.radio.service.notification.RadioService"
        const val FOREGROUND_NOTIFICATION_ID = 0xb138
        const val RADIO_CHANNEL_NAME = "AniLib Radio"

        const val MEDIA_ID_EXTRA_KEY = "com.revolgenx.anilib.radio.service.MEDIA_ID_EXTRA_KEY"
        const val PLAY_ACTION_KEY = "com.revolgenx.anilib.radio.service.ACTION_PLAY"
        const val PAUSE_ACTION_KEY = "com.revolgenx.anilib.radio.service.ACTION_PAUSE"
        const val STOP_ACTION_KEY = "com.revolgenx.anilib.radio.service.ACTION_STOP"
        const val NEXT_ACTION_KEY = "com.revolgenx.anilib.radio.service.ACTION_NEXT"
        const val PREVIOUS_ACTION_KEY = "com.revolgenx.anilib.radio.service.ACTION_PREVIOUS"
    }


    private var _exoPlayer: SimpleExoPlayer? = null
    private val exoPlayer get() = _exoPlayer!!
    private val playerIsNull get() = _exoPlayer == null
    private val currentStationIsNull get() = currentRadioStation.value == null

    private val audioAttributes by lazy {
        AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build()
    }


    private val mDispatcher: ServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    private lateinit var mediaSession: MediaSessionCompat

    private val radioSource by inject<RadioStationSource>()

    private val currentRadioStation get() = radioSource.currentRadioStation
    private val playBackState get() = radioSource.playbackState


    //notification
    private lateinit var notificationManager: NotificationManager
    private var notification: Notification? = null
    private val hasNotificationBitmapCache: AtomicBoolean = AtomicBoolean(false)

    private val notificationIconJob = SupervisorJob()
    private val stationIconScope = CoroutineScope(Dispatchers.Main + notificationIconJob)


    private val playStateObserver = Observer<PlaybackState> {
        when (it) {
            is PlaybackState.RadioPlayState -> {
                makeDefaultNotification()
            }
            is PlaybackState.RadioStopState -> {
                makeDefaultNotification()
            }
            is PlaybackState.RadioBufferingState -> {
                makeDefaultNotification()
            }
        }
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    @Suppress("DEPRECATION")
    override fun onStart(intent: Intent?, startId: Int) {
        mDispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent == null && intent?.action == null) {
            stopServiceIfIdle()
            return START_NOT_STICKY
        }

        handleActionMessage(intent)

        //create notification
        makeDefaultNotification()

        //start foreground
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)

        return START_STICKY
    }

    private fun handleActionMessage(intent: Intent) {
        when (intent.action) {
            PLAY_ACTION_KEY -> {
                var mediaId = intent.getLongExtra(MEDIA_ID_EXTRA_KEY, -1)

                val isDifferentStation =
                    mediaId != -1L && currentRadioStation.value != null && mediaId != currentRadioStation.value!!.id

                if (mediaId == -1L) {
                    if (currentRadioStation.value != null) {
                        mediaId = currentRadioStation.value!!.id
                    } else {
                        return
                    }
                }

                if (isDifferentStation) {
                    stopCurrentPlayer()
                    radioSource.setCurrentRadioStation(mediaId)
                    playCurrentStation()
                } else {
                    if (radioSource.playbackState.value is PlaybackState.RadioPlayState || radioSource.playbackState.value is PlaybackState.RadioBufferingState) {
                        stopCurrentPlayback()
                    } else {
                        stopCurrentPlayer()
                        radioSource.setCurrentRadioStation(mediaId)
                        playCurrentStation()
                    }
                }

            }
            PAUSE_ACTION_KEY -> {
                stopCurrentPlayer()
            }
            STOP_ACTION_KEY -> {
                stopCurrentPlayback()
            }
            NEXT_ACTION_KEY -> {
                playNextStation()
            }
            PREVIOUS_ACTION_KEY -> {
                playPreviousStation()
            }
        }
    }

    private fun stopCurrentPlayback() {
        if (_exoPlayer != null) {
            stopCurrentPlayer()
            stopServiceIfIdle()
        }
    }

    override fun onCreate() {
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()


        //create session
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, immutableFlagEmpty)
            }

        mediaSession = MediaSessionCompat(this, RadioService::class.java.simpleName).apply {
            setSessionActivity(sessionActivityPendingIntent)
            setCallback(mediaSessionCallback)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken


        //observe playlist type
        radioSource.radioChildrenType.observe(this) {
            notifyChildrenChanged(RadioService::class.java.simpleName)
        }

        radioSource.favouriteRadioStations.observe(this) {
            if (radioSource.radioChildrenType.value == RadioChildrenType.FAVOURITE) {
                notifyChildrenChanged(RadioService::class.java.simpleName)
            }
        }

        currentRadioStation.observe(this) {
            if (it.streamTitle != null) {
                notifyDefaultNotification()
            }
        }

        //notification manager
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //register notification channel
        makeNotificationChannel()


        //create player
        _exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            addListener(this@RadioService)
            setAudioAttributes(this@RadioService.audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addMetadataOutput {
                val entry = it.get(0)
                if (entry is IcyInfo) {
                    currentRadioStation.value = currentRadioStation.value?.also {
                        it.streamTitle = entry.title
                    }
                }
            }
        }

        radioSource.currentStationChannelChangeListener = {
            stopCurrentPlayer()
            playCurrentStation()
        }

        playBackState.observe(this, playStateObserver)
    }


    //#region notification
    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            NOW_PLAYING_CHANNEL_ID,
            RADIO_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.setShowBadge(true)
        channel.setSound(null, null)

        notificationManager.createNotificationChannel(channel)

    }

    private fun notifyDefaultNotification() {
        makeDefaultNotification()
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    private fun makeDefaultNotification() {
        notification = getDefaultNotificationBuilder()?.build()
        makeDefaultNotificationWithIcon()
    }

    private fun makeDefaultNotificationWithIcon() {
        if (hasNotificationBitmapCache.get()) return
        val iconUrl = currentRadioStation.value?.logo ?: return
        val notificationBuilder = getDefaultNotificationBuilder() ?: return

        stationIconScope.launch {
            withContext(Dispatchers.IO) {
                val dataSource = Fresco.getImagePipeline().fetchDecodedImage(
                    ImageRequestBuilder.newBuilderWithSource(iconUrl.toUri())
                        .setImageDecodeOptions(
                            ImageDecodeOptions.newBuilder()
                                .setForceStaticImage(true).build()
                        )
                        .setLocalThumbnailPreviewsEnabled(true)
                        .build(),
                    this@RadioService
                )
                try {
                    val result =
                        DataSources.waitForFinalResult<CloseableReference<CloseableImage>>(
                            dataSource
                        )
                    if (result != null) {
                        notificationBuilder.setLargeIcon(
                            (result.get() as CloseableStaticBitmap).underlyingBitmap.copy(
                                (result.get() as CloseableStaticBitmap).underlyingBitmap.config,
                                true
                            )
                        )
                        notification = notificationBuilder.build()
                        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
                    }
                } finally {
                    dataSource.close()
                }
            }
        }
    }

    private fun getDefaultNotificationBuilder(): NotificationCompat.Builder? {
        val currentRadioStation = currentRadioStation.value ?: return null

        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    sessionIntent,
                    immutableFlagEmpty
                )
            }

        val contentText = currentRadioStation.streamTitle ?: ""
        val isBuffering = playBackState.value is PlaybackState.RadioBufferingState
        val streamTitle = getString(R.string.notification_content_text).format(
            if (isBuffering) getString(R.string.buffering) else "",
            contentText
        )

        return NotificationCompat.Builder(this, NOW_PLAYING_CHANNEL_ID)
            .setContentTitle(currentRadioStation.name)
            .setContentText(streamTitle)
            .setSmallIcon(R.drawable.ic_anilib_inline)
            .setContentIntent(sessionActivityPendingIntent)
            .setWhen(System.currentTimeMillis()).apply {

                val iconUrl = currentRadioStation.logo
                if (iconUrl != null) {
                    buildNotificationWithIconIfPresent(iconUrl, this)
                }

                addAction(R.drawable.ic_close, getString(R.string.close), makeClosePendingIntent())
                addAction(
                    R.drawable.ic_skip_previous,
                    getString(R.string.previous),
                    makePreviousPendingIntent()
                )
                when (playBackState.value) {
                    is PlaybackState.RadioPlayState, is PlaybackState.RadioBufferingState -> {
                        addAction(
                            R.drawable.ic_pause,
                            getString(R.string.pause),
                            makePausePendingIntent()
                        )
                    }
                    else -> {
                        addAction(
                            R.drawable.ic_play,
                            getString(R.string.play),
                            makePlayPendingIntent()
                        )
                    }
                }
                addAction(
                    R.drawable.ic_skip_next,
                    getString(R.string.next),
                    makeNextPendingIntent()
                )
            }
            .setStyle(
                MediaStyle()
                    .setShowActionsInCompactView(1, 2, 3)
                    .setMediaSession(sessionToken)
            )
    }

    private fun buildNotificationWithIconIfPresent(
        iconUrl: String,
        builder: NotificationCompat.Builder
    ) {
        hasNotificationBitmapCache.set(false)
        val request = ImageRequestBuilder.newBuilderWithSource(iconUrl.toUri())
            .setImageDecodeOptions(
                ImageDecodeOptions.newBuilder()
                    .setForceStaticImage(true).build()
            )
            .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.DISK_CACHE)
            .build()

        val cacheKey = DefaultCacheKeyFactory.getInstance()
            .getEncodedCacheKey(request, null)
        val mainCache = ImagePipelineFactory.getInstance()
            .mainFileCache

        if (mainCache.hasKey(cacheKey)) {
            val radioBitmapFile =
                (mainCache.getResource(cacheKey) as? FileBinaryResource)?.file ?: return

            val radioBitmap = BitmapFactory.decodeFile(radioBitmapFile.absolutePath) ?: return

            hasNotificationBitmapCache.set(true)
            notificationIconJob.cancel()
            builder.setLargeIcon(radioBitmap)
        }
    }

    private fun makePlayPendingIntent(): PendingIntent? {
        val intent = Intent(PLAY_ACTION_KEY)
        intent.component = ComponentName(this, RadioService::class.java)
        return PendingIntent.getService(this, 1, intent, immutableFlagEmpty)
    }

    private fun makePausePendingIntent(): PendingIntent? {
        val intent = Intent(PAUSE_ACTION_KEY)
        intent.component = ComponentName(this, RadioService::class.java)
        return PendingIntent.getService(this, 2, intent, immutableFlagEmpty)
    }

    private fun makeClosePendingIntent(): PendingIntent? {
        val intent = Intent(STOP_ACTION_KEY)
        intent.component = ComponentName(this, RadioService::class.java)
        return PendingIntent.getService(this, 3, intent, immutableFlagEmpty)
    }

    private fun makePreviousPendingIntent(): PendingIntent? {
        val intent = Intent(PREVIOUS_ACTION_KEY)
        intent.component = ComponentName(this, RadioService::class.java)
        return PendingIntent.getService(this, 4, intent, immutableFlagEmpty)
    }

    private fun makeNextPendingIntent(): PendingIntent? {
        val intent = Intent(NEXT_ACTION_KEY)
        intent.component = ComponentName(this, RadioService::class.java)
        return PendingIntent.getService(this, 5, intent, immutableFlagEmpty)
    }

    //#endregion

    override fun onBind(intent: Intent?): IBinder? {
        mDispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }


    private fun stopServiceIfIdle() {
        notificationIconJob.cancel()
        stopForeground(true)
        stopSelf()
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopCurrentPlayback()
    }

    override fun onDestroy() {
        radioSource.currentStationChannelChangeListener = null
        mDispatcher.onServicePreSuperOnDestroy()
        notificationIconJob.cancel()
        super.onDestroy()
        mediaSession.run {
            isActive = false
            release()
        }
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer.setWakeMode(C.WAKE_MODE_NONE)
        exoPlayer.removeListener(this)
        exoPlayer.stop()
        exoPlayer.release()
        _exoPlayer = null
    }

    private fun playCurrentStation() {
        //play player from current radio station
        if (playerIsNull || currentStationIsNull) return
        exoPlayer.setMediaItem(MediaItem.fromUri(currentRadioStation.value!!.getDefaultStream().stream))
        exoPlayer.prepare()
        exoPlayer.play()
    }

    private fun playNextStation() {
        stopCurrentPlayer()
        radioSource.gotToNextStation()
        playCurrentStation()
    }

    private fun playPreviousStation() {
        stopCurrentPlayer()
        radioSource.goToPreviousStation()
        playCurrentStation()
    }

    private fun stopCurrentPlayer() {
        //stop player
        if (playerIsNull) return
        exoPlayer.stop()
        exoPlayer.setWakeMode(C.WAKE_MODE_NONE)
    }

    private fun setPlaybackState(state: PlaybackState) {
        playBackState.value = state
    }

    override fun onPlaybackStateChanged(@Player.State state: Int) {
        when (state) {
            Player.STATE_BUFFERING -> {
                setPlaybackState(PlaybackState.RadioBufferingState(currentRadioStation.value!!))
            }
            Player.STATE_ENDED -> {
                setPlaybackState(PlaybackState.RadioStopState(currentRadioStation.value))
            }
            Player.STATE_IDLE -> {
                setPlaybackState(PlaybackState.RadioStopState(currentRadioStation.value))
            }
            Player.STATE_READY -> {
                if (exoPlayer.isPlaying) {
                    setPlaybackState(PlaybackState.RadioPlayState(currentRadioStation.value!!))
                } else {
                    setPlaybackState(PlaybackState.RadioStopState(currentRadioStation.value))
                }
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            setPlaybackState(PlaybackState.RadioPlayState(currentRadioStation.value!!))
        } else {
            setPlaybackState(PlaybackState.RadioStopState(currentRadioStation.value))
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(RadioService::class.java.simpleName, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        if (parentId == RadioService::class.java.simpleName) {
            result.sendResult(radioSource.getMediaItems().toMutableList())
        }
    }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            playCurrentStation()
        }

        override fun onPause() {
            stopCurrentPlayer()
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)
            if (mediaId != null) {
                stopCurrentPlayer()
                radioSource.setCurrentRadioStation(mediaId.toLong())
                playCurrentStation()
            }
        }

        override fun onSkipToNext() {
            playNextStation()
        }

        override fun onSkipToPrevious() {
            playPreviousStation()
        }

        override fun onStop() {
            stopCurrentPlayer()
        }


        override fun onPlayFromSearch(query: String?, extras: Bundle?) {
            val foundStation = radioSource.findStation(query) ?: return
            stopCurrentPlayer()
            radioSource.setCurrentRadioStation(foundStation.id)
            playCurrentStation()
        }
    }

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }
}