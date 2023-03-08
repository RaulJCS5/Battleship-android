package pt.isel.battleshipAndroid.users.author

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.main.LocalAuthorDto
import pt.isel.battleshipAndroid.main.MainActivity.Companion.AUTHOR_EXTRA
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.users.player.Author
import pt.isel.battleshipAndroid.utils.viewModelInit

class AuthorActivity : ComponentActivity() {
    /**
     * It serves to host the time-consuming operations and the state that results from those time-consuming operations.
     * This piece is used because it survives reconfigurations.
     * ViewModels store by activity.
     * There is a repository (store) of viewModels per activity, each activity has an associated store of viewModels (can have more than one viewModel in a given activity)
     * Ou cria um novo viewModel ou devolve o que já existia.
     * Na reconfiguração é iniciado de novo e vem de lá a instancia que já existia e não uma nova
     * */
    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel: AuthorViewModel by viewModels {
        viewModelInit {
            AuthorViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedExtra = authorExtra
            if (receivedExtra != null) {
                viewModel.setAuthors(receivedExtra)
                AuthorScreen(
                    state = AuthorScreenState(Author(receivedExtra)),
                    onNavigationRequested = NavigationHandlers(
                        onBackRequested = { finish() }
                    ),
                    onSendEmailRequested = { openSendEmail(viewModel.author?.getOrNull()) },
                    onOpenUrlRequested = { openURL(it) }
                )
            }
        }
    }

    private fun openSendEmail(auth: Author?) {
        try {
            if (auth!=null) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(auth.email))
                    putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                }

                startActivity(intent)
            }
        }
        catch (e: ActivityNotFoundException) {
            Log.e(ContentValues.TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    private fun openURL(url: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e(ContentValues.TAG, "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    @Suppress("deprecation")
    private val authorExtra: LocalAuthorDto?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(AUTHOR_EXTRA, LocalAuthorDto::class.java)
            else
                intent.getParcelableExtra(AUTHOR_EXTRA)
}

private const val emailSubject = "About the Battleship App"