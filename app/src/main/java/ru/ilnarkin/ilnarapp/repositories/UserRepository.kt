package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import ru.ilnarkin.ilnarapp.models.Info
import ru.ilnarkin.ilnarapp.models.PasswordModel
import ru.ilnarkin.ilnarapp.models.Token
import ru.ilnarkin.ilnarapp.models.UserInfo
import ru.ilnarkin.ilnarapp.models.UserLoginData


class UserRepository(private val httpClient: HttpClient, private val endpoint: String) {

	suspend fun checkAuth(): Boolean = httpClient.get("$endpoint/auth-check").status.isSuccess()


	suspend fun login(userAuthData : UserLoginData): Token {
		return httpClient.post("$endpoint/login"){ setBody(userAuthData) }.body()
	}


	suspend fun getUserInfo(): UserInfo{
		return httpClient.get("$endpoint/info").body()
	}


	suspend fun changeEmail(userInfo: UserInfo): Info{
		return httpClient.post("$endpoint/email-change"){ setBody(userInfo) }.body()
	}


	suspend fun resetPassword(passwordModel: PasswordModel): Info{
		return httpClient.post("$endpoint/password-reset"){ setBody(passwordModel)}.body()
	}
}