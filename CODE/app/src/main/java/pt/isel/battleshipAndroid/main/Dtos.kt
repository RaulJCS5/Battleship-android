package pt.isel.battleshipAndroid.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Used to create local (to the Android device) representations.
 */
@Parcelize
data class LocalTokenDto(
    val token: String?,
    val userId: Int?
) : Parcelable

@Parcelize
data class LocalPlayerDto(
    val id: Int,
    val userName: String,
    val email: String
) : Parcelable

@Parcelize
data class LocalAuthorDto(
    val name: String, val number: Int, val email: String, val username: String,
    val github_http: String, val linkedin_http: String, val ic_author: Int
) : Parcelable