package com.todo.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.todo.model.ToDoListItem
import com.todo.networking.Resource
import com.todo.repository.TodoRepository
import com.todo.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TodoViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    @Mock
    private lateinit var toDoRepository: TodoRepository
    @Mock
    private lateinit var todosObserver: Observer<Resource<List<ToDoListItem>>>

    @Before
    fun setUp() {
        // do something if required
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            Mockito.doReturn(getTodoListItems())
                .`when`(toDoRepository)
                .getTodos()
            val viewModel = TodoViewModel(toDoRepository)
            viewModel.getTodos().observeForever(todosObserver)
            Mockito.verify(toDoRepository).getTodos()
            Mockito.verify(todosObserver).onChanged(getTodoListItems())
            viewModel.getTodos().removeObserver(todosObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message"
            Mockito.doThrow(RuntimeException(errorMessage))
                .`when`(toDoRepository)
                .getTodos()
            val viewModel = TodoViewModel(toDoRepository)
            viewModel.getTodos().observeForever(todosObserver)
            Mockito.verify(toDoRepository).getTodos()
            Mockito.verify(todosObserver).onChanged(
                Resource.error(RuntimeException(errorMessage).toString(), null)
            )
            viewModel.getTodos().removeObserver(todosObserver)
        }
    }

    private fun getTodoListItems(): Resource<List<ToDoListItem>>{
        val todoItems = List<ToDoListItem>(1) {
            ToDoListItem(false, 1, "Todo assignment",
                123, "Completed")
        }
        return Resource.success(todoItems)
    }


    @After
    fun tearDown() {
        // do something if required
    }

}