package com.example.healthbuddy.nutrition

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.R
import com.example.healthbuddy.database.UserNutritionData
import android.content.res.Resources
import android.widget.ImageView

class NutriAnalysisAdapter(private val resources: Resources) : RecyclerView.Adapter<NutriAnalysisAdapter.NutriDataViewHolder>() {

    private var nutriData = emptyList<UserNutritionData>()
    private var actionDelete: ((UserNutritionData) -> Unit)? = null
    private var actionEdit: ((UserNutritionData) -> Unit)? = null

    private var nutriCategory: Array<String> = emptyArray()
    private var nutriType: Array<String> = emptyArray()
    private var calories: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutriDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nutri_analysis_item, parent, false)

        return NutriDataViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nutriData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NutriDataViewHolder, position: Int) {
        val currentData = nutriData[position]
        calories = resources.getString(R.string.cal)

        // Initialize array
        nutriCategory = resources.getStringArray(R.array.food_group)
        when(currentData.food_category){
            0 -> nutriType = resources.getStringArray(R.array.fruits_selection)
            1 -> nutriType = resources.getStringArray(R.array.vegetables_selection)
            2 -> nutriType = resources.getStringArray(R.array.grains_selection)
            3 -> nutriType = resources.getStringArray(R.array.protein_foods_selection)
            4 -> nutriType = resources.getStringArray(R.array.dairy_selection)
        }

        holder.dateTime.text = currentData.intake_date.toString() + ", " + currentData.intake_time.toString()
        holder.foodTypeSelected.text = nutriType[currentData.food_type]
        holder.foodCategorySelected.text = nutriCategory[currentData.food_category]
        holder.caloriesGained.text = "+ " + currentData.cal_obtained + " " + calories

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
        val deleteBtn: ImageView = itemView.findViewById<ImageView>(R.id.delete_btn)
        val editBtn: ImageView = itemView.findViewById<ImageView>(R.id.edit_btn)
    }
}