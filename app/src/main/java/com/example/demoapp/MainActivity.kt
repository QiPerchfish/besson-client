package com.example.demoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvStudent: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化 RecyclerView
        rvStudent = findViewById(R.id.rv_student)
        rvStudent.layoutManager = LinearLayoutManager(this)

        // 初始化 Adapter
        studentAdapter = StudentAdapter(emptyList())
        rvStudent.adapter = studentAdapter

        // 设置编辑回调
        studentAdapter.setOnEditClickListener { student ->
            showEditStudentDialog(student)
        }

        // 设置删除回调（整行点击 = 删除）
        studentAdapter.setOnItemClickListener { student ->
            showDeleteConfirmDialog(student)
        }

        // FAB 添加按钮
        fabAdd = findViewById(R.id.add_fab)
        fabAdd.setOnClickListener {
            showAddStudentDialog()
        }

        // 加载数据
        fetchStudents()
    }

    // 获取列表
    private fun fetchStudents() {
        RetrofitClient.instance.getStudentsInfo().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    val students = response.body() ?: emptyList()
                    studentAdapter.updateData(students)
                } else {
                    Toast.makeText(this@MainActivity, "获取数据失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "网络错误: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    //  添加学生
    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

        val nameEt = dialogView.findViewById<EditText>(R.id.name_et)
        val ageEt = dialogView.findViewById<EditText>(R.id.age_et)
        val scoreEt = dialogView.findViewById<EditText>(R.id.score_et)
        val classNameEt = dialogView.findViewById<EditText>(R.id.class_name_et)
        val submitBtn = dialogView.findViewById<Button>(R.id.submit_btn)

        val dialog = AlertDialog.Builder(this)
            .setTitle("添加学生")
            .setView(dialogView)
            .setNegativeButton("取消", null)
            .show()

        submitBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val ageStr = ageEt.text.toString().trim()
            val scoreStr = scoreEt.text.toString().trim()
            val className = classNameEt.text.toString().trim()

            if (name.isEmpty() || ageStr.isEmpty() || scoreStr.isEmpty() || className.isEmpty()) {
                Toast.makeText(this, "请完整填写所有字段", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageStr.toIntOrNull()
            val score = scoreStr.toFloatOrNull()

            if (age == null || score == null) {
                Toast.makeText(this, "年龄和成绩必须是有效数字", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addStudent(name, age, score, className)
            dialog.dismiss()
        }
    }

    private fun addStudent(name: String, age: Int, score: Float, className: String) {
        val student = Student(
            id = 0,
            name = name,
            age = age,
            score = score,
            className = className
        )

        RetrofitClient.instance.addStudentsInfo(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "添加成功", Toast.LENGTH_SHORT).show()
                    fetchStudents()
                } else {
                    Toast.makeText(this@MainActivity, "添加失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@MainActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 编辑
    private fun showEditStudentDialog(student: Student) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)

        val nameEt = dialogView.findViewById<EditText>(R.id.name_et)
        val ageEt = dialogView.findViewById<EditText>(R.id.age_et)
        val scoreEt = dialogView.findViewById<EditText>(R.id.score_et)
        val classNameEt = dialogView.findViewById<EditText>(R.id.class_name_et)
        val submitBtn = dialogView.findViewById<Button>(R.id.submit_btn)

        // 预填数据
        nameEt.setText(student.name)
        ageEt.setText(student.age.toString())
        scoreEt.setText(student.score.toString())
        classNameEt.setText(student.className)

        val dialog = AlertDialog.Builder(this)
            .setTitle("编辑学生")
            .setView(dialogView)
            .setNegativeButton("取消", null)
            .show()

        submitBtn.text = "保存"
        submitBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val ageStr = ageEt.text.toString().trim()
            val scoreStr = scoreEt.text.toString().trim()
            val className = classNameEt.text.toString().trim()

            if (name.isEmpty() || ageStr.isEmpty() || scoreStr.isEmpty() || className.isEmpty()) {
                Toast.makeText(this, "请完整填写所有字段", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageStr.toIntOrNull()
            val score = scoreStr.toFloatOrNull()

            if (age == null || score == null) {
                Toast.makeText(this, "年龄和成绩必须是有效数字", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = student.copy(
                name = name,
                age = age,
                score = score,
                className = className
            )

            updateStudent(student.id, updatedStudent)
            dialog.dismiss()
        }
    }

    private fun updateStudent(id: Int, student: Student) {
        RetrofitClient.instance.updateStudentInfo(id, student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "更新成功", Toast.LENGTH_SHORT).show()
                    fetchStudents()
                } else {
                    Toast.makeText(this@MainActivity, "更新失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@MainActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 删除学生
    private fun showDeleteConfirmDialog(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除 ${student.name} 吗？")
            .setPositiveButton("删除") { _, _ ->
                deleteStudent(student.id)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun deleteStudent(id: Int) {
        RetrofitClient.instance.deleteStudentsInfo(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "删除成功", Toast.LENGTH_SHORT).show()
                    fetchStudents()
                } else {
                    Toast.makeText(this@MainActivity, "删除失败: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
