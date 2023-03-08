package pt.isel.battleshipAndroid.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.users.player.Author
import pt.isel.battleshipAndroid.utils.*
import pt.isel.battleshipAndroid.R

@Composable
fun ExpandableAuthorView(author: Author, onSelected: () -> Unit) {
    StatelessExpandablePlayerView(
        author = author,
        onSelected = onSelected
    )
}

@Composable
private fun StatelessExpandablePlayerView(
    author: Author,
    onSelected: () -> Unit = { }
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .testTag(NavigateExpandableAuthorTestTag)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onSelected)
                .padding(8.dp)
        ) {
            Text(
                text = author.name,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
            Text(
                text = author.number.toString(),
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandedUserViewPreview() {
    StatelessExpandablePlayerView(author = aAuthor)
}

@Preview(showBackground = true)
@Composable
private fun CollapsedUserViewPreview() {
    StatelessExpandablePlayerView(author = aAuthor)
}

@Preview(showBackground = true)
@Composable
private fun ExpandableUserViewPreview() {
    ExpandableAuthorView(author = aAuthor, onSelected = { })
}

private val aAuthor = Author(
    name="player1",number=11111,email="player1@alunos.isel.pt",username="player1",github_http="https://github.com/player1", linkedin_http = "https://www.linkedin.com/in/player1/",ic_author = R.drawable.clipart3240117
)
