package ru.ilnarkin.ilnarapp.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.repositories.FileRepository


class FilePagingSource(private val fileRepository: FileRepository) : PagingSource<Int, FileInfo>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FileInfo> {
		val offset = params.key ?: 0
		val limit = params.loadSize

		return try {

			val result = fileRepository.getFilesList<AppPagination<FileInfo>>(offset, limit)

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


	override fun getRefreshKey(state: PagingState<Int, FileInfo>): Int? {
		return null
	}
}