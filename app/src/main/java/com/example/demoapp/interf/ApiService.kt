package com.example.demoapp.interf

import com.example.demoapp.Student
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("list")
    fun getStudentsInfo() : Call<List<Student>>

    @POST("add")
    fun addStudentsInfo(@Body student: Student) : Call<Student>
}
