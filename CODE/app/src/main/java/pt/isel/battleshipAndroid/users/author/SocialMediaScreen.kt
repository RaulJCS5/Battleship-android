package pt.isel.battleshipAndroid.users.author

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.R

data class SocialInfo(val link: Uri, @DrawableRes val imageId: Int)

@Composable
fun UsersView(username : String, email : String,ic:Int?, modifier : Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        var icPicture = ic
        if (icPicture==null)
            icPicture = R.drawable.clipart3240117
        Image(
            painter = painterResource(id = icPicture),
            contentDescription = null,
            modifier = Modifier.sizeIn(100.dp, 100.dp, 200.dp, 200.dp)
        )
        Text(text = username, style = MaterialTheme.typography.h5)
        Text(text = email, style = MaterialTheme.typography.h5)
        Icon(imageVector = Icons.Default.Email, contentDescription = null)
    }
}

@Composable
fun Socials(
    onOpenUrlRequested: (Uri) -> Unit = { },
    socials: Iterable<SocialInfo>
) {
    Column(
        modifier = Modifier.widthIn(min = 60.dp, max = 120.dp)
    ) {
        socials.forEach {
            Social(id = it.imageId, onClick = { onOpenUrlRequested(it.link) })
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun Social(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}
