package com.example.hatran_todo

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.example.hatran_todo.interfaces.IButton
import com.example.hatran_todo.interfaces.IControl
import com.example.hatran_todo.interfaces.IRepo
import kotlinx.android.synthetic.main.fragment_button.*
import kotlinx.android.synthetic.main.fragment_list.*


class MainActivity : AppCompatActivity() , IControl, IButton, ButtonFragment.OnListChange{

    override fun allButton() {
        all_btn.setBackgroundColor(Color.parseColor("#ff0000"))
        active_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        completed_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        todostemp.findAll()
        recycle_fram.adapter?.notifyDataSetChanged()
    }

    override fun activeButton() {
        all_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        active_btn.setBackgroundColor(Color.parseColor("#ff0000"))
        completed_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        todostemp.findActive()
        recycle_fram.adapter?.notifyDataSetChanged()
    }

    override fun completedButton() {
        all_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        active_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        completed_btn.setBackgroundColor(Color.parseColor("#ff0000"))
        todostemp.findCompleted()
        recycle_fram.adapter?.notifyDataSetChanged()
    }

    override lateinit var todos: IRepo
    override lateinit var todostemp:IRepo

    override fun launchAdd() {
        val intent = Intent(this, AddTodo::class.java)
        startActivityForResult(intent, ADD_TODO_REQUEST_CODE)
    }

    override fun launchEdit(idx:Int, todo: Todo) {
        val intent = Intent(this, EditTodo::class.java)
        val todostring = Gson().toJson(todo)
        intent.putExtra("Todo",todostring)
        intent.putExtra("Idx", idx)
        intent.putExtra("Id", todostemp.getTodo(idx).id)
        startActivityForResult(intent, EDIT_TODO_REQUEST_CODE)
    }

    override fun getCurrentCount() : Int {
        return todostemp.getCount()
    }

    override fun complete(todo: Todo) {

        todostemp.isCompleted(todo)
    }

    override fun deleteTodo(todo: Todo) {

        todostemp.remove(todo)
    }

    override fun editTodo(todo: Todo) {

        todostemp.replace(todo)
    }

    override fun search(word: String): MutableList<Todo> {
        return todostemp.search(word)
    }

    override fun reset() {
        todostemp.reset()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val frag = supportFragmentManager.findFragmentById(R.id.fragment_btn)

        if (frag != null){
            if (frag is ButtonFragment){
                frag.setOnListChange(this)
            }
        }

        all_btn.setBackgroundColor(Color.parseColor("#ff0000"))
        active_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))
        completed_btn.setBackgroundColor(Color.parseColor("#C92F2E2E"))



        recycle_fram.layoutManager = LinearLayoutManager(this)
        recycle_fram.adapter = Adapter(this)

        todostemp = Repo(this)

        add_todo_btn.setOnClickListener {
            launchAdd()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.search_bar, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_bar)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchItem.collapseActionView()
                Toast.makeText(this@MainActivity,"looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()){
                    search(newText)
                    recycle_fram.adapter?.notifyDataSetChanged()
                }
                else{
                    reset()
                    recycle_fram.adapter?.notifyDataSetChanged()
                }
                return true
            }
        })

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            Activity.RESULT_OK -> {
                when(requestCode){
                    ADD_TODO_REQUEST_CODE -> {
                        val json = data?.getStringExtra(AddTodo.TODO_EXTRA_KEY)
                        if (json != null){
                            val todo = Gson().fromJson<Todo>(json,Todo::class.java)
                            todostemp.addTodo(todo)
                            recycle_fram.adapter?.notifyItemInserted(todostemp.getCount())
                        }

                    }
                    EDIT_TODO_REQUEST_CODE -> {
                        val json = data?.getStringExtra(EditTodo.TODOE_EXTRA_KEY)
                        val idx = data?.getIntExtra(EditTodo.POSITION_KEY,1)
                        if (json != null && idx != null){
                            val todo = Gson().fromJson<Todo>(json,Todo::class.java)
                            editTodo(todo)
                            recycle_fram.adapter?.notifyItemChanged(idx)
                        }

                    }
                }

            }
            Activity.RESULT_CANCELED -> {
                Toast.makeText(this,"No change",Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun OnListChange(change: ButtonFragment.OnListChange.Change) {



        Toast.makeText(this,"$change", Toast.LENGTH_LONG).show()

        println(change)

        when (change){
            ButtonFragment.OnListChange.Change.ALL -> {
                allButton()
            }
            ButtonFragment.OnListChange.Change.ACTIVE -> {
                activeButton()
            }
            ButtonFragment.OnListChange.Change.COMPLETED -> {
                completedButton()
            }
        }
    }
    companion object{
        val ADD_TODO_REQUEST_CODE = 1
        val EDIT_TODO_REQUEST_CODE = 2
    }
}

