package com.example.hatran_todo

data class Todo(
    var id:Int,
    var title:String,
    var content:String,
    var complete:Boolean,

    var date:String
)