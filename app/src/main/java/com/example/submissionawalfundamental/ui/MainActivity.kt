package com.example.submissionawalfundamental.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalfundamental.adapter.SearchAdapter
import com.example.submissionawalfundamental.databinding.ActivityMainBinding
import com.example.submissionawalfundamental.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: SearchAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    binding.searchBar.setText(binding.searchView.text)
                    searchView.hide()
                    val query = searchView.text.toString()
                    mainViewModel.searchUsers(query)
                    false
                }
        }

        showRecycleView()

        mainViewModel.userList.observe(this) {user ->
            userAdapter.submitList(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.errorMessage.observe(this ){ message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecycleView() {
        userAdapter = SearchAdapter()
        binding.rvUsers.adapter = userAdapter
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}