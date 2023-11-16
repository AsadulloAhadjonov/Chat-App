package com.asadullo.goofgleregistratsiya.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asadullo.goofgleregistratsiya.Models.Groups
import com.asadullo.goofgleregistratsiya.databinding.ItemRv2Binding

class AdapterGroups(var list: ArrayList<Groups>) : RecyclerView.Adapter<AdapterGroups.Vh>() {
    inner class Vh(var item: ItemRv2Binding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: Groups) {
            item.groupName.text = user.groupName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRv2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

}