package com.example.mvvmauth.ui.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmauth.databinding.DialogAddItemBinding
import com.example.mvvmauth.databinding.FragmentListBinding
import com.example.mvvmauth.ui.adapter.ItemAdapter
import com.example.mvvmauth.viewmodel.ItemListViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemListViewModel by viewModels()

    private lateinit var itemAdapter: ItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        setupSwipeRefresh()

        setupFabButton()

        observeViewModel()

        viewModel.loadItems()
    }


    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter()

        binding.recyclerView.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }


    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadItems()
        }
    }


    private fun setupFabButton() {
        binding.fabAddItem.setOnClickListener {
            showAddItemDialog()
        }
    }


    private fun showAddItemDialog() {
        val dialogBinding = DialogAddItemBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.addButton.setOnClickListener {
            val title = dialogBinding.titleEditText.text.toString()
            val description = dialogBinding.descriptionEditText.text.toString()

            dialogBinding.addButton.isEnabled = false
            dialogBinding.cancelButton.isEnabled = false

            viewModel.addItem(title, description) { success, error ->
                if (success) {
                    Toast.makeText(
                        requireContext(),
                        "Item added successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        error ?: "Failed to add item",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    dialogBinding.addButton.isEnabled = true
                    dialogBinding.cancelButton.isEnabled = true
                }
            }
        }

        dialog.show()
    }


    private fun observeViewModel() {
        // Observe items list
        viewModel.items.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                itemAdapter.updateItems(items)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.GONE
            // Stop SwipeRefresh animation if it's running
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            // Stop SwipeRefresh animation
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun showEmptyState() {
        binding.apply {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }


    private fun hideEmptyState() {
        binding.apply {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

