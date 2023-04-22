package com.red.code015.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import com.red.code015.data.APIDataSource
import com.red.code015.data.AppSettings
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.PlatformID
import com.red.code015.ui.theme.IchigoTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IchigoTheme {
                val myService = koinInject<DataStore<AppSettings>>()
                val collectAsState =
                    myService.data.collectAsState(initial = AppSettings.getDefaultInstance())
                val coroutineScope = rememberCoroutineScope()

                val summonerBySummonerNameUserCase = koinInject<RemoteRiotGamesDataSource>()
                val xd = koinInject<LocalSummonerDataSource>()

                Column {
                    Button(onClick = {
                        coroutineScope.launch {
                            myService.updateData {
                                it.copy(
                                    loading = !it.loading,
                                    PlatformID.LAN,
                                    listOf(
                                        summonerBySummonerNameUserCase.profileBySummonerName("Like you do")
                                    )
                                )
                            }
                        }
                    }) {
                        Text(text = collectAsState.value.loading.toString())
                    }
                    collectAsState.value.profiles.forEach {
                        Text(text = it.name)
                    }
                    Text(text = xd.toString())
                }

                //   IchigoApp(viewModel)
            }
        }
    }
}