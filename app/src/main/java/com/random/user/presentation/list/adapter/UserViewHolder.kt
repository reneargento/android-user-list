package com.random.user.presentation.list.adapter

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.random.user.databinding.UserContentBinding
import com.random.user.presentation.list.model.UserView
import com.random.user.util.loadImage

class UserViewHolder(
    root: View,
    private val onItemClickListener: OnItemClickListener,
    private val onUserDeletedListener: OnUserDeletedListener
) : RecyclerView.ViewHolder(root) {

    private val vehicleViewBinding = UserContentBinding.bind(root)

    fun bindData(userView: UserView) {
        with(vehicleViewBinding) {
            val emailText = userView.email
            name.text = userView.fullName
            email.text = emailText
            phone.text = userView.phone
            deleteUser.setOnClickListener {
                onUserDeletedListener.onUserDeleted(userView.email)
            }
            root.setOnClickListener{ onItemClickListener.onItemClick(userView, image) }
            image.loadImage(root.context, userView.pictureMedium)
            ViewCompat.setTransitionName(image, emailText)
        }
    }
}