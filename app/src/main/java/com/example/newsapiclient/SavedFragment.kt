package com.example.newsapiclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapiclient.databinding.FragmentSavedBinding
import com.example.newsapiclient.presentation.adapter.NewsAdapter
import com.example.newsapiclient.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class SavedFragment : Fragment() {


    private lateinit var fragmentSavedBinding: FragmentSavedBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSavedBinding = FragmentSavedBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        newsAdapter = (activity as MainActivity).newsAdapter


        newsAdapter.setOnItemClickListener {
            if(it.equals(null)){
                Toast.makeText(requireActivity(), "No article available", Toast.LENGTH_SHORT).show()
            }
            else{
                val bundle = Bundle().apply {
                    putSerializable("selected_article", it)
                }
                // findNavController().navigate(R.id.action_newsFragment_to_infoFragment, bundle)

                findNavController().navigate(R.id.action_savedFragment_to_infoFragment, bundle)

            }
        }

        initializeRecyclerView()
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Delete Successfully!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo")
                    {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }


        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(fragmentSavedBinding.savedRecyclerView)
        }



    }


    private fun initializeRecyclerView() {
        // newsAdapter = NewsAdapter()
        fragmentSavedBinding.savedRecyclerView.adapter = newsAdapter
        fragmentSavedBinding.savedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        // fragmentSavedBinding.savedRecyclerView.addOnScrollListener(this@SavedFragment.onScrollListener)
    }


}