package pt.isel.battleshipAndroid.game

import android.content.ContentValues
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.battleshipAndroid.game.model.Position
import pt.isel.battleshipAndroid.game.model.PositionStateBoard
import pt.isel.battleshipAndroid.http.Uris
import pt.isel.battleshipAndroid.infra.SirenModel
import pt.isel.battleshipAndroid.lobby.SetFleetInputModel
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.model.GameOutputModel
import pt.isel.battleshipAndroid.model.GamePhase
import pt.isel.battleshipAndroid.model.MyBoardModel
import pt.isel.battleshipAndroid.users.model.GameRankTotals
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Abstraction that characterizes the game.
 */
interface IGameService {
    suspend fun fetchUserCurrentGame(tokenAuthorize: String): GameOutputModel
    suspend fun fetchGamePhase(gameId: Int, tokenAuthorize: String): GamePhase
    suspend fun fetchGiveUpGame(tokenAuthorize: String): DefaultAnswerModel
    suspend fun fetchSetFleet(
        gameId: Int?,
        token: String?,
        setFleetInputModel: MutableList<SetFleetInputModel>?
    ): String

    suspend fun fetchSetShoot(
        gameId: Int,
        pos: Position,
        tokenAuthorize: String
    ): DefaultAnswerModel

    suspend fun fetchCheckFleet(
        gameId: Int,
        myBoard: MyBoardModel,
        tokenAuthorize: String
    ): MutableList<MutableList<PositionStateBoard>>
}

class GameService(
    val httpClient: OkHttpClient,
    val gson: Gson,
    val homeUrl: String
) : IGameService {

    //Get user current game (is playing)
    override suspend fun fetchUserCurrentGame(tokenAuthorize: String): GameOutputModel {
        //create request
        val request =
            Request.Builder().get().addHeader("Content-Type", GameConstants.MEDIA_TYPE_JSON_SIREN)
                .addHeader("Authorization", "Bearer $tokenAuthorize")
                .url(URL(homeUrl + Uris.Game.GET_USER_CURRENT_GAME)).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "getUserCurrentGame got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "getUserCurrentGame got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "getUserCurrentGame got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val gameOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)

                        val gameResult = Gson().toJson(gameOutputResult?.properties)
                        val gameData = Gson().fromJson(gameResult, GameOutputModel::class.java)

                        continuation.resume(gameData)
                    }
                }
            })
        }
    }

    // Get user current game phase
    override suspend fun fetchGamePhase(gameId: Int, tokenAuthorize: String): GamePhase {
        //create request
        val request =
            Request.Builder().get().addHeader("Content-Type", GameConstants.MEDIA_TYPE_JSON_SIREN)
                .addHeader("Authorization", "Bearer $tokenAuthorize")
                .url(URL(homeUrl + Uris.Game.getCurrentGamePhase(gameId))).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "getGamePhase got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "getGamePhase got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "getGamePhase got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val gamePhaseOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)
                        val gamePhaseResult = Gson().toJson(gamePhaseOutputResult?.properties)
                        val gamePhaseData = Gson().fromJson(gamePhaseResult, GamePhase::class.java)
                        Log.v(
                            ContentValues.TAG,
                            "getGamePhase got response = ${gamePhaseData.name}"
                        )
                        continuation.resume(gamePhaseData)
                    }
                }
            })
        }
    }

    // user give up game and give winner to other player
    override suspend fun fetchGiveUpGame(tokenAuthorize: String): DefaultAnswerModel {
        //simplify with gson
        val emptyBody = gson.toJson(null)
        //create request body
        val body = emptyBody.toRequestBody(GameConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request = Request
            .Builder()
            .post(body)
            .addHeader("Authorization", "Bearer $tokenAuthorize")
            .addHeader("Content-Type", GameConstants.MEDIA_TYPE_JSON)
            .url(URL(homeUrl + Uris.Game.GIVE_UP_GAME))
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "giveUpGame got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "giveUpGame got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "giveUpGame got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))

                        }
                    } else {
                        val contentData = response.body?.string()
                        val giveUpGameMessage =
                            gson.fromJson(contentData, DefaultAnswerModel::class.java)
                        continuation.resume(giveUpGameMessage)
                    }
                }
            })
        }
    }

    //set fleet to @gameId
    override suspend fun fetchSetFleet(
        gameId: Int?,
        token: String?,
        setFleetInputModel: MutableList<SetFleetInputModel>?
    ): String {
        //simplify with gson
        val setFleetInputToJson = gson.toJson(setFleetInputModel?.toList())
        //create request body
        val body = setFleetInputToJson.toRequestBody(GameConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request =
            Request.Builder().post(body)
                .addHeader("Content-Type", GameConstants.MEDIA_TYPE_JSON_SIREN)
                .addHeader("Authorization", "Bearer $token")
                .url(URL(homeUrl + Uris.Game.setLayoutFleet(gameId!!))).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "setFleet got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "setFleet got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "setFleet got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val fleetOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)
                        val fleetResult = Gson().toJson(fleetOutputResult?.properties)

                        continuation.resume(fleetResult.replace("\"",""))
                    }
                }
            })
        }
    }

    // shoot opponent fleet
    override suspend fun fetchSetShoot(
        gameId: Int,
        pos: Position,
        tokenAuthorize: String
    ): DefaultAnswerModel {
        //simplify with gson
        val shootToJson = gson.toJson(pos)
        //create request body
        val body = shootToJson.toRequestBody(GameConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request =
            Request.Builder().post(body)
                .addHeader("Content-Type", GameConstants.MEDIA_TYPE_JSON_SIREN)
                .addHeader("Authorization", "Bearer $tokenAuthorize")
                .url(URL(homeUrl + Uris.Game.setShoot(gameId))).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "setShoot got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "setShoot got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "setShoot got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val shootOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)

                        val shootResult = Gson().toJson(shootOutputResult?.properties)
                        val shootResultMsg =
                            Gson().fromJson(shootResult, DefaultAnswerModel::class.java)

                        continuation.resume(shootResultMsg)
                    }
                }
            })
        }
    }

    //Get fleet @myBoard=true or false of current @gameId in json format
    override suspend fun fetchCheckFleet(
        gameId: Int,
        myBoard: MyBoardModel,
        tokenAuthorize: String
    ): MutableList<MutableList<PositionStateBoard>> {
        //simplify with gson
        val boardFlagToJson = gson.toJson(myBoard)
        //create request body
        val body = boardFlagToJson.toRequestBody(GameConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request =
            Request.Builder().post(body)
                .addHeader("Content-Type", GameConstants.MEDIA_TYPE_JSON_SIREN)
                .addHeader("Authorization", "Bearer $tokenAuthorize")
                .url(URL(homeUrl + Uris.Game.checkFleet(gameId))).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(
                        ContentValues.TAG,
                        "fetchCheckFleet got failure Thread = ${Thread.currentThread().name}"
                    )
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(
                        ContentValues.TAG,
                        "fetchCheckFleet got response Thread = ${Thread.currentThread().name}"
                    )
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(
                                ContentValues.TAG,
                                "fetchCheckFleet got response problem = ${problem.title}"
                            )
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val checkFleetResult =
                            gson.fromJson(contentData, SirenModel::class.java)

                        val checkFleetData = Gson().toJson(checkFleetResult!!.properties)

                        val itemType = object : TypeToken<MutableList<MutableList<PositionStateBoard>>>() {}.type
                        val boardStatusJson =
                            Gson().fromJson<MutableList<MutableList<PositionStateBoard>>>(checkFleetData, itemType)

                        continuation.resume(boardStatusJson)
                    }
                }
            })
        }
    }
}

//aux const
class GameConstants {
    companion object {
        const val MEDIA_TYPE_JSON = "application/json"
        const val MEDIA_TYPE_JSON_SIREN = "application/vnd.siren+json"
    }
}