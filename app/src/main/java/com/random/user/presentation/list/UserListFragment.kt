package com.random.user.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.random.user.R
import com.random.user.databinding.UserListFragmentBinding
import com.random.user.presentation.list.adapter.OnItemClickListener
import com.random.user.presentation.list.adapter.OnUserDeletedListener
import com.random.user.presentation.list.adapter.UserAdapter
import com.random.user.presentation.list.viewModel.UserListAction
import com.random.user.presentation.list.viewModel.UserListViewState
import com.random.user.presentation.list.viewModel.UserListViewModel
import com.random.user.util.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private var _binding: UserListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var linearLayoutManager: LinearLayoutManager
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()
    private lateinit var userAdapter: UserAdapter

    private val viewModel: UserListViewModel by viewModels()

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
        viewModel.userListViewStateLiveData.observe(viewLifecycleOwner) { render(it) }
        viewModel.userListActionLiveData.observe(viewLifecycleOwner) { perform(it) }

        binding.userList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                viewModel.onScroll(binding.search.text, totalItemCount, lastVisibleItemPosition)
            }
        })
        binding.search.afterTextChanged { filter ->
            viewModel.filterUsers(filter)
        }
        viewModel.fetchUsers()
    }

    private fun render(viewState: UserListViewState) {
        return when (viewState) {
            UserListViewState.Loading -> showLoading()
            is UserListViewState.Results -> {
                hideLoading()
                displayUsers(viewState.users)
            }
            is UserListViewState.Error -> {
                showErrorMessage(viewState.errorMessage)
            }
            UserListViewState.Initial -> Unit
        }
    }

    private fun perform(action: UserListAction) {
        return when (action) {
            is UserListAction.DeleteUser -> {
                displayUsers(action.users)
                showUserDeletedMessage()
            }
            is UserListAction.Navigate -> navigate(action.bundle)
        }
    }

    private fun showLoading() {
        binding.spinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.spinner.visibility = View.GONE
    }

    private fun showErrorMessage(errorMessage: String?) {
        errorMessage?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
    }

    private fun displayUsers(userList: List<UserView>) {
        userAdapter.submitList(userList)
    }

    private fun showUserDeletedMessage() {
        Snackbar.make(binding.root, R.string.user_deleted, Snackbar.LENGTH_SHORT).show()
    }

    private fun navigate(bundle: Bundle) {
        findNavController().navigate(R.id.action_UserListFragment_to_UserDetailsFragment, bundle)
    }

    private fun initRecyclerView() {
        userAdapter = UserAdapter(
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(user: UserView) {
                    viewModel.onUserClicked(user)
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