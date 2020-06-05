package com.revolgenx.anilib.view

import android.content.Context;
import android.util.AttributeSet
import android.view.LayoutInflater;
import android.widget.FrameLayout
import android.widget.TextView
import java.io.BufferedReader;
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.ThreadUtil
import kotlinx.android.synthetic.main.view_log.view.*
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean

class LogView : FrameLayout {
    private val started: AtomicBoolean = AtomicBoolean(false)
     val sb: StringBuilder = StringBuilder();
    private var logText: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        init(context);

    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_log, null)
        logText = view.logs
        addView(view)
    }

    fun start() {
        Thread {
            started.set(true)
            var stream: BufferedReader? = null
            try {
                Runtime.getRuntime().exec("logcat -c");
                val pq: Process = Runtime.getRuntime().exec("logcat *:E v main");
                stream = BufferedReader(InputStreamReader(pq.inputStream));
                var log: String? = "";
                log = stream.readLine()
                while (started.get() && log != null && context != null) {
                    ThreadUtil.runOnUiThread {
                        log(truncate(log))
                    }
                    log = stream.readLine()
                }
            } catch (e: Exception) {

            } finally {
                stream?.close()
            }
        }.start()
    }

    private fun truncate(log: String?): String {
        return (log ?: "").replaceFirst("([\\d-\\s:.]*)", "");
    }

    private fun log(log: String) {
        sb.append(log).append("\n");
        logText?.text = sb.toString();
    }

    fun stop() {
        started.set(false);
    }

}