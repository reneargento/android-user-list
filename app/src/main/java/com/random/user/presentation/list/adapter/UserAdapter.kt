package com.random.user.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.random.user.R
import com.random.user.presentation.list.model.UserView

class UserAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onUserDeletedListener: OnUserDeletedListener
) : ListAdapter<UserView, UserViewHolder>(UserCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_content, parent, false)
        return UserViewHolder(view, onItemClickListener, onUserDeletedListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    override fun getItemCount() = currentList.size
}

private class UserCallback : DiffUtil.ItemCallback<UserView>() {
    override fun areItemsTheSame(oldItem: UserView, newItem: UserView): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: UserView, newItem: UserView): Boolean {
        return oldItem == newItem
    }
}

interface OnItemClickListener {
    fun onItemClick(user: UserView, imageView: ImageView)
}

interface OnUserDeletedListener {
    fun onUserDeleted(email: String)
}