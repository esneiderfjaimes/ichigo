package nei.ichigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import ichigo.core.api.di.DataDragonModule
import nei.ichigo.ui.pages.encyclopedia.EncyclopediaPage
import nei.ichigo.ui.pages.encyclopedia.EncyclopediaViewModel
import nei.ichigo.ui.theme.IchigoTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IchigoTheme {
                EncyclopediaPage(
                    viewModel = EncyclopediaViewModel(
                        dataDragonService = DataDragonModule.service
                    )
                )
            }
        }
    }
}