package com.example.hatran_todo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.hatran_todo.interfaces.IRepo
import android.content.ContentValues
object TodoConract {
    object TodoEntry : BaseColumns {
        const val TABLE_NAME = "todos"
        const val COLUMN_NAME_TITLE = "todo_title"
        const val COLUMN_NAME_CONTENT = "todo_content"
        const val COLUMN_NAME_COMPLETED = "todo_completed"
        const val COLUMN_NAME_DATE = "todo_date"
        const val COLUMN_NAME_DELETED = "deleted"
    }
}

private const val CREATE_TODO_TABEL = "CREATE TABLE ${TodoConract.TodoEntry.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "${TodoConract.TodoEntry.COLUMN_NAME_TITLE} TEXT, " +
        "${TodoConract.TodoEntry.COLUMN_NAME_CONTENT} TEXT, " +
        "${TodoConract.TodoEntry.COLUMN_NAME_COMPLETED} BOOL, " +
        "${TodoConract.TodoEntry.COLUMN_NAME_DATE} Text, "+
        "${TodoConract.TodoEntry.COLUMN_NAME_DELETED} BOOL DEFAULT 0" +
        ")"

interface ITodoDataBase{
    fun getTodos(): MutableList<Todo>
    fun getTodo(idx: Int): Todo
    fun addTodo(todo: Todo)
    fun updateTodo(todo: Todo)
    fun deleteTodo(todo: Todo)
}


private const val DELETE_TODO_TABLE = "DROP TABLE IF EXISTS ${TodoConract.TodoEntry.TABLE_NAME}"


class TodoDataBase (ctx: Context) : ITodoDataBase {


    class TodoDbHelper(ctx: Context): SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION){
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_TODO_TABEL)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL(DELETE_TODO_TABLE)
            onCreate(db)
        }


        companion object{
            val DATABASE_NAME = "todos.db"
            val DATABASE_VERSION = 1
        }

    }

    private val db: SQLiteDatabase

    init {
        db = TodoDbHelper(ctx).writableDatabase
    }


    override fun deleteTodo(todo: Todo) {
        val cvs = toContentValues(todo)
        cvs.put(TodoConract.TodoEntry.COLUMN_NAME_DELETED, "1")
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(todo.id.toString())
        db.update(TodoConract.TodoEntry.TABLE_NAME, cvs, selection, selectionArgs)
    }

    override fun getTodo(idx: Int): Todo {
        val project = arrayOf(BaseColumns._ID, TodoConract.TodoEntry.COLUMN_NAME_TITLE, TodoConract.TodoEntry.COLUMN_NAME_CONTENT,
            TodoConract.TodoEntry.COLUMN_NAME_COMPLETED, TodoConract.TodoEntry.COLUMN_NAME_DATE)
        val sortorder = "${BaseColumns._ID} ASC"
        val selection = "${TodoConract.TodoEntry.COLUMN_NAME_DELETED} = ? AND ${BaseColumns._ID} = ?"
        val selectionArg = arrayOf("0", idx.toString())

        val cursor = db.query(
            TodoConract.TodoEntry.TABLE_NAME,
            project,
            selection,
            selectionArg,
            null,
            null,
            sortorder
        )
        val todos = mutableListOf<Todo>()
        with(cursor){
            while (cursor.moveToNext()){

                val id = getInt(getColumnIndex(BaseColumns._ID))
                val title = getString(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_TITLE))
                val content = getString(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_CONTENT))
                val completed = getInt(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_COMPLETED)) > 0
                val data = getString(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_DATE))
                val todo = Todo(id, title, content, completed, data)

                todos.add(todo)

            }
        }

        return todos[0]

    }

    override fun getTodos(): MutableList<Todo> {

        val project = arrayOf(BaseColumns._ID, TodoConract.TodoEntry.COLUMN_NAME_TITLE, TodoConract.TodoEntry.COLUMN_NAME_CONTENT,
            TodoConract.TodoEntry.COLUMN_NAME_COMPLETED, TodoConract.TodoEntry.COLUMN_NAME_DATE)
        val sortorder = "${BaseColumns._ID} ASC"
        val selection = "${TodoConract.TodoEntry.COLUMN_NAME_DELETED} = ?"
        val selectionArg = arrayOf("0")

        val cursor = db.query(
            TodoConract.TodoEntry.TABLE_NAME,
            project,
            selection,
            selectionArg,
            null,
            null,
            sortorder
        )
        val todos = mutableListOf<Todo>()
        with(cursor){
            while (cursor.moveToNext()){

                val id = getInt(getColumnIndex(BaseColumns._ID))
                val title = getString(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_TITLE))
                val content = getString(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_CONTENT))
                val completed = getInt(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_COMPLETED)) > 0
                val data = getString(getColumnIndex(TodoConract.TodoEntry.COLUMN_NAME_DATE))
                val todo = Todo(id, title, content, completed, data)

                todos.add(todo)

            }
        }
        return todos
    }

    override fun addTodo(todo: Todo) {
        val cvs = toContentValues(todo)
        db.insert(TodoConract.TodoEntry.TABLE_NAME, null, cvs)
    }

    override fun updateTodo(todo: Todo) {
        val cvs = toContentValues(todo)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(todo.id.toString())
        db.update(TodoConract.TodoEntry.TABLE_NAME, cvs, selection, selectionArgs)
    }



    private fun toContentValues(todo: Todo): ContentValues {
        val cv = ContentValues()
        cv.put(TodoConract.TodoEntry.COLUMN_NAME_TITLE, todo.title)
        cv.put(TodoConract.TodoEntry.COLUMN_NAME_CONTENT, todo.content)
        cv.put(TodoConract.TodoEntry.COLUMN_NAME_COMPLETED, todo.complete)
        cv.put(TodoConract.TodoEntry.COLUMN_NAME_DATE, todo.date)

        return cv
    }
}