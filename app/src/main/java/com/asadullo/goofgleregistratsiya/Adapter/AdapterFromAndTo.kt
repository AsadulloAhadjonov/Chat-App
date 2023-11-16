package com.asadullo.goofgleregistratsiya.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asadullo.goofgleregistratsiya.Models.MyMessage
import com.asadullo.goofgleregistratsiya.databinding.ItemFromBinding
import com.asadullo.goofgleregistratsiya.databinding.ItemToBinding
import com.squareup.picasso.Picasso

class AdapterFromAndTo( val uid:String,  val list: ArrayList<MyMessage>): RecyclerView.Adapter<ViewHolder>() {

    inner class FromVh(var itemFrom : ItemFromBinding):ViewHolder(itemFrom.root){
        fun onBind(myMessage: MyMessage) {
            if (myMessage.text!!.contains("my_images")) {
                itemFrom.txtMessage.visibility = View.GONE
                itemFrom.img.visibility = View.VISIBLE
                Picasso.get().load(myMessage.text).into(itemFrom.img)
            }else if (myMessage.text!!.contains("my_videos")) {
                itemFrom.txtMessage.visibility = View.GONE
                itemFrom.video.visibility = View.VISIBLE
                val mediaController = MediaController(itemFrom.root.context)
                mediaController.setAnchorView(itemFrom.video)
                itemFrom.video.setMediaController(mediaController)
                val videoUri = Uri.parse(myMessage.text)
                itemFrom.video.setVideoURI(videoUri)
                itemFrom.video.requestFocus()
                itemFrom.video.start()
            }else{
                itemFrom.txtMessage.visibility = View.VISIBLE
                itemFrom.img.visibility = View.GONE
                itemFrom.txtMessage.text = myMessage.text
            }
        }
    }

    inner class ToVh(var itemTo:ItemToBinding):ViewHolder(itemTo.root){
        fun onBind(myMessage: MyMessage) {
            if (myMessage.text!!.contains("my_images")) {
                itemTo.txtMessage.visibility = View.GONE
                itemTo.img.visibility = View.VISIBLE
                Picasso.get().load(myMessage.text).into(itemTo.img)
            }else if (myMessage.text!!.contains("my_videos")){
                itemTo.txtMessage.visibility = View.GONE
                itemTo.verticalOnly.visibility = View.VISIBLE
                val mediaController = MediaController(itemTo.root.context)
                mediaController.setAnchorView(itemTo.verticalOnly)
                itemTo.verticalOnly.setMediaController(mediaController)
                val videoUri = Uri.parse(myMessage.text)
                itemTo.verticalOnly.setVideoURI(videoUri)
                itemTo.verticalOnly.requestFocus()
                itemTo.verticalOnly.start()
            }else{
                itemTo.txtMessage.visibility = View.VISIBLE
                itemTo.img.visibility = View.GONE
                itemTo.txtMessage.text = myMessage.text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1){
            return FromVh(ItemFromBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            return  ToVh((ItemToBinding.inflate(LayoutInflater.from(parent.context), parent, false)))
        }
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == 1){
            var fromVh = holder as FromVh
            fromVh.onBind(list[position])
        }else{
            var toVh = holder as ToVh
            toVh.onBind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].fromUid == uid){
            return 1
        }else return 0
    }
}