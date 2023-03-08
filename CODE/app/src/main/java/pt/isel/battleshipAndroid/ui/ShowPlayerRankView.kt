package pt.isel.battleshipAndroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.users.model.GameRankTotals
import pt.isel.battleshipAndroid.utils.*

@Composable
fun ShowPlayerRankView(ranks: GameRankTotals) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .testTag(NavigateExpandablePlayerTestTag)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_user_id_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.user.id.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_username_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.user.username,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_email_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.user.email,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_ranking_played_games_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.playedGames.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),

                    )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_ranking_win_games_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.winGames.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_ranking_lost_games_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.lostGames.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_ranking_rank_points_label),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp),
                )
                Text(
                    text = ranks.rankPoints.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }
}