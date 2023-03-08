package pt.isel.battleshipAndroid.users.listAuthors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.battleshipAndroid.ui.ExpandableAuthorView
import pt.isel.battleshipAndroid.users.player.Author
import pt.isel.battleshipAndroid.utils.*
import pt.isel.battleshipAndroid.R


data class AuthorsListScreenState(
    val authors: List<Author> = emptyList(),
)


@Composable
fun AuthorsListScreen(
    state: AuthorsListScreenState = AuthorsListScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    onAuthorSelected: (Author) -> Unit = { },
) {
    BattleshipAndroidTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(NavigateAuthorsListScreenTestTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(navigation = onNavigationRequested,R.string.app_authors_screen_title) }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(state.authors) {
                    ExpandableAuthorView(
                        author = it,
                        onSelected = { onAuthorSelected(it) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorsPreview() {
    val list = mutableListOf<Author>()
    list.add(Author(name = "Unknown1",number = 99999,email = "Unknown1@alunos.isel.pt",username = "Unknown1", github_http = "https://github.com/Unknown1",linkedin_http = "https://www.linkedin.com/in/unknown1/",ic_author = R.drawable.clipart3240117))
    list.add(Author(name = "Unknown2",number = 88888,email = "Unknown2@alunos.isel.pt",username = "Unknown2", github_http = "https://github.com/Unknown2",linkedin_http = "https://www.linkedin.com/in/unknown2/",ic_author = R.drawable.clipart3240117))
    val state = AuthorsListScreenState(list)
    AuthorsListScreen(
        state = state
    )
}