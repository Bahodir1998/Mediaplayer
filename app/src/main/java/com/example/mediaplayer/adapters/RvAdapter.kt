package com.example.mediaplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.Mp3Player
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.Mp3ItemBinding


class RvAdapter(var list: ArrayList<Mp3Player>,var myInterface: MyInterface) :RecyclerView.Adapter<RvAdapter.Vh>(){

    inner class Vh(itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind(mp3Player: Mp3Player,position: Int){
            val bind = Mp3ItemBinding.bind(itemView)
            bind.name.text = mp3Player.title
            bind.title.text = mp3Player.artist

            bind.img.setImageURI(mp3Player.img)
            if(bind.img.drawable == null){
                bind.img.setImageResource(R.drawable.imusic)
            }
            itemView.setOnClickListener {
                myInterface.click(mp3Player,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.mp3_item, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MyInterface{
        fun click(mp3Player: Mp3Player,position: Int)
    }
}