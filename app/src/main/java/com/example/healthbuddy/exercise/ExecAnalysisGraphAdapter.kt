package com.example.healthbuddy.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.database.TotalCaloriesBurnt
import com.example.healthbuddy.databinding.ExecAnalysisItemBinding

class ExecAnalysisGraphAdapter: RecyclerView.Adapter<ExecAnalysisGraphAdapter.MyViewHolder>() {

    private var totalCaloriesBurnt = emptyList<TotalCaloriesBurnt>()

    class MyViewHolder(private val binding: ExecAnalysisItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentData: TotalCaloriesBurnt){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExecAnalysisGraphAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolderBinding = ExecAnalysisItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(viewHolderBinding)
    }

    override fun getItemCount(): Int {
        return totalCaloriesBurnt.size
    }

    override fun onBindViewHolder(holder: ExecAnalysisGraphAdapter.MyViewHolder, position: Int) {
        val currentData = totalCaloriesBurnt[position]
        holder.bind(currentData)
    }


}