package ru.ilnarkin.ilnarapp.repositories

import retrofit2.Response
import ru.ilnarkin.ilnarapp.models.AppModel
import ru.ilnarkin.ilnarapp.models.FilterModel
import ru.ilnarkin.ilnarapp.models.PaginationModel


interface Repository<TModel, PModel> where TModel : AppModel, PModel : PaginationModel {
	suspend fun getList(offset: Int, limit: Int, filter: FilterModel?): Response<PModel>
	suspend fun getById(id: String): Response<TModel?>
	suspend fun create(model: TModel): Response<TModel>
	suspend fun update(model: TModel): Response<TModel>
	suspend fun delete(id: String): Response<Any>
}