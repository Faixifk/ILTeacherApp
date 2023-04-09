package com.example.intellilearnteacherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.available_mcqs_list.view.*

class AdapterMcqItem : RecyclerView.Adapter<AdapterMcqItem.MyViewHolder>() {

    private var list = ArrayList<McqItem>()
    var onDeleteMcqClickListener:OnDeleteMcqClickListener? = null

    interface OnDeleteMcqClickListener{

        fun onDeleteMcq(mcq : McqItem, position: Int)

    }


    inner class MyViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.available_mcqs_list, parent, false)){


        fun bind(mcq_item : McqItem) = with(itemView){

            question.text = mcq_item.question
            option_a.text = mcq_item.option_a
            option_b.text = mcq_item.option_b
            option_c.text = mcq_item.option_c
            option_d.text = mcq_item.option_d
            weight.text = buildString {
                            append("Weight: ")
                            append(mcq_item.weight.toString())
                        }
            btnDelete.setOnClickListener{

                onDeleteMcqClickListener?.onDeleteMcq(mcq_item, adapterPosition)



            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun setList(list : ArrayList<McqItem>){

        this.list = list
        notifyDataSetChanged()

    }

    fun addItem(mcq : McqItem){

        list.add(mcq)
        notifyItemInserted(list.size - 1)

    }

    fun deleteMcq(position: Int){

        list.removeAt(position)
        notifyItemRemoved(position)

    }

}