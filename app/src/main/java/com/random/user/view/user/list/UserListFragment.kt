package com.random.user.view.user.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.random.user.R
import com.random.user.databinding.UserListFragmentBinding
import com.random.user.domain.UserDataStore
import com.random.user.domain.UserRepository
import com.random.user.domain.getDatabase
import com.random.user.domain.getNetworkService
import com.random.user.util.afterTextChanged
import com.random.user.view.user.details.UserDetailsFragment

class UserListFragment : Fragment() {

    private var _binding: UserListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    private val viewModel: UserListViewModel by viewModels {
        val userNetwork = getNetworkService()
        val userDatabase = getDatabase(requireActivity().baseContext)
        val repository = UserRepository(userNetwork, userDatabase.userDao)
        val userDataStore = UserDataStore(requireContext())
        UserListViewModel.FACTORY(repository, userDataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = UserListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        initRecyclerView()
        val mapper = UserDaoToViewMapper()

        viewModel.spinnerLiveData.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
        viewModel.snackBarLiveData.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackBarShown()
            }
        }
        viewModel.userLiveData.observe(viewLifecycleOwner) { userList ->
            userList?.let {
                (binding.userList.adapter as UserAdapter).updateItems(
                    userList.map { mapper.userDaoToView(it) }
                )
            }
        }
        viewModel.filteredUsersLiveDataObservable.observe(viewLifecycleOwner) {  userList ->
            userList?.let {
                (binding.userList.adapter as UserAdapter).updateItems(
                    userList.map { mapper.userDaoToView(it) }
                )
            }
        }

        binding.userList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (!viewModel.isRequestingUsers
                    && binding.search.text.isNullOrEmpty()
                    && totalItemCount == lastVisibleItemPosition + 1) {
                    viewModel.fetchUsers()
                }
            }
        })
        binding.search.afterTextChanged { filter ->
            viewModel.filterUsers(filter)
        }

        viewModel.fetchUsers()
    }

    private fun initRecyclerView() {
        val userAdapter = UserAdapter(
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(user: UserView) {
                    val bundle = bundleOf(
                        UserDetailsFragment.NAME_PARAM to user.fullName,
                        UserDetailsFragment.GENDER_PARAM to user.gender,
                        UserDetailsFragment.ADDRESS_PARAM to user.address,
                        UserDetailsFragment.REGISTERED_DATE_PARAM to user.registered,
                        UserDetailsFragment.EMAIL_PARAM to user.email,
                        UserDetailsFragment.PICTURE_PARAM to user.pictureLarge,
                    )
                    findNavController().navigate(R.id.action_UserListFragment_to_UserDetailsFragment, bundle)
                }
            },
            onUserDeletedListener = object : OnUserDeletedListener {
                override fun onUserDeleted(email: String) {
                    viewModel.deleteUser(email)
                }
            }
        )

        val layoutManager = LinearLayoutManager(binding.root.context)
        linearLayoutManager = LinearLayoutManager(binding.root.context)
        binding.userList.layoutManager = linearLayoutManager
        binding.userList.addItemDecoration(DividerItemDecoration(binding.root.context, layoutManager.orientation))
        binding.userList.adapter = userAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}