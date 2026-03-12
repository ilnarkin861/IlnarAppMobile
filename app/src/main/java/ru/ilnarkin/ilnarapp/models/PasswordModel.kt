package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class PasswordModel(
    val oldPassword: String,
    val newPassword: String,
    val confirmedPassword: String)
