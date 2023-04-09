package com.example.intellilearnteacherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.available_notes_list.view.*
import kotlinx.android.synthetic.main.table_row.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterNoteItem : RecyclerView.Adapter<AdapterNoteItem.MyViewHolder>() {

    private var list = ArrayList<NoteItem>()
    var onDeleteNoteClickListener:OnDeleteNoteClickListener? = null

    interface OnDeleteNoteClickListener{

        fun onDeleteNote(note : NoteItem, position: Int)
        fun onEditNote(note : NoteItem)

    }



    inner class MyViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.available_notes_list, parent, false)){


        fun bind(note_item : NoteItem) = with(itemView){

            note_title.text =  note_item.title


            val outputDateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
            note_created_on.text= outputDateFormat.format(note_item.created_at)

            note_content.text = note_item.content


            btnDelete.setOnClickListener{

                onDeleteNoteClickListener?.onDeleteNote(note_item, adapterPosition)

            }
            aNote.setOnClickListener{
                onDeleteNoteClickListener?.onEditNote(note_item)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun setList(list : ArrayList<NoteItem>){

        this.list = list
        notifyDataSetChanged()

    }

    fun addItem(note: NoteItem){

        list.add(note)
        notifyItemInserted(list.size - 1)

    }

    fun deleteNote(position: Int){

        list.removeAt(position)
        notifyItemRemoved(position)

    }

}