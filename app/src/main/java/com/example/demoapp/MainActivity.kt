package com.example.demoapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rv_student : RecyclerView
    private lateinit var post_adapter : StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val deleteIb = findViewById<ImageButton>(R.id.delete_info)
        val searchIb = findViewById<ImageButton>(R.id.search_bar)
        val listItem = findViewById<ImageButton>(R.id.list_item)
        val edits = findViewById<ImageButton>(R.id.edit_query)
        val addStudent = findViewById<ImageButton>(R.id.btn_add)
        val student = findViewById<RecyclerView>(R.id.rv_student)

        listItem.setOnClickListener {
            fetchStudents()
        }
        addStudent.setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

        val nameEt = dialogView.findViewById<EditText>(R.id.name_et)
        val ageEt = dialogView.findViewById<EditText>(R.id.age_et)
        val scoreEt = dialogView.findViewById<EditText>(R.id.score_et)
        val classNameEt = dialogView.findViewById<EditText>(R.id.class_name_et)

        AlertDialog.Builder(this)
            .setTitle("添加学生")
            .setView(dialogView)
            .setPositiveButton("提交") { _, _ ->
                val name = nameEt.text.toString().trim()
                val age = ageEt.text.toString().trim()
                val score = scoreEt.text.toString().trim()
                val className = classNameEt.text.toString().trim()

                if (name.isEmpty()) {
                    Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (age.isEmpty()) {
                    Toast.makeText(this, "年龄不能为空", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (score.isEmpty()) {
                    Toast.makeText(this, "成绩不能为空", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (className.isEmpty()) {
                    Toast.makeText(this, "班级不能为空", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                addSudents(name, age.toInt(), score.toFloat(), className)
            }
            .setPositiveButton("取消", null)
            .show()
    }

    private fun addSudents(name: String, age: Int, score: Float, className: String) {
        val student = Student (
            id = 0,
            name = name,
            age = age,
            score = score,
            className = className )
        RetrofitClient.instance.addStudentsInfo(student).enqueue(object : retrofit2.Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                TODO("Not yet implemented")
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "添加成功", Toast.LENGTH_SHORT).show()
                    fetchStudents()
                } else {
                    Toast.makeText(this@MainActivity, "添加失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                TODO("Not yet implemented")
                Toast.makeText(this@MainActivity, "网络错误: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchStudents() {

        val call = RetrofitClient.instance.getStudentsInfo()
        call.enqueue(object : retrofit2.Callback<List<Student>> {
            override fun onResponse(call: retrofit2.Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    val posts = response.body() ?: emptyList()
                    post_adapter = StudentAdapter(posts)
                    rv_student.adapter = post_adapter
                } else {
                    Toast.makeText(this@MainActivity, "请求错误: ${response.code()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<List<Student> ?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "网络错误: ${t.message}", Toast.LENGTH_LONG)
            }
        })
    }
}
