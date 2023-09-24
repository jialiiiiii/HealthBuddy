import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Suggestion

class SuggestExecAdapter(
    private val context: Context,
    private val suggestions: MutableList<Suggestion>
) : RecyclerView.Adapter<SuggestExecAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.exec_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.exec_desc)
        val imageView: ImageView = itemView.findViewById(R.id.exec_img)
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
}