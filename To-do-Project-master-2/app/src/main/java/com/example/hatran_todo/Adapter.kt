package com.example.hatran_todo

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hatran_todo.interfaces.IControl
import com.example.hatran_todo.interfaces.IRepo
import kotlinx.android.synthetic.main.todolist.view.*

 class Adapter (val controller:IControl): RecyclerView.Adapter<CustomViewHolder>(){

     override fun getItemCount(): Int {

         return controller.getCurrentCount()
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
         val layoutInflater = LayoutInflater.from(parent?.context).inflate(R.layout.todolist, parent, false)
         val viewHolder = CustomViewHolder(layoutInflater)

         layoutInflater.completeview.setOnClickListener {
             val position = viewHolder.adapterPosition
             val todo = controller.todostemp.getTodo(position)
             controller.complete(todo)
             this.notifyItemChanged(position)

         }

         layoutInflater.setOnClickListener {
             val position = viewHolder.adapterPosition

             val todo = controller.todostemp.getTodo(position)
             val idx = position
             controller.launchEdit(idx, todo)

         }

         layoutInflater.setOnLongClickListener {
             val position = viewHolder.adapterPosition
             val dialogBuilder = AlertDialog.Builder(layoutInflater.context)
             val todo = controller.todostemp.getTodo(position)


             dialogBuilder.setMessage("Do you want to delete?")

                 .setCancelable(false)

                 .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                         dialog, id -> controller.deleteTodo(todo)
                     this.notifyItemRemoved(position)
                 })

                 .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                         dialog, id -> dialog.cancel()
                 })


             val alert = dialogBuilder.create()

             alert.setTitle(controller.todostemp.getTodo(position).title)

             alert.show()

             return@setOnLongClickListener true
         }
         return viewHolder
     }

     override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
         val todo = controller.todostemp.getTodo(position)

         holder.BindTodo(todo)
     }


 }

class CustomViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun BindTodo(todo:Todo?){


        if (todo!!.complete) {
            itemView.titleview_txt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.todoview_txt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        else{
            itemView.titleview_txt.paintFlags = 0
            itemView.todoview_txt.paintFlags = 0
        }
        //Todo need to make sure its not null
        if (todo.content.length > 12){
            itemView.todoview_txt.text = todo.content.substring(0,11) + "..."
        }
        else{
            itemView.todoview_txt.text = todo.content
        }

        if (todo.title.length > 10){
            itemView.titleview_txt.text = todo.title.substring(0, 9) + "..."
        }
        else{
            itemView.titleview_txt.text = todo.title
        }


        itemView.dateview_txt.text = todo.date
        itemView.completeview.isChecked = todo.complete
    }

}
