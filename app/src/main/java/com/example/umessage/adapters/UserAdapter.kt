package com.example.umessage.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.umessage.R
import com.example.umessage.activities.ChatActivity
import com.example.umessage.models.User
import com.squareup.picasso.Picasso

class UserAdapter(val context: Context, val userList: ArrayList<User> ) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_new_row_message,parent,false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.username.text = currentUser.username
        Picasso.get().load(currentUser.profileImageUrl).into(holder.avatar)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name",currentUser.username)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.username_new_message)
        val avatar = itemView.findViewById<ImageView>(R.id.avatar_new_message)

    }
}