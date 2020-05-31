package com.todo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.todo.R
import com.todo.fragment.TodoFragment

class MainActivity : AppCompatActivity() {
    private val todoFragment: TodoFragment = TodoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.root, todoFragment, "ToDo").commit()
    }
}
