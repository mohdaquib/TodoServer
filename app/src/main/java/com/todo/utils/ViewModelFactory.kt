package com.todo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todo.fragment.TodoViewModel
import com.todo.repository.ToDoRepositoryImpl

class ViewModelFactory(private val repositoryImpl: ToDoRepositoryImpl) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(repositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}