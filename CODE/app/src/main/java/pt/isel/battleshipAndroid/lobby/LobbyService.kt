package pt.isel.battleshipAndroid.lobby

import android.content.ContentValues
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.battleshipAndroid.http.Uris
import pt.isel.battleshipAndroid.infra.SirenModel
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.model.GameInputModel
import pt.isel.battleshipAndroid.model.GameOutputModel
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Abstraction that characterizes the game's lobby.
 */
interface ILobbyService {
    suspend fun fetchNewGame(maxShot: Int, tokenAuthorize: String): String
    suspend fun fetchGiveUpLobby(tokenAuthorize: String): DefaultAnswerModel
}

class LobbyService(
    val httpClient: OkHttpClient,
    val gson: Gson,
    val homeUrl: String
) : ILobbyService {

    //Intent to start new game
    override suspend fun fetchNewGame(maxShot: Int, tokenAuthorize: String): String {
        //simplify with gson
        val gameInputModel = GameInputModel(maxShot)
        val gameInputModelJson = gson.toJson(gameInputModel)
        //create request body
        val body = gameInputModelJson.toRequestBody(LobbyConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request = Request
            .Builder()
            .post(body)
            .addHeader("Authorization", "Bearer $tokenAuthorize")
            .addHeader("Content-Type", LobbyConstants.MEDIA_TYPE_JSON)
            .url(URL(homeUrl + Uris.Game.NEW_GAME))
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "fetchNewGame got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "fetchNewGame got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            if (problem.title == "User set in lobby" || problem.title == "User already in lobby!") {
                                continuation.resume(problem.title)
                            } else {
                                Log.v(
                                    ContentValues.TAG,
                                    "fetchNewGame got response problem = ${problem.title}"
                                )
                                continuation.resumeWithException(ProblemException(problem))
                            }
                        }
                    } else {
                        val contentData = response.body?.string()
                        val gameOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)

                        val gameFromSiren = Gson().toJson(gameOutputResult!!.properties)
                        val itemType = object : TypeToken<GameOutputModel>() {}.type
                        val game =
                            Gson().fromJson<GameOutputModel>(gameFromSiren, itemType)

                        continuation.resume("Enter game ${game.gameId}")
                    }
                }
            })
        }
    }

    // Exit lobby (paring not done)
    override suspend fun fetchGiveUpLobby(tokenAuthorize: String): DefaultAnswerModel {
        //simplify with gson
        val emptyBody = gson.toJson(null)
        //create request body
        val body = emptyBody.toRequestBody(LobbyConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request = Request
            .Builder()
            .post(body)
            .addHeader("Authorization", "Bearer $tokenAuthorize")
            .addHeader("Content-Type", LobbyConstants.MEDIA_TYPE_JSON)
            .url(URL(homeUrl + Uris.Game.GIVE_UP_LOBBY))
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "giveUpLobby got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "giveUpLobby got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "giveUpLobby got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))

                        }
                    } else {
                        val contentData = response.body?.string()
                        val giveUpLobbyMessage =
                            gson.fromJson(contentData, DefaultAnswerModel::class.java)
                        continuation.resume(giveUpLobbyMessage)
                    }
                }
            })
        }
    }
}

//aux const
class LobbyConstants {
    companion object {
        const val MEDIA_TYPE_JSON = "application/json"
        const val MEDIA_TYPE_JSON_SIREN = "application/vnd.siren+json"
    }
}