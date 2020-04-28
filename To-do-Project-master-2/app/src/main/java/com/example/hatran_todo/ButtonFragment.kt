package com.example.hatran_todo


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hatran_todo.interfaces.IButton
import com.example.hatran_todo.interfaces.IControl
import com.example.hatran_todo.interfaces.IRepo
import kotlinx.android.synthetic.main.fragment_button.*
import kotlinx.android.synthetic.main.fragment_button.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*



class ButtonFragment : Fragment() , View.OnClickListener {
    interface OnListChange{
        enum class Change {ALL, ACTIVE, COMPLETED}
        fun OnListChange(change:Change)
    }

    private var currentChange:OnListChange.Change = OnListChange.Change.ALL
    private var listener: OnListChange? = null
    //lateinit var newL:ITodoButton




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_button, container, false)

        view.all_btn.setOnClickListener(this)
        view.active_btn.setOnClickListener(this)
        view.completed_btn.setOnClickListener(this)


        return view
    }

    fun setOnListChange(listener:OnListChange){
        this.listener = listener
    }

    fun getCurrentList(): OnListChange.Change{
        return currentChange
    }

    override fun onClick(view: View?) {

        currentChange = when(view?.id){
            R.id.all_btn -> {
                OnListChange.Change.ALL
            }
            R.id.active_btn -> {
                OnListChange.Change.ACTIVE

            }
            R.id.completed_btn -> {
                OnListChange.Change.COMPLETED

            }
            else -> {
                OnListChange.Change.ALL
            }
        }

        listener?.OnListChange(currentChange)

    }



}
