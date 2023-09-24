package com.example.healthbuddy.nutrition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.R
import com.example.healthbuddy.database.UserExecData
import com.example.healthbuddy.database.UserNutritionData

class NutriAnalysisAdapter : RecyclerView.Adapter<NutriAnalysisAdapter.NutriDataViewHolder>() {

    private var nutriData = emptyList<UserNutritionData>()
    private var actionDelete: ((UserNutritionData) -> Unit)? = null
    private var actionEdit: ((UserNutritionData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutriDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nutri_analysis_item, parent, false)

        return NutriDataViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nutriData.size
    }

    override fun onBindViewHolder(holder: NutriDataViewHolder, position: Int) {
        val currentData = nutriData[position]

        holder.dateTime.text = currentData.intake_date.toString() + ", " + currentData.intake_time.toString()
        holder.foodTypeSelected.text = currentData.food_type.toString()
        holder.foodCategorySelected.text = currentData.food_category.toString()
        holder.caloriesGained.text = "+ " + currentData.cal_obtained.toString() + R.string.cal

        holder.deleteBtn.setOnClickListener {
            actionDelete?.invoke(currentData)
        }

        holder.editBtn.setOnClickListener {
            actionEdit?.invoke(currentData)
        }
    }

    // Method to update the data
    fun setData(newNutriData: List<UserNutritionData>) {
        this.nutriData = newNutriData
        notifyDataSetChanged()
    }

    fun setOnActionDeleteListener(callback: (UserNutritionData) -> Unit) {
        this.actionDelete = callback
    }

    fun setOnActionDEditListener(callback: (UserNutritionData) -> Unit) {
        this.actionEdit = callback
    }

    class NutriDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dateTime: TextView = itemView.findViewById<TextView>(R.id.date_time)
        val foodTypeSelected: TextView = itemView.findViewById<TextView>(R.id.food_type_selected)
        val foodCategorySelected: TextView = itemView.findViewById<TextView>(R.id.food_category_selected)
        val caloriesGained: TextView = itemView.findViewById<TextView>(R.id.calories_gained)
        val deleteBtn: ImageButton = itemView.findViewById<ImageButton>(R.id.delete_btn)
        val editBtn: ImageButton = itemView.findViewById<ImageButton>(R.id.edit_btn)
    }
}