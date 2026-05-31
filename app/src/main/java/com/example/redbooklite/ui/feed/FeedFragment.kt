package com.example.redbooklite.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redbooklite.RedBookApp
import com.example.redbooklite.R
import com.example.redbooklite.data.repository.NoteRepository
import com.example.redbooklite.model.Note

class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var tvEmpty: TextView
    private lateinit var rvFeed: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFeed = view.findViewById(R.id.rvFeed)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        adapter = NoteAdapter(object : NoteAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note) {
                // 暂时不处理点击
            }
        })

        rvFeed.layoutManager = LinearLayoutManager(requireContext())
        rvFeed.adapter = adapter

        // 获取 App 和 Repository
        val app = requireActivity().application as RedBookApp
        val repository: NoteRepository = app.repository

        viewModel = ViewModelProvider(
            this,
            FeedViewModelFactory(repository)
        )[FeedViewModel::class.java]

        // 观察 LiveData
        viewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
            adapter.submitList(notes)
            tvEmpty.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        })
    }
}