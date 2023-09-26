import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Suggestion

class SuggestExecAdapter(
    private val context: Context,
    private val suggestions: MutableList<Suggestion>
) : RecyclerView.Adapter<SuggestExecAdapter.ViewHolder>() {

    private val sharedPreferences = context.getSharedPreferences("HealthBuddyPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.exec_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.exec_desc)
        val imageView: ImageView = itemView.findViewById(R.id.exec_img)
        val starBtn: ImageButton = itemView.findViewById((R.id.exec_star))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exec_suggestion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = suggestions[position]

        // Bind data to views
        holder.titleTextView.text = suggestion.postTitle
        val fullDescription = suggestion.postDescription ?: ""
        val maxShortenLength = 100 // Adjust the maximum length as needed

        // Check if the full description is longer than the maximum length
        if (fullDescription.length > maxShortenLength) {
            // Show the shortened description with "Read More"
            val shortenedText = fullDescription.substring(0, maxShortenLength) + "... Read More"

            // Create a SpannableString to make "Read More" clickable
            val spannable = SpannableString(shortenedText)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // Handle "Read More" click here
                    holder.descriptionTextView.text = fullDescription
                }
            }

            // Apply the ClickableSpan to "Read More"
            spannable.setSpan(clickableSpan, maxShortenLength + 1, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Set the text and make it clickable
            holder.descriptionTextView.text = spannable
            holder.descriptionTextView.movementMethod = LinkMovementMethod.getInstance()
        } else {
            // If the description is shorter, display it without "Read More"
            holder.descriptionTextView.text = fullDescription
        }

        holder.imageView.setImageBitmap(suggestion.postImage)

        // Check if the post is a favorite and set the starBtn accordingly
        val favoritePostIds = sharedPreferences.getStringSet("favoriteExecIds", mutableSetOf()) ?: mutableSetOf()
        holder.starBtn.isSelected = favoritePostIds.contains(suggestion.postId.toString())


        // Check if the post is a favorite and set the appropriate image drawable
        if (favoritePostIds.contains(suggestion.postId.toString())) {
            // The post is a favorite, set the filled heart icon
            holder.starBtn.setImageResource(R.drawable.ic_star_filled)
        } else {
            // The post is not a favorite, set the outline heart icon
            holder.starBtn.setImageResource(R.drawable.ic_star_outlined)
        }

        holder.starBtn.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val post = suggestions[position]
                toggleFavorite(suggestion.postId.toString(), holder.starBtn)
            }
        }
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    // Function to update the dataset
    fun updateData(newData: List<Suggestion>) {
        suggestions.clear()
        suggestions.addAll(newData)
        notifyDataSetChanged()
    }

    private fun toggleFavorite(postId: String, starBtn: ImageButton) {
        val favoritePostIds = sharedPreferences.getStringSet("favoriteExecIds", mutableSetOf()) ?: mutableSetOf()

        if (favoritePostIds.contains(postId)) {
            // Toggle off
            favoritePostIds.remove(postId)
            editor.putStringSet("favoriteExecIds", favoritePostIds)
            editor.apply()

            // Remove
            editor.remove("favoriteExecIds").commit()
            editor.putStringSet("favoriteExecIds", favoritePostIds).commit()
            editor.apply()

            starBtn.setImageResource(R.drawable.ic_star_outlined)
        } else {
            // Toggle on
            favoritePostIds.add(postId)
            editor.putStringSet("favoriteExecIds", favoritePostIds)
            editor.apply()

            // Remove
            editor.remove("favoriteExecIds").commit()
            editor.putStringSet("favoriteExecIds", favoritePostIds).commit()
            editor.apply()

            starBtn.setImageResource(R.drawable.ic_star_filled)
        }
    }
}