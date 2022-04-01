package com.example.textbook

import java.util.*

class TextModel(
    var id:Int = autoID(),
    var isbn:String = "",
    var title:String = "",
    var author:String = "",
    var course:String = ""
) {
companion object{
    fun autoID():Int{
        val random = Random()
        return random.nextInt(100)
    }
}

}