package com.mobdeve.group3.mco.comment

import java.text.SimpleDateFormat
import java.util.*

class Comment(
    userHandler: String,
    userIcon: Int,
    commentTime: String,
    content: String,
    postId: String
) {
    val id = UUID.randomUUID().toString()

    var userHandler = userHandler
        private set

    var userIcon = userIcon
        private set

    var commentTime: String = formatDate(commentTime)
        private set

    var content = content
        private set

    var postId = postId
        private set

    private fun formatDate(dateString: String): String {
        try {
            // Parse the input date string into a Date object
            val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            val date = inputFormat.parse(dateString)

            // Format the date into the desired format (dd MMM yyyy HH:mm)
            val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return dateString
        }
    }
}
