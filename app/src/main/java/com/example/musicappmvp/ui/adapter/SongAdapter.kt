package com.example.musicappmvp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicappmvp.R
import com.example.musicappmvp.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class songAdapter(private val listener: onItemClickListener) :
    RecyclerView.Adapter<songAdapter.MyViewHolder>() {
    private var listSong = mutableListOf<Song>()

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun updateData(songsList: List<Song>) {
        listSong.clear()
        listSong.addAll(songsList)
        notifyDataSetChanged()
    }

   inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        fun bindData(song: Song) {
            itemView.apply {
                textview_title.text = song.Title
                textview_author.text = song.Author
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(listSong[position])
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

}
