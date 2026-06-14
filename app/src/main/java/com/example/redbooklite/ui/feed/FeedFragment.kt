package com.example.redbooklite.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.redbooklite.R
import com.example.redbooklite.RedBookApp
import com.example.redbooklite.model.Note
import com.example.redbooklite.ui.author.AuthorProfileActivity
import com.example.redbooklite.ui.detail.NoteDetailActivity

class FeedFragment : Fragment(), NoteAdapter.OnNoteClickListener {

    private lateinit var viewModel: FeedViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvFeed: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var tvLoading: TextView
    private lateinit var tvError: TextView
    private lateinit var btnRetry: Button
    private lateinit var etSearch: EditText
    private lateinit var btnCategoryAll: Button
    private lateinit var btnCategoryTravel: Button
    private lateinit var btnCategoryFood: Button
    private lateinit var btnCategoryFashion: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        rvFeed = view.findViewById(R.id.rvFeed)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        tvLoading = view.findViewById(R.id.tvLoading)
        tvError = view.findViewById(R.id.tvError)
        btnRetry = view.findViewById(R.id.btnRetry)
        etSearch = view.findViewById(R.id.etSearch)
        btnCategoryAll = view.findViewById(R.id.btnCategoryAll)
        btnCategoryTravel = view.findViewById(R.id.btnCategoryTravel)
        btnCategoryFood = view.findViewById(R.id.btnCategoryFood)
        btnCategoryFashion = view.findViewById(R.id.btnCategoryFashion)

        val app = requireActivity().application as RedBookApp
        viewModel = ViewModelProvider(
            this,
            FeedViewModelFactory(app.repository)
        )[FeedViewModel::class.java]

        adapter = NoteAdapter(this)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvFeed.layoutManager = layoutManager
        rvFeed.adapter = adapter
        rvFeed.setHasFixedSize(false)

        swipeRefresh.setOnRefreshListener {
            viewModel.refreshFeed()
        }
        btnRetry.setOnClickListener {
            viewModel.refreshFeed()
        }
        btnCategoryAll.setOnClickListener {
            viewModel.selectCategory(FeedViewModel.CATEGORY_ALL)
        }
        btnCategoryTravel.setOnClickListener {
            viewModel.selectCategory(FeedViewModel.CATEGORY_TRAVEL)
        }
        btnCategoryFood.setOnClickListener {
            viewModel.selectCategory(FeedViewModel.CATEGORY_FOOD)
        }
        btnCategoryFashion.setOnClickListener {
            viewModel.selectCategory(FeedViewModel.CATEGORY_FASHION)
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateSearchKeyword(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.notes)
            updateCategoryButtons(state.selectedCategory)
            swipeRefresh.isRefreshing = state.isRefreshing && !state.showLoading
            rvFeed.visibility = if (state.notes.isEmpty()) View.GONE else View.VISIBLE
            tvLoading.visibility = if (state.showLoading) View.VISIBLE else View.GONE
            tvEmpty.visibility = if (state.showEmpty) View.VISIBLE else View.GONE

            val showError = state.errorMessage != null
            tvError.visibility = if (showError) View.VISIBLE else View.GONE
            btnRetry.visibility = if (showError) View.VISIBLE else View.GONE
            tvError.text = state.errorMessage.orEmpty()
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(requireContext(), NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
        startActivity(intent)
    }

    override fun onAuthorClick(note: Note) {
        val intent = Intent(requireContext(), AuthorProfileActivity::class.java)
        intent.putExtra(AuthorProfileActivity.EXTRA_AUTHOR_ID, note.authorId)
        intent.putExtra(AuthorProfileActivity.EXTRA_AUTHOR_NAME, note.authorName)
        intent.putExtra(AuthorProfileActivity.EXTRA_AUTHOR_AVATAR_URL, note.authorAvatarUrl)
        startActivity(intent)
    }

    private fun updateCategoryButtons(selectedCategory: String) {
        setCategoryButtonSelected(btnCategoryAll, selectedCategory == FeedViewModel.CATEGORY_ALL)
        setCategoryButtonSelected(btnCategoryTravel, selectedCategory == FeedViewModel.CATEGORY_TRAVEL)
        setCategoryButtonSelected(btnCategoryFood, selectedCategory == FeedViewModel.CATEGORY_FOOD)
        setCategoryButtonSelected(btnCategoryFashion, selectedCategory == FeedViewModel.CATEGORY_FASHION)
    }

    private fun setCategoryButtonSelected(button: Button, selected: Boolean) {
        button.isSelected = selected
        button.alpha = if (selected) 1f else 0.55f
    }
}
