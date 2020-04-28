package com.example.hatran_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.content.Intent

import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_todo.*
import java.text.SimpleDateFormat
import java.util.*


class EditTodo : AppCompatActivity(), View.OnClickListener {

    override fun onClick(view: View?) {
        val titletest = edittitle.text
        val contenttest = editText.text
        if (titletest.isEmpty() || contenttest.isEmpty()){
            Toast.makeText(this,"Missing Title or Content", Toast.LENGTH_SHORT).show()
        }
        else{
            val idx = intent.getIntExtra("Idx",-1)
            val id = intent.getIntExtra("Id",-1)
            val intent = Intent()
            val today = Calendar.getInstance()
            val date = SimpleDateFormat("EEE MMMM d H:m:s Y").format(today.time)
            val title = edittitle.text.toString()
            val text = editText.text.toString()
            val complete = editcheckBox.isChecked

            val todo = Todo(id, title, text, complete, date)
            val json = Gson().toJson(todo)
            intent.putExtra(TODOE_EXTRA_KEY, json)
            intent.putExtra(POSITION_KEY, idx)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)


        val json = intent.getStringExtra("Todo")

        val todo = Gson().fromJson<Todo>(json, Todo::class.java)


        edittitle.setText(todo.title)
        editText.setText(todo.content)
        editcheckBox.isChecked = todo.complete



        saveedit_btn.setOnClickListener(this)


    }
    companion object{
        val TODOE_EXTRA_KEY = "Todo"
        val POSITION_KEY = "idx"
    }
}
