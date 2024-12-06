package com.mobdeve.group3.mco.comment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mobdeve.group3.mco.R
import com.mobdeve.group3.mco.db.CommentsAPI
import com.mobdeve.group3.mco.sighting.Sighting
import java.util.Date

class CommentsDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var edtCommentInput: EditText
    private lateinit var btnSubmitComment: Button
    private lateinit var sighting: Sighting

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments_dialog, container, false)

        recyclerView = view.findViewById(R.id.rcvComments)
        edtCommentInput = view.findViewById(R.id.edtCommentInput)
        btnSubmitComment = view.findViewById(R.id.btnSubmitComment)

        // Pass the onDeleteClick lambda to the adapter
        commentAdapter = CommentAdapter(mutableListOf()) { comment ->
            showDeleteConfirmationDialog(comment)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = commentAdapter

        loadComments()

        btnSubmitComment.setOnClickListener {
            val newCommentContent = edtCommentInput.text.toString()
            if (newCommentContent.isNotBlank()) {
                // Call the CommentsAPI to add the comment
                CommentsAPI.getInstance().addComment(sighting.id, newCommentContent) { commentId ->
                    Log.d("Comments", "Updated comment with ID: $commentId")

                    // Get the current user's details (user ID and profile image)
                    val currentUser = Firebase.auth.currentUser
                    val userHandler = currentUser?.uid ?: "Unknown"
                    val userIcon = R.drawable.profpic

                    val newComment = Comment(
                        userHandler = userHandler,
                        userIcon = userIcon,
                        content = newCommentContent,
                        commentTime = Date().toString(),
                        postId = sighting.id
                    )

                    sighting.addComment(newComment)

                    val updatedCommentsList = sighting.comments
                    commentAdapter.updateComments(updatedCommentsList)
                    commentAdapter.notifyItemInserted(updatedCommentsList.size - 1)
                    edtCommentInput.text.clear()
                    recyclerView.scrollToPosition(updatedCommentsList.size - 1)
                }
            }
        }

        return view
    }

    private fun loadComments() {
        CommentsAPI.getInstance().getComments(sighting.id) { comments ->
            Log.d("Comments", "Fetched comments: $comments") // Debug log

            val commentList = comments.map { commentData ->
                Comment(
                    userHandler = commentData["userId"] as? String ?: "Unknown",
                    userIcon = R.drawable.profpic,
                    content = commentData["content"] as? String ?: "No Content",
                    commentTime = commentData["commentTime"] as? String ?: "Unknown time",
                    postId = sighting.id
                )
            }

            commentAdapter.updateComments(commentList)
            commentAdapter.notifyDataSetChanged()
        }
    }

    private fun showDeleteConfirmationDialog(comment: Comment) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Comment")
            .setMessage("Are you sure you want to delete this comment?")
            .setPositiveButton("Yes") { _, _ ->
                deleteComment(comment)
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }

    fun deleteComment(comment: Comment) {
        CommentsAPI.getInstance().deleteComment(comment.id, comment.postId) { success ->
            if (success) {
                Toast.makeText(context, "Comment deleted", Toast.LENGTH_SHORT).show()
                updateCommentListAfterDeletion(comment)
            } else {
                Toast.makeText(context, "Failed to delete comment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateCommentListAfterDeletion(comment: Comment) {
        val position = commentAdapter.comments.indexOf(comment)
        if (position != -1) {
            commentAdapter.comments.removeAt(position)
            commentAdapter.notifyItemRemoved(position)
        }
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
            Log.d("Comments", "Sighting ID passed: ${sighting.id}")  // Debug log
            return fragment
        }
    }
}


