package com.revolgenx.anilib.common.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ui.screen.state.LoadingScreen
import com.revolgenx.anilib.common.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/*todo: need to change way to show messages */
class AuthActivity : ComponentActivity() {
    private val authDataStore: AuthDataStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme() {
                LoadingScreen()
            }
        }
        handleResult(intent.data)
    }

    private fun handleResult(data: Uri?) {
        val regex = "(?:access_token=)(.*?)(?:&)".toRegex()
        val matchResult = regex.find(data?.fragment.toString())
        if (matchResult?.groups?.get(1) != null) {
            try {
                matchResult.groups[1]?.value?.let {
                    lifecycleScope.launch {
                        authDataStore.login(matchResult.groups[1]!!.value)
                    }
                } ?: let {
                    Toast.makeText(this@AuthActivity, "Login Failed", Toast.LENGTH_LONG).show()
                }
                returnToActivity(true)
            } catch (e: Exception) {
                Toast.makeText(this@AuthActivity, "Login Failed", Toast.LENGTH_LONG).show()
                returnToActivity()
            }
        } else {
            Toast.makeText(this@AuthActivity, "Login Failed", Toast.LENGTH_LONG).show()
            returnToActivity()
        }
    }

    private fun returnToActivity(new: Boolean = false) {
        val intent = Intent(this, MainActivity::class.java).apply {
            if (new) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            } else {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        }
        startActivity(intent)
        finish()
    }

}