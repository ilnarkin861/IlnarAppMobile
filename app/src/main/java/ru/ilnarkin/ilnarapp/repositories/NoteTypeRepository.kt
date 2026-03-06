package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.helpers.NOTE_TYPES_ENDPOINT


class NoteTypeRepository(httpClient: HttpClient) : BaseRepository(httpClient,"$API_URL/$NOTE_TYPES_ENDPOINT")