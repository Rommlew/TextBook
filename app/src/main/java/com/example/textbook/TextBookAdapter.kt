package com.example.textbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextBookAdapter : RecyclerView.Adapter<TextBookAdapter.TextViewHolder>(){

    private var txtList:ArrayList<TextModel> = ArrayList()
    private var onClickItem:((TextModel)->Unit)?=null
    private var onClickItemDelete:((TextModel)->Unit)?=null

    fun addItems(items:ArrayList<TextModel>){
        this.txtList=items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback:(TextModel)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickItemDelete(callback:(TextModel)->Unit){
        this.onClickItemDelete = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= TextViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.items_txt, parent, false)
    )

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
       val std = txtList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener { onClickItemDelete?.invoke(std) }
    }

    override fun getItemCount(): Int {
        return txtList.size
    }


class TextViewHolder(var view: View):RecyclerView.ViewHolder(view){
    private var id= view.findViewById<TextView>(R.id.vId)
    private var isbn= view.findViewById<TextView>(R.id.vIsbn)
    private var title= view.findViewById<TextView>(R.id.vTitle)
    private var author= view.findViewById<TextView>(R.id.vAuthor)
    private var course= view.findViewById<TextView>(R.id.vCourse)
    var btnDelete= view.findViewById<Button>(R.id.btnDelete)

    fun bindView(std:TextModel){
        id.text=std.id.toString()
        isbn.text=std.isbn
        title.text=std.title
        author.text=std.author
        course.text=std.course
    }
}
}