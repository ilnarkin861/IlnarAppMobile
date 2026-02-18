package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import retrofit2.Response
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.helpers.ARCHIVES_ENDPOINT
import ru.ilnarkin.ilnarapp.helpers.NOTE_TYPES_ENDPOINT
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.NoteType


class NoteTypeRepository(httpClient: HttpClient) : BaseRepository(httpClient,"$API_URL/$NOTE_TYPES_ENDPOINT")