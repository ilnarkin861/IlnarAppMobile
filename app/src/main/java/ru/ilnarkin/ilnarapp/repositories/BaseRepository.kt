package ru.ilnarkin.ilnarapp.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import io.ktor.util.reflect.typeInfo
import ru.ilnarkin.ilnarapp.models.AppModel
import ru.ilnarkin.ilnarapp.models.FilterModel
import ru.ilnarkin.ilnarapp.models.PaginationModel


abstract class BaseRepository(
	@PublishedApi internal val httpClient: HttpClient,
	@PublishedApi internal val endpoint: String
) {

	@Suppress("UNCHECKED_CAST")
	suspend inline fun <reified P : PaginationModel> getList(offset: Int, limit: Int, filter: FilterModel?): P {
		return httpClient.get(endpoint) {
			url {
				parameters.append("offset", offset.toString())
				parameters.append("limit", limit.toString())
				filter?.let { parameters.append("filter", it.toString()) }
			}
		}.body(typeInfo<P>()) as P
	}

	@Suppress("UNCHECKED_CAST")
	suspend inline fun <reified T : AppModel> getById(id: String): T? {

		return httpClient.get("$endpoint/$id").body(typeInfo<T>()) as T
	}

	@Suppress("UNCHECKED_CAST")
	suspend inline fun <reified T : AppModel> create(model: AppModel): T {
		return httpClient.post("$endpoint/add") {
			setBody(model)
		}.body(typeInfo<T>()) as T
	}

	@Suppress("UNCHECKED_CAST")
	suspend inline fun <reified T : AppModel> update(id: String, model: AppModel): T {
		return httpClient.put("$endpoint/edit/$id") {
			setBody(model)
		}.body(typeInfo<T>()) as T
	}

	suspend fun delete(id: String): Boolean {
		return httpClient.delete("$endpoint/delete/$id").status.isSuccess()
	}
}