package com.example.textbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var bkIsbn:EditText//declaration of private variables
    private lateinit var bkTitle:EditText
    private lateinit var bkAuthor:EditText
    private lateinit var bkCourse:EditText
    private lateinit var btnAdd:Button
    private lateinit var btnView:Button
    private lateinit var btnUpdate:Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: TextBookAdapter?=null
    private var std:TextModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener {addTextBook()}
        btnView.setOnClickListener {viewTexts()}
        btnUpdate.setOnClickListener {updateTextBooks()}
        adapter?.setOnClickItem {
            Toast.makeText(this, it.author, Toast.LENGTH_SHORT).show()

            bkIsbn.setText(it.isbn)
            bkTitle.setText(it.title)
            bkAuthor.setText(it.author)
            bkCourse.setText(it.course)
            std=it
        }
        adapter?.setOnClickItemDelete {
            deleteTxt(it.id)
        }
    }

    //To execute each buttons functionality, data must be entered into field first
    //add the data then click Add to enter data into database
    private fun addTextBook(){
        val isbn = bkIsbn.text.toString()
        val title = bkTitle.text.toString()
        val author = bkAuthor.text.toString()
        val course = bkCourse.text.toString()

        if(isbn.isEmpty()||title.isEmpty()||author.isEmpty()||course.isEmpty()){
            Toast.makeText(this, "Field cannot be Empty!", Toast.LENGTH_SHORT).show()
        }else{
            val std= TextModel(isbn=isbn, title=title, author=author, course=course)
            val status = sqliteHelper.insertText(std)

            if(status >-1){
                Toast.makeText(this, "TextBook has been added", Toast.LENGTH_SHORT).show()
                clearText()
                viewTexts()
            }else{
                Toast.makeText(this, "TextBook was not added", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Press View first to view all records, select a record and the edittext fields will be occcupied
    //chnage the data and then press update, the data will be changed in the database
    private fun updateTextBooks(){
        val isbn = bkIsbn.text.toString()
        val title = bkTitle.text.toString()
        val author = bkAuthor.text.toString()
        val course = bkCourse.text.toString()

        if(isbn==std?.isbn && title==std?.title && author==std?.author && course==std?.course){
            Toast.makeText(this, "TextBook info was not changed", Toast.LENGTH_SHORT).show()
            return
        }
        if(std ==null) return
            val std = TextModel(id=std!!.id, isbn=isbn, title=title, author=author, course=course)
            val status= sqliteHelper.updateText(std)
        if(status > -1){
            clearText()
            viewTexts()
        }else{
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }

    //select view first and the delete button will appear beside each record
    private fun deleteTxt(id:Int){
        if(id==null) return
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this record?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){
            dialog,_->
            sqliteHelper.deleteText(id)
            viewTexts()
            clearText()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){
                dialog,_->dialog.dismiss()
    }
        val alert=builder.create()
        alert.show()
    }

    private fun clearText(){//clears edit text fields
        bkIsbn.setText("")
        bkTitle.setText("")
        bkAuthor.setText("")
        bkCourse.setText("")
        bkIsbn.requestFocus()
    }
    //displays record and a delete button in a recyclerView
    private fun viewTexts(){
        val txtList = sqliteHelper.getTextbooks()
        Log.e("pppp", "${txtList.size}")

        adapter?.addItems(txtList)
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager=LinearLayoutManager(this)
        adapter= TextBookAdapter()
        recyclerView.adapter=adapter
    }


    private fun initView(){//initialize variables with layout Ids
        bkIsbn=findViewById(R.id.bkIsbn)
        bkTitle=findViewById(R.id.bkTitle)
        bkAuthor=findViewById(R.id.bkAuthor)
        bkCourse=findViewById(R.id.bkCourse)
        btnAdd=findViewById(R.id.btnAdd)
        btnView=findViewById(R.id.btnView)
        btnUpdate=findViewById(R.id.btnUpdate)
        recyclerView=findViewById(R.id.recyclerView)
    }
}