package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.Todo

class TodoAdapter(
    private var todos: List<Todo>,
    private val listener: OnTodoActionListener
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    interface OnTodoActionListener {
        fun onUpdateClick(todo: Todo)
        fun onDeleteClick(todo: Todo)
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTitle: TextView = itemView.findViewById(R.id.todoTitle)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        init {
            updateButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onUpdateClick(todos[position])
                }
            }
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(todos[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.todoTitle.text = currentTodo.title
    }

    override fun getItemCount() = todos.size

    fun updateTodos(newTodos: List<Todo>) {
        todos = newTodos
        notifyDataSetChanged()
    }
}
