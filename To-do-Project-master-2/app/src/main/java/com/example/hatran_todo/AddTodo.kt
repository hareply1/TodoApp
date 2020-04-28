package com.example.hatran_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent

import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_todo.*
import java.text.SimpleDateFormat
import java.util.*


class AddTodo : AppCompatActivity(), View.OnClickListener {

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.save_btn -> {
                val titletest = title_txt.text
                val contenttest = content_txt.text
                if (titletest.isEmpty() || contenttest.isEmpty()){
                    Toast.makeText(this,"Missing Title or Content", Toast.LENGTH_SHORT).show()
                }
                else{
                    val intent = Intent()
                    val today = Calendar.getInstance()
                    val date = SimpleDateFormat("EEE MMMM d H:m:s Y").format(today.time)
                    val title = title_txt.text.toString()
                    val text = content_txt.text.toString()
                    val complete = iscompleted_check.isChecked

                    val todo = Todo(-1,title, text, complete, date)
                    val json = Gson().toJson(todo)
                    intent.putExtra(TODO_EXTRA_KEY, json)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        save_btn.setOnClickListener(this)

    }

    companion object{
        val TODO_EXTRA_KEY = "Todo"
    }
}
