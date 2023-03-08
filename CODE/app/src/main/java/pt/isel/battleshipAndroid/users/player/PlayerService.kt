package pt.isel.battleshipAndroid.users.player

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.http.Uris
import pt.isel.battleshipAndroid.infra.SirenModel
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.users.model.*
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Abstraction that characterizes the player.
 */
interface IPlayerService {
    suspend fun fetchLogin(username: String, password: String): UserLoginOutputModel
    suspend fun fetchRegister(username: String, password: String, email: String): DefaultAnswerModel
    suspend fun fetchLogout(tokenAuthorize: String)
    suspend fun fetchRecovery(email: String): DefaultAnswerModel
    suspend fun fetchRanking(): List<GameRankTotals>
    suspend fun fetchUserMe(tokenAuthorize: String): UserOutputModel
    suspend fun getAllAuthors(): List<Author>
}

class PlayerService(
    val httpClient: OkHttpClient,
    val gson: Gson,
    val homeUrl: String
) : IPlayerService {

    //Login user in DAW API
    override suspend fun fetchLogin(username: String, password: String): UserLoginOutputModel {
        //simplify with gson
        val loginInfo = LoginInputModel(username, password)
        val loginInfoJson = gson.toJson(loginInfo)
        //create request body
        val body = loginInfoJson.toRequestBody(PlayerConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request =
            Request.Builder().post(body).addHeader("Content-Type", PlayerConstants.MEDIA_TYPE_JSON)
                .url(URL(homeUrl + Uris.Users.LOGIN)).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "fetchLogin got failure Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "fetchLogin got response Thread = ${Thread.currentThread().name}")
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(TAG, "fetchLogin got response problem = ${problem.title}")
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val loginOutputResult =
                            gson.fromJson(contentData, UserLoginOutputModel::class.java)
                        continuation.resume(loginOutputResult)
                    }
                }
            })
        }
    }

    //Register user in DAW API
    override suspend fun fetchRegister(
        username: String, password: String, email: String
    ): DefaultAnswerModel {
        //simplify with gson
        val registerInfo = UserCreateInputModel(username, email, password)
        val registerInfoJson = gson.toJson(registerInfo)
        //create request body
        val body = registerInfoJson.toRequestBody(PlayerConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request =
            Request.Builder().post(body).addHeader("Content-Type", PlayerConstants.MEDIA_TYPE_JSON)
                .url(URL(homeUrl + Uris.Users.REGISTER)).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "fetchRegister got failure Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "fetchRegister got response Thread = ${Thread.currentThread().name}")
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(TAG, "fetchRegister got response problem = ${problem.title}")
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val registerOutputResult =
                            gson.fromJson(contentData, DefaultAnswerModel::class.java)
                        continuation.resume(registerOutputResult)
                    }
                }
            })
        }
    }

    //Invalidate token in DAW API
    override suspend fun fetchLogout(tokenAuthorize: String) {
        //empty body to logout
        val logoutInfo = ""
        val body = logoutInfo.toRequestBody(PlayerConstants.MEDIA_TYPE_JSON.toMediaType())

        val request = Request
            .Builder()
            .post(body)
            .addHeader("Authorization", "Bearer $tokenAuthorize")
            .addHeader("Content-Type", PlayerConstants.MEDIA_TYPE_JSON)
            .url(URL(homeUrl + Uris.Users.LOGOUT))
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "fetchLogout got failure Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "fetchLogout got response Thread = ${Thread.currentThread().name}")
                    if (!response.isSuccessful)
                        continuation.resumeWithException(Exception(response.message))
                    else {
                        Log.v(TAG, "fetchLogout success = ${Thread.currentThread().name}")
                        continuation.resume(Unit)
                    }
                }
            })
        }
    }

    //Set password recovery in DAW API
    override suspend fun fetchRecovery(email: String): DefaultAnswerModel {
        //simplify with gson
        val recoveryInfo = RecoveryPasswordInputModel(email)
        val recoveryInfoJson = gson.toJson(recoveryInfo)
        //create request body
        val body = recoveryInfoJson.toRequestBody(PlayerConstants.MEDIA_TYPE_JSON.toMediaType())
        //create request
        val request =
            Request.Builder().post(body).addHeader("Content-Type", PlayerConstants.MEDIA_TYPE_JSON)
                .url(URL(homeUrl + Uris.Users.RECOVERY)).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "fetchRecovery got failure Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "fetchRecovery got response Thread = ${Thread.currentThread().name}")
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(TAG, "fetchRecovery got response problem = ${problem.title}")
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val registerOutputResult =
                            gson.fromJson(contentData, DefaultAnswerModel::class.java)
                        continuation.resume(registerOutputResult)
                    }
                }
            })
        }
    }

    //get ranking from DAW API
    override suspend fun fetchRanking(): List<GameRankTotals> {
        //create request
        val request =
            Request.Builder().get().addHeader("Content-Type", PlayerConstants.MEDIA_TYPE_JSON_SIREN)
                .url(URL(homeUrl + Uris.RANKING)).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "getRanking got failure Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "getRanking got response Thread = ${Thread.currentThread().name}")
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(TAG, "getRanking got response problem = ${problem.title}")
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val rankingOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)

                        val rankFromSiren = Gson().toJson(rankingOutputResult!!.properties)
                        val itemType = object : TypeToken<List<GameRankTotals>>() {}.type
                        val rankList =
                            Gson().fromJson<List<GameRankTotals>>(rankFromSiren, itemType)

                        continuation.resume(rankList)
                    }
                }
            })
        }
    }

    //get user home from DAW API
    override suspend fun fetchUserMe(tokenAuthorize: String): UserOutputModel {
        //create request
        val request =
            Request.Builder().get().addHeader("Content-Type", PlayerConstants.MEDIA_TYPE_JSON_SIREN)
                .addHeader("Authorization", "Bearer $tokenAuthorize")
                .url(URL(homeUrl + Uris.Users.HOME)).build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "getUserMe got failure Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "getUserMe got response Thread = ${Thread.currentThread().name}")
                    if (!response.isSuccessful) {
                        if (response.body == null) {
                            continuation.resumeWithException(Exception(response.code.toString()))
                        } else {
                            val contentData = response.body?.string()
                            val problem = gson.fromJson(contentData, ProblemOutputModel::class.java)
                            Log.v(TAG, "getUserMe got response problem = ${problem.title}")
                            continuation.resumeWithException(ProblemException(problem))
                        }
                    } else {
                        val contentData = response.body?.string()
                        val homeOutputResult =
                            gson.fromJson(contentData, SirenModel::class.java)

                        val homeResult = Gson().toJson(homeOutputResult?.properties)
                        val homeData = Gson().fromJson(homeResult, UserOutputModel::class.java)

                        continuation.resume(homeData)
                    }
                }
            })
        }
    }

    //stationary, because is always true
    override suspend fun getAllAuthors(): List<Author> {
        val list = mutableListOf<Author>()
        list.add(
            Author(
                name = "Raul Santos",
                number = 44806,
                email = "a44806@alunos.isel.pt",
                username = "RaulJCS5",
                github_http = "https://github.com/RaulJCS5",
                linkedin_http = "https://www.linkedin.com/in/rauljosecsantos/",
                ic_author = R.drawable.ic_author_rauljcs5
            )
        )
        list.add(
            Author(
                name = "Tiago Duarte",
                number = 42525,
                email = "a42525@alunos.isel.pt",
                username = "tiagomduarte",
                github_http = "https://github.com/tiagomduarte",
                linkedin_http = "https://www.linkedin.com/in/tiagomduarte/",
                ic_author = R.drawable.ic_author_tiagomduarte
            )
        )
        return list
    }
}

//aux const
class PlayerConstants {
    companion object {
        const val MEDIA_TYPE_JSON = "application/json"
        const val MEDIA_TYPE_JSON_SIREN = "application/vnd.siren+json"
    }
}