package com.example.hatran_todo.interfaces
import com.example.hatran_todo.Todo
interface IRepo {
   fun getCount(): Int
    fun replace(todo: Todo)
    fun remove(todo: Todo)
    fun getTodo(idx: Int):Todo
    fun findAll()
    fun addTodo(todo: Todo)
    fun isCompleted(todo: Todo)
    fun findCompleted()
    fun findActive()
 fun search(word:String):MutableList<Todo>
 fun reset()


}