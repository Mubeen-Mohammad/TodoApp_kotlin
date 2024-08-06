package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodosActivity : AppCompatActivity(), TodoAdapter.OnTodoActionListener {

    private lateinit var addTodoButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var allTodosButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todos: List<Todo>
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todos)

        addTodoButton = findViewById(R.id.addTodoButton)
        titleEditText = findViewById(R.id.titleEditText)
        allTodosButton = findViewById(R.id.allTodosButton)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(emptyList(), this)
        recyclerView.adapter = todoAdapter

        email = intent.getStringExtra("email") ?: ""
        if (email.isEmpty()) {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show()
            finish()
        }

        addTodoButton.setOnClickListener {
            val title = titleEditText.text.toString()
            if (title.isNotEmpty()) {
                addTodo(title, email)
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        allTodosButton.setOnClickListener {
            fetchAllTodos(email)
        }
    }

    private fun addTodo(title: String, email: String) {
        val todo = Todo(
            title = title,
            email = email
        )
        RetrofitClient.instance.createTodo(todo).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Toast.makeText(this@TodosActivity, apiResponse?.msg, Toast.LENGTH_SHORT).show()
                    fetchAllTodos(email)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@TodosActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAllTodos(email: String) {
        RetrofitClient.instance.getAllTodos(email).enqueue(object : Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                if (response.isSuccessful) {
                    todos = response.body() ?: emptyList()
                    todoAdapter.updateTodos(todos)
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                Toast.makeText(this@TodosActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onUpdateClick(todo: Todo) {
        val updatedTodo = todo.copy(title = "Updated: ${todo.title}") // Example update
        RetrofitClient.instance.updateTodo(todo.email!!, updatedTodo).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Toast.makeText(this@TodosActivity, apiResponse?.msg, Toast.LENGTH_SHORT).show()
                    fetchAllTodos(email) // Refresh the list after update
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@TodosActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteClick(todo: Todo) {
        RetrofitClient.instance.deleteTodo(todo.email!!).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Toast.makeText(this@TodosActivity, apiResponse?.msg, Toast.LENGTH_SHORT).show()
                    fetchAllTodos(email) // Refresh the list after deletion
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@TodosActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
