package com.example.textbook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME="textbook.db"
        private const val TBL_TEXTBOOK= "tbl_textbook"
        private const val ID= "id"
        private const val ISBN= "isbn"
        private const val TITLE= "title"
        private const val AUTHOR= "author"
        private const val COURSE= "course"//name of each column which are stored in a variable

    }

    override fun onCreate(db: SQLiteDatabase?){//create table
        val createTblTextbook = ("CREATE TABLE " + TBL_TEXTBOOK + "("
                +ID+ " INTEGER PRIMARY KEY," + ISBN + " TEXT,"
                +TITLE+ " TEXT," + AUTHOR+ " TEXT," +COURSE+ " TEXT" + ")"
                )
        db?.execSQL(createTblTextbook)
    }

    override fun onUpgrade(db:SQLiteDatabase?, oldVersion: Int, newVersion: Int){
       db!!.execSQL("DROP TABLE IF EXISTS $TBL_TEXTBOOK")//if a table already exists with same name drop it
        onCreate(db)
    }

    fun insertText(std:TextModel):Long{
        val db = this.writableDatabase

        val values= ContentValues()
        values.put(ID, std.id)
        values.put(ISBN, std.isbn)
        values.put(TITLE, std.title)
        values.put(AUTHOR, std.author)
        values.put(COURSE, std.course)

        val success= db.insert(TBL_TEXTBOOK, null, values)
        db.close()
        return success
    }

    fun getTextbooks(): ArrayList<TextModel>{
        val txtList: ArrayList<TextModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_TEXTBOOK"
        val db=this.readableDatabase

        val cursor: Cursor?

        try {
            cursor=db.rawQuery(selectQuery, null)
        }catch(e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id :Int
        var isbn: String
        var title: String
        var author:String
        var course:String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                isbn = cursor.getString(cursor.getColumnIndexOrThrow("isbn"))
                title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                author = cursor.getString(cursor.getColumnIndexOrThrow("author"))
                course = cursor.getString(cursor.getColumnIndexOrThrow("course"))

                val std =
                    TextModel(id = id, isbn = isbn, title = title, author = author, course = course)
                txtList.add(std)
            } while (cursor.moveToNext())
        }
        return txtList
        }

    fun updateText(std: TextModel): Int{
        val db = this.writableDatabase
        val values=ContentValues()
        values.put(ID, std.id)
        values.put(ISBN, std.isbn)
        values.put(TITLE, std.title)
        values.put(AUTHOR, std.author)
        values.put(COURSE, std.course)

        val success = db.update(TBL_TEXTBOOK, values, "id=" +std.id, null)
        db.close()
        return success
    }

    fun deleteText(id:Int):Int{
        val db = this.writableDatabase

        val values=ContentValues()
        values.put(ID, id)
        val success=db.delete(TBL_TEXTBOOK, "id=$id", null)
        db.close()
        return success
    }
    }


