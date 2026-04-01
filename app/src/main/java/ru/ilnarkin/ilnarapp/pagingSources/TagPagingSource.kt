package ru.ilnarkin.ilnarapp.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.repositories.TagRepository


class TagPagingSource(private val tagRepository: TagRepository) : PagingSource<Int, Tag>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tag> {
		val offset = params.key ?: 0
		val limit = params.loadSize

		return try {

			val result = tagRepository.getList<AppPagination<Tag>>(offset, limit, null)

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


	override fun getRefreshKey(state: PagingState<Int, Tag>): Int? {
		return null
	}
}