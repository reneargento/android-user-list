package com.random.user.presentation.userDetails

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.random.user.databinding.UserDetailsFragmentBinding
import com.random.user.util.loadImage

class UserDetailsFragment: Fragment() {

    private var _binding: UserDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private var userName: String? = null
    private var userGender: String? = null
    private var userAddress: String? = null
    private var userRegisteredDate: String? = null
    private var userEmail: String? = null
    private var userPicture: String? = null

    companion object {
        const val NAME_PARAM = "name"
        const val GENDER_PARAM = "gender"
        const val ADDRESS_PARAM = "address"
        const val REGISTERED_DATE_PARAM = "registered_date"
        const val EMAIL_PARAM = "email"
        const val PICTURE_PARAM = "picture"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            userName = arguments?.getString(NAME_PARAM)
            userGender = arguments?.getString(GENDER_PARAM)
            userAddress = arguments?.getString(ADDRESS_PARAM)
            userRegisteredDate = arguments?.getString(REGISTERED_DATE_PARAM)
            userEmail = arguments?.getString(EMAIL_PARAM)
            userPicture = arguments?.getString(PICTURE_PARAM)
        }
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = UserDetailsFragmentBinding.inflate(inflater, container, false)

        with(binding) {
            name.text = userName
            gender.text = userGender
            address.text = userAddress
            registeredDate.text = userRegisteredDate
            email.text = userEmail

            userPicture?.let { image.loadImage(root.context, it) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.image, userEmail)
    }
}