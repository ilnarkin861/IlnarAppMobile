package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.helpers.NOTES_ENDPOINT


class NoteRepository(httpClient: HttpClient) : BaseRepository(httpClient,"$API_URL/$NOTES_ENDPOINT")