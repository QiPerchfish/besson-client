package com.example.demoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private var studentList: List<Student>
) : RecyclerView.Adapter<StudentAdapter.PostViewHolder>() {
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id : TextView = itemView.findViewById(R.id.stu_id)
        val name : TextView = itemView.findViewById(R.id.stu_name)
        val age : TextView = itemView.findViewById(R.id.stu_age)
        val score : TextView = itemView.findViewById(R.id.stu_score)
        val className : TextView = itemView.findViewById(R.id.class_name)
        val edits : ImageButton = itemView.findViewById(R.id.edit_query)
    }
    private var onItemClickListener: ((Student) -> Unit)?= null
    private var onEditClickListener: ((Student) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return PostViewHolder(view)
    }
    fun setOnItemClickListener(listener: (Student) -> Unit) {
        this.onItemClickListener = listener
    }
    fun setOnEditClickListener(listener: (Student) -> Unit) {
        this.onEditClickListener = listener
    }
    fun updateData(newList: List<Student>) {
        studentList = newList
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val student = studentList[position]
        holder.id.text = "ID: ${student.id}"
        holder.name.text = "姓名: ${student.name}"
        holder.age.text = "年龄: ${student.age}"
        holder.score.text = "分数: ${student.score}"
        holder.className.text = "班级: ${student.className}"

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(student)
        }
        holder.edits.setOnClickListener {
            onEditClickListener?.invoke(student)
        }
    }

    override fun getItemCount(): Int = studentList.size
}