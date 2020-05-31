package com.todo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todo.R
import com.todo.model.ToDoListItem
import kotlinx.android.synthetic.main.item_layout.view.*

class TodoItemsAdapter(
    private val context: Context,
    private val todos: ArrayList<ToDoListItem>
) : RecyclerView.Adapter<TodoItemsAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todoItem : ToDoListItem) {
            itemView.todoName.text = todoItem.title
            itemView.todoStatus.text = todoItem.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = todos.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        var todo = todos[position].also {
            if (it.completed) it.status = context.getString(R.string.completed_status)
            else it.status = context.getString(R.string.pending_status)
        }
        holder.bind(todo)
    }

    fun addData(list: List<ToDoListItem>) {
        todos.addAll(list)
    }

}