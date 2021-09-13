package com.random.user.presentation.user.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.random.user.R
import com.random.user.databinding.UserContentBinding

class UserViewHolder(
    root: View,
    private val onUserDeletedListener: OnUserDeletedListener
) : RecyclerView.ViewHolder(root) {

    private val vehicleViewBinding = UserContentBinding.bind(root)

    fun bindData(userView: UserView) {
        with(vehicleViewBinding) {
            name.text = userView.fullName
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