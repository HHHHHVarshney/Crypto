package com.example.crypto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.crypto.databinding.RvItemBinding

class rv_adapter(val context: Context, var data:ArrayList<rv_model>):RecyclerView.Adapter<rv_adapter.viewHolder>() {

    fun changeData(filteredData: ArrayList<rv_model>) {
        data = filteredData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = RvItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        setAnimation(holder.itemView)
        holder.binding.name.text = data.get(position).name
        holder.binding.symbol.text = data.get(position).symbol
        holder.binding.price.text = data.get(position).price
    }

    class viewHolder(val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    fun setAnimation(view: View) {
        val animation = AlphaAnimation(0.0f,1.0f)
        animation.duration = 1000
        view.startAnimation(animation)
    }

}