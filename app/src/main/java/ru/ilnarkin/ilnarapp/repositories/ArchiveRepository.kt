package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.helpers.ARCHIVES_ENDPOINT


class ArchiveRepository(httpClient: HttpClient) : BaseRepository(httpClient,"$API_URL/$ARCHIVES_ENDPOINT")

