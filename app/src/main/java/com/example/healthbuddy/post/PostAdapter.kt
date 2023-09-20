package com.example.healthbuddy.post

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbuddy.R
import com.example.healthbuddy.database.Post

class PostAdapter(private val postList:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.PostHolder>() {

    private lateinit var plistener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        plistener=listener
    }

    class PostHolder(postView: View,listener: OnItemClickListener):RecyclerView.ViewHolder(postView){
        val image:ImageView=postView.findViewById(R.id.image)
        val title:TextView=postView.findViewById(R.id.Title)

        init {
            postView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val postView = LayoutInflater.from(parent.context).inflate(R.layout.post,
            parent,false)
        return PostHolder(postView,plistener)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = postList[position]
        holder.title.text = currentPost.postTitle.toString()
        val bytes = android.util.Base64.decode(currentPost.postImage,android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.image.setImageBitmap(bitmap)
    }
}