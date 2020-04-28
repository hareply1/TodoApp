package com.example.hatran_todo

import android.content.Context
import com.example.hatran_todo.interfaces.IRepo


class Repo ( ctx : Context) : IRepo{



    private var todostemp: MutableList<Todo> = mutableListOf()
    var location: Int
    private val db: ITodoDataBase

    init {
        location = 0
       db = TodoDataBase(ctx)
        todostemp.addAll(db.getTodos())

    }
    override fun getCount(): Int {
        return todostemp.size
    }


    override fun replace(todo: Todo) {

        db.updateTodo(todo)

        var position = 0

        val id = todo.id


        while (position < todostemp.size){
            if (todostemp.get(position).id == id){
                todostemp.set(position, todo)
                break
            }
            position++
        }

    }

    override fun remove(todo: Todo) {
        var position = 0

        db.deleteTodo(todo)

        val id = todo.id


        while (position < todostemp.size){
            if (todostemp.get(position).id == id){
                todostemp.removeAt(position)
                break
            }
            position++
        }
    }

    override fun getTodo(idx: Int): Todo {

        return todostemp.get(idx)
    }

    override fun addTodo(todo: Todo) {

        db.addTodo(todo)
        when(location){
            0 -> findAll()
            1 -> findActive()
            2 -> findCompleted()
        }
    }

    override fun isCompleted(todo: Todo) {

        val check = !todo.complete

        todo.complete = check

        db.updateTodo(todo)


        var position = 0
        val id = todo.id


        while (position < todostemp.size){
            if (todostemp.get(position).id == id){
                todostemp.get(position).complete = check
                break
            }
            position++
        }
    }

    override fun findCompleted() {
        location = 2
        var position = 0
        todostemp.clear()
        while (position < db.getTodos().size){
            if (db.getTodos().get(position).complete){
                todostemp.add(db.getTodos().get(position))
            }
            position++
        }
    }

    override fun findActive() {
        location = 1
        var position = 0
        todostemp.clear()
        while (position < db.getTodos().size){
            if (!db.getTodos().get(position).complete){
                todostemp.add(db.getTodos().get(position))
            }
            position++
        }
    }

    override fun search(word: String): MutableList<Todo> {
        var todos: MutableList<Todo> = mutableListOf()
        todos.addAll(db.getTodos())
        todostemp.clear()
        val search = word.toLowerCase()
        todos.forEach {
            if (it.title.toLowerCase().contains(search) || it.content.toLowerCase().contains(search)){
                todostemp.add(it)

            }
        }
        return todostemp
    }

    override fun reset() {
        when(location){
            0 -> findAll()
            1 -> findActive()
            2 -> findCompleted()
        }
    }

    override fun findAll() {
        location = 0
        todostemp.clear()
        todostemp.addAll(db.getTodos())
    }



}