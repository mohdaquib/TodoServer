package com.todo.repository

import com.todo.model.ToDoListItem
import com.todo.networking.Resource

interface TodoRepository {
    suspend fun getTodos(): Resource<List<ToDoListItem>>
}