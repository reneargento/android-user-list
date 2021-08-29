package com.random.user.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.random.user.R

class UserAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<UserViewHolder>() {

    private var userList: MutableList<UserView> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_content, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(userList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(userList[position])
        }
    }

    override fun getItemCount() = userList.size

    fun addItems(users: List<UserView>) {
        userList = users.toMutableList()
        notifyDataSetChanged()
    }
}

interface OnItemClickListener {
    fun onItemClick(user: UserView)
}