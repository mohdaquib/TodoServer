package com.todo.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.model.ToDoListItem
import com.todo.networking.Resource
import com.todo.repository.ToDoRepositoryImpl
import com.todo.repository.TodoRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class TodoViewModel(private val todoRepo: TodoRepository) : ViewModel(){
    public val todoItems = MutableLiveData<Resource<List<ToDoListItem>>>()

    init {
        fetchTodos()
    }

    /** Fetch todos from repository **/
    fun fetchTodos(){
        viewModelScope.launch {
            todoItems.postValue(Resource.loading(null))
            try {
                val todos = todoRepo.getTodos()
                todoItems.postValue(todos)
            } catch (e: Exception){
                todoItems.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTodos(): LiveData<Resource<List<ToDoListItem>>>{
        return todoItems
    }
}