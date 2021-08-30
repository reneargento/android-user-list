package com.random.user.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.random.user.R
import com.random.user.databinding.UserContentBinding

class UserViewHolder(
    root: View,
    private val onUserDeletedListener: OnUserDeletedListener) : RecyclerView.ViewHolder(root) {

    private val vehicleViewBinding = UserContentBinding.bind(root)

    fun bindData(userView: UserView) {
        with(vehicleViewBinding) {
            name.text = root.context.getString(R.string.name, userView.name, userView.surname)
            email.text = userView.email
            phone.text = userView.phone
            deleteUser.setOnClickListener {
                onUserDeletedListener.onUserDeleted(userView.email)
            }

            Glide.with(root.context)
                .load(userView.pictureMedium)
                .placeholder(R.mipmap.ic_placeholder)
                .into(vehicleViewBinding.image)
        }
    }
}