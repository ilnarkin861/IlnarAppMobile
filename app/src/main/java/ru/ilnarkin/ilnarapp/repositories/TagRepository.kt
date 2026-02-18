package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.helpers.TAGS_ENDPOINT


class TagRepository(httpClient: HttpClient) : BaseRepository(httpClient,"$API_URL/$TAGS_ENDPOINT")