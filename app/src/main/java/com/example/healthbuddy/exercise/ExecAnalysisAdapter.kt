package com.example.healthbuddy.exercise

import ExecDataFragment
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.R
import com.example.healthbuddy.database.UserExecData
class ExecAnalysisAdapter : RecyclerView.Adapter<ExecAnalysisAdapter.ExecDataViewHolder>() {

    private var execData = emptyList<UserExecData>()
    private var actionDelete: ((UserExecData) -> Unit)? = null
    private var actionEdit: ((UserExecData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExecDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exec_analysis_item, parent, false)

        return ExecDataViewHolder(view)
    }

    override fun getItemCount(): Int {
        return execData.size
    }

    override fun onBindViewHolder(holder: ExecDataViewHolder, position: Int) {
        val currentData = execData[position]

        holder.dateTime.text = currentData.exec_date.toString() + ", " + currentData.exec_time.toString()
        holder.exerciseTypeSelected.text = currentData.exec_type.toString()
        holder.exerciseCategorySelected.text = currentData.exec_category.toString()
        holder.caloriesBurnt.text = "- " + currentData.cal_burnt.toString() + " calories"

        holder.deleteBtn.setOnClickListener {
            actionDelete?.invoke(currentData)
        }

        holder.editBtn.setOnClickListener {
            actionEdit?.invoke(currentData)
        }
    }

    // Method to update the data
    fun setData(newExecData: List<UserExecData>) {
        this.execData = newExecData
        notifyDataSetChanged()
    }

    fun setOnActionDeleteListener(callback: (UserExecData) -> Unit) {
        this.actionDelete = callback
    }

    fun setOnActionDEditListener(callback: (UserExecData) -> Unit) {
        this.actionEdit = callback
    }

    class ExecDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dateTime: TextView = itemView.findViewById<TextView>(R.id.date_time)
        val exerciseTypeSelected: TextView = itemView.findViewById<TextView>(R.id.exercise_type_selected)
        val exerciseCategorySelected: TextView = itemView.findViewById<TextView>(R.id.exercise_category_selected)
        val caloriesBurnt: TextView = itemView.findViewById<TextView>(R.id.calories_burnt)
        val deleteBtn: ImageButton = itemView.findViewById<ImageButton>(R.id.delete_btn)
        val editBtn: ImageButton = itemView.findViewById<ImageButton>(R.id.edit_btn)
    }


}