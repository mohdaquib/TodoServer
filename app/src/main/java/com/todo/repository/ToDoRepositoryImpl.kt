package com.todo.repository

import com.google.gson.Gson
import com.todo.model.ToDoListItem
import com.todo.networking.Resource
import com.todo.networking.ResponseHandler
import com.todo.networking.ToDoApi

open class ToDoRepositoryImpl(
    private val toDoApi: ToDoApi,
    private val responseHandler: ResponseHandler
) : TodoRepository {
    private val gson: Gson = Gson()

    /** Repository layer function for fetching todos **/
    override suspend fun getTodos(): Resource<List<ToDoListItem>> {
        return try {
            val response = toDoApi.getTodos()
            val todoList: List<ToDoListItem> =
                gson.fromJson(response.charStream(),
                    Array<ToDoListItem>::class.java).toList() // Parse response body to List<TodoListItem>
            return responseHandler.handleSuccess(todoList)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}