package com.example.todoapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<ApiResponse>

    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<ApiResponse>

    @GET("/alltodos")
    fun getAllTodos(@Query("email") email: String): Call<List<Todo>>

    @POST("/alltodos")
    fun createTodo(@Body todo: Todo): Call<ApiResponse>

    @PUT("/alltodos/{email}")
    fun updateTodo(@Path("email") email: String, @Body todo: Todo): Call<ApiResponse>

    @DELETE("/alltodos/{email}")
    fun deleteTodo(@Path("email") email: String): Call<ApiResponse>
}
