package com.example.redbooklite.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.redbooklite.R
import com.example.redbooklite.RedBookApp
import com.example.redbooklite.model.Note
import com.example.redbooklite.ui.author.AuthorProfileActivity
import com.example.redbooklite.ui.feed.NoteAdapter
import com.example.redbooklite.ui.detail.NoteDetailActivity

class ProfileFragment : Fragment(), NoteAdapter.OnNoteClickListener {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var rvProfile: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvProfile = view.findViewById(R.id.rvProfile)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        val app = requireActivity().application as RedBookApp
        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(app.repository)
        )[ProfileViewModel::class.java]

        adapter = NoteAdapter(this)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvProfile.layoutManager = layoutManager
        rvProfile.adapter = adapter
        rvProfile.setHasFixedSize(false)

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes)
            tvEmpty.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
            rvProfile.visibility = if (notes.isEmpty()) View.GONE else View.VISIBLE
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
}
