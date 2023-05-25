package com.revolgenx.anilib.common.ui.screen

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.setting.ui.screen.SettingScreen


object LoginScreen : BaseTabScreen() {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.profile)
            val icon = painterResource(id = R.drawable.ic_person)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        LoginScreenContent()
    }
}


@Composable
private fun LoginScreenContent() {
    val context = localContext()
    val navigator = localNavigator()
    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 12.dp)
        ) {
            Card(
                modifier = Modifier.padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = stringResource(id = R.string.important_to_know)
                        )
                        Text(
                            text = stringResource(id = R.string.important_to_know),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.login_browser_requirement_desc),
                        fontSize = 13.sp
                    )

                    Divider(
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                        login(context)
                    }) {
                        Text(text = stringResource(id = R.string.login))
                    }

                    Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.sign_up))
                    }

                    Divider(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            alignment = Alignment.CenterHorizontally
                        )
                    ) {
                        FilledTonalButton(onClick = {
                            navigator.push(SettingScreen())
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_settings),
                                contentDescription = stringResource(id = R.string.settings)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = stringResource(id = R.string.settings))
                        }
                        OutlinedButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = stringResource(id = R.string.about)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = stringResource(id = R.string.about))
                        }
                    }
                }
            }
        }
    }
}


private fun login(context: Context) {
    val authUri = BuildConfig.AUTH_ENDPOINT.toUri().buildUpon()
        .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
        .appendQueryParameter("response_type", "token")
        .build()
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, authUri)
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenContent()
}