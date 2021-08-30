package com.random.user.view.user.list

import android.content.Context
import com.random.user.R
import com.random.user.domain.User
import java.util.*

class UserDaoToViewMapper {

    fun userDaoToView(user: User, context: Context) = UserView(
        user.email,
        user.gender.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        context.getString(R.string.full_name, user.name, user.surname),
        context.getString(R.string.address, user.street, user.city, user.state),
        user.registered.substring(0, user.registered.indexOf('T')),
        user.phone,
        user.pictureLarge,
        user.pictureMedium
    )
}