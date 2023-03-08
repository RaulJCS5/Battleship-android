package pt.isel.battleshipAndroid.utils

import android.app.Activity
import android.content.Intent
import android.os.Parcelable

//Aux to avoid repeat code in all activity's
class NavigateAux {

    companion object {
        inline fun <reified T> navigateTo(
            ctx: Activity,
            argumentName: String? = null,
            obj: Parcelable? = null
        ) {
            val intent = Intent(ctx, T::class.java)

            if (obj != null && argumentName != null)
                intent.putExtra(argumentName, obj)
            ctx.startActivity(intent)
        }
    }
}