package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.reflect.typeInfo
import ru.ilnarkin.ilnarapp.models.PaginationModel

class FileRepository(
	@PublishedApi internal val httpClient: HttpClient,
	@PublishedApi internal val endpoint: String)
{

	@Suppress("UNCHECKED_CAST")
	suspend inline fun <reified P : PaginationModel> getFilesList(offset: Int, limit: Int): P{
		return httpClient.get(endpoint){
			url {
				parameters.append("offset", offset.toString())
				parameters.append("limit", limit.toString())
			}
		}.body(typeInfo<P>()) as P
	}
}