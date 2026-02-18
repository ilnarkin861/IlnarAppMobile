package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginData (
	var email: String,
	var password: String,
)