package com.example.demoapp.interf

import com.example.demoapp.Student
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/students/list")
    fun getStudentsInfo() : Call<List<Student>>

    @POST("api/students/add")
    fun addStudentsInfo(@Body student: Student) : Call<Student>

    @PUT("/{id}")
    fun updateStudentInfo(@Path("id") id: Int, @Body student: Student) : Call<Student>

    @DELETE("/{id}")
    fun deleteStudentsInfo(@Path("id") id: Int) : Call<Void>
}
