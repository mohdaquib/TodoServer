package com.todo.model

data class ToDoListItem(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int,
    var status: String
)