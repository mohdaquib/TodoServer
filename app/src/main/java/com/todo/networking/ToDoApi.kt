package com.todo.networking

import okhttp3.ResponseBody
import retrofit2.http.GET

interface ToDoApi {
    @GET("todos")
    suspend fun getTodos(): ResponseBody
}