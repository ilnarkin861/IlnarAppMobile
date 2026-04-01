package ru.ilnarkin.ilnarapp.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.repositories.ArchiveRepository


class ArchivePagingSource(private val archiveRepository: ArchiveRepository) : PagingSource<Int, Archive>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Archive> {
		val offset = params.key ?: 0
		val limit = params.loadSize

		return try {

			val result = archiveRepository.getList<AppPagination<Archive>>(offset, limit, null)

			LoadResult.Page(
				data = result.data,

				prevKey = if (offset <= 0) null else offset - limit,

				nextKey = if (result.pagination.hasNextPage) offset + limit else null
			)
		}
		catch (e: Exception){
			LoadResult.Error(e)
		}
	}


	override fun getRefreshKey(state: PagingState<Int, Archive>): Int? {
		return null
	}
}