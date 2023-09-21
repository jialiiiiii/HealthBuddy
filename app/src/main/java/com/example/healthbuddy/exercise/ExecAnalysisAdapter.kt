package com.example.healthbuddy.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.database.UserExecData
import com.example.healthbuddy.databinding.ExecAnalysisItemBinding

class ExecAnalysisAdapter: RecyclerView.Adapter<ExecAnalysisAdapter.MyViewHolder>() {

    private var execData = emptyList<UserExecData>()

    class MyViewHolder(private val binding: ExecAnalysisItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentData: UserExecData){
            binding.dateTime.text = currentData.exec_date.toString() + ", " + currentData.exec_time.toString()
            binding.exerciseTypeSelected.text = currentData.exec_type.toString()
            binding.exerciseCategorySelected.text = currentData.exec_category.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolderBinding = ExecAnalysisItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(viewHolderBinding)
    }

    override fun getItemCount(): Int {
        return execData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = execData[position]
        holder.bind(currentData)
    }

    // Method to update the data
    fun setData(newData: List<UserExecData>) {
        execData = newData
        notifyDataSetChanged()
    }
}

























