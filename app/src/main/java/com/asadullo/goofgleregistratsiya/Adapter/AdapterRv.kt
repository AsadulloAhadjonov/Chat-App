package com.asadullo.goofgleregistratsiya.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asadullo.goofgleregistratsiya.Models.User
import com.asadullo.goofgleregistratsiya.databinding.ItemRvBinding
import com.squareup.picasso.Picasso

class AdapterRv(var list: ArrayList<User>, var rvClick: RvClick) : RecyclerView.Adapter<AdapterRv.Vh>() {
    inner class Vh(var item: ItemRvBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: User, position: Int) {
            Picasso.get().load("${user.imgLink}")
                .into(item.usersImg)
            item.usersName.text = user.displayName
            item.root.setOnClickListener {
                rvClick.click(list, position, user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    interface RvClick{
        fun click(list: ArrayList<User>, position: Int, user: User)
    }

}