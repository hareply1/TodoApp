package com.example.hatran_todo.interfaces
import  com.example.hatran_todo.Todo
interface IControl {

   fun deleteTodo(todo: Todo)
    fun complete(todo: Todo)
    fun launchAdd()
    fun launchEdit(idx: Int, todo: Todo)
    fun editTodo(todo: Todo)
    fun getCurrentCount() : Int
 fun search(word:String):MutableList<Todo>
 fun reset()

    val todos: IRepo
    val todostemp: IRepo
}