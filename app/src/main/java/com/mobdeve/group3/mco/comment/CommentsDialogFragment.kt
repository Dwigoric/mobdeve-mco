package com.mobdeve.group3.mco.comment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.sighting.Sighting
import java.util.Date

class CommentsDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var edtCommentInput: EditText
    private lateinit var btnSubmitComment: Button
    private lateinit var sighting: Sighting

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.BottomDialogTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments_dialog, container, false)

        recyclerView = view.findViewById(R.id.rcvComments)
        edtCommentInput = view.findViewById(R.id.edtCommentInput)
        btnSubmitComment = view.findViewById(R.id.btnSubmitComment)

        commentAdapter = CommentAdapter(sighting.comments.toMutableList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = commentAdapter

        btnSubmitComment.setOnClickListener {
            val newCommentContent = edtCommentInput.text.toString()
            if (newCommentContent.isNotBlank()) {
                val newComment = Comment(
                    userHandler = "rosmar",
                    userIcon = R.drawable.profpic,
                    content = newCommentContent,
                    commentTime = Date().toString(),
                    postId = sighting.id
                )

                sighting.addComment(newComment)
                // Log the comment list to check if it's updating
                Log.d("Comments", "Updated comments list: ${sighting.comments}")

                // Update the adapter's data
                commentAdapter.updateComments(sighting.comments)  // Update all comments
                commentAdapter.notifyItemInserted(sighting.comments.size - 1)  // Notify about the new item

                edtCommentInput.text.clear()
            }
        }


        return view
    }

    override fun onStart() {
        super.onStart()

        val dialogWindow = dialog?.window
        val params = dialogWindow?.attributes

        // Set dialog width and height
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT

        // Position the dialog at the bottom
        dialogWindow?.setGravity(Gravity.BOTTOM)
        dialogWindow?.attributes = params
    }


    companion object {
        fun newInstance(sighting: Sighting): CommentsDialogFragment {
            val fragment = CommentsDialogFragment()
            fragment.sighting = sighting
            return fragment
        }
    }
}

