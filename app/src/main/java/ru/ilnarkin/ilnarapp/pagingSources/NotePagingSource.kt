package ru.ilnarkin.ilnarapp.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.repositories.NoteRepository

class NotePagingSource(
	private val noteRepository: NoteRepository,
	private val filter: NoteFilter?
): PagingSource<Int, Note>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Note> {
		val offset = params.key ?: 0
		val limit = params.loadSize

		return try {
			val filterParams = mutableListOf<Pair<String, String>>().apply {
				filter?.noteTypeId?.let { add("noteTypeId" to it) }
				filter?.archiveId?.let { add("archiveId" to it) }
				filter?.year?.let { add("year" to it.toString()) }
				filter?.month?.let { add("month" to it.toString()) }
				filter?.tagIds?.forEach { id -> add("tagIds" to id) }
			}

			val result = noteRepository.getList<AppPagination<Note>>(offset, limit, filterParams)

			LoadResult.Page(
				data = result.data,

				prevKey = if (offset <= 0) null else offset - limit,

				nextKey = if (result.pagination.hasNextPage) offset + limit else null
			)
		}
		catch (e: Exception) {
			LoadResult.Error(e)
		}

	}


	override fun getRefreshKey(state: PagingState<Int, Note>): Int? {
		return null
	}
}