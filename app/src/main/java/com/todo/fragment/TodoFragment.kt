package com.todo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.todo.R
import com.todo.adapter.TodoItemsAdapter
import com.todo.model.ToDoListItem
import com.todo.networking.ResponseHandler
import com.todo.networking.RetrofitBuilder
import com.todo.networking.Status
import com.todo.repository.ToDoRepositoryImpl
import com.todo.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_todo.*

class TodoFragment : Fragment() {
    private var viewModel: TodoViewModel? = null
    private lateinit var todoItemsAdapter: TodoItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupObserver()
    }

    private fun readFile(){

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        initListener()
    }

    override fun onStart() {
        super.onStart()
    }

    /** This function will set up UI **/
    private fun setupUi(){
        recyclerView.showShimmer()
        todoItemsAdapter = TodoItemsAdapter(context!!, arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation)
        )
        recyclerView.adapter = todoItemsAdapter
    }

    /** Initialize click listener**/
    private fun initListener(){
        reload.setOnClickListener {
            fetchTodos()
        }
    }

    /** Call viewmodel to fetch todos **/
    private fun fetchTodos(){
        viewModel?.fetchTodos()
    }

    /** Set up livedata observer **/
    private fun setupObserver() {
        viewModel?.getTodos()?.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { toDoList -> renderList(toDoList) }
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.hideShimmer()
                }
                Status.LOADING -> {
                    recyclerView.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    errorLayout.visibility = View.VISIBLE
                    recyclerView.hideShimmer()
                }
            }
        })
    }

    /** Render recycler view with data **/
    private fun renderList(todos: List<ToDoListItem>) {
        todoItemsAdapter.addData(todos)
        todoItemsAdapter.notifyDataSetChanged()
    }

    /** set up view model **/
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ToDoRepositoryImpl(RetrofitBuilder.apiService,
                ResponseHandler()))
        ).get(TodoViewModel::class.java)
    }
}
