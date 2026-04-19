package ru.ilnarkin.ilnarapp.repositories

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.streams.asInput
import ru.ilnarkin.ilnarapp.models.PaginationModel
import ru.ilnarkin.ilnarapp.models.SelectedFileInfo


class FileRepository(
	private val context: Context,
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


	@OptIn(InternalAPI::class)
	suspend fun uploadFiles(files: List<SelectedFileInfo>) {
		val resolver = context.contentResolver

		httpClient.post("${endpoint}/upload/multiple") {
			setBody(MultiPartFormDataContent(
				formData {
					files.forEach { fileInfo ->
						val inputStream = resolver.openInputStream(fileInfo.uri)
						if (inputStream != null) {

							appendInput(
								key = "files",
								headers = Headers.build {
									append(HttpHeaders.ContentType, fileInfo.mimeType ?: "application/octet-stream")
									append(HttpHeaders.ContentDisposition, "filename=\"${fileInfo.name}\"")
								}
							) {
								inputStream.asInput()
							}
						}
					}
				}
			))
		}
	}
}