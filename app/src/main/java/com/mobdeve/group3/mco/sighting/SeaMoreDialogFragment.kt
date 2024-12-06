package com.mobdeve.group3.mco.sighting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.mobdeve.group3.mco.R
import java.text.SimpleDateFormat
import java.util.Locale

class SeaMoreDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fg_sea_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data passed via arguments
        val species = arguments?.getString("species")
        val commonName = arguments?.getString("commonName")
        val groupSize = arguments?.getString("groupSize")
        val location = arguments?.getString("location")
        val distance = arguments?.getString("distance")
        val observerType = arguments?.getString("observerType")
        val sightingDateRaw  = arguments?.getString("sightingDate")
        val sightingTimeRaw = arguments?.getString("sightingTime")

        val formattedDate = formatDate(sightingDateRaw)
        val formattedTime = formatTime(sightingTimeRaw)

        view.findViewById<TextView>(R.id.txtSpeciesValue).text = species
        view.findViewById<TextView>(R.id.txtCommonNameValue).text = commonName
        view.findViewById<TextView>(R.id.txtGroupSizeValue).text = groupSize
        view.findViewById<TextView>(R.id.txtLocationValue).text = location
        view.findViewById<TextView>(R.id.txtDistanceValue).text = distance
        view.findViewById<TextView>(R.id.txtObserverTypeValue).text = observerType
        view.findViewById<TextView>(R.id.txtSDateValue).text = formattedDate
        view.findViewById<TextView>(R.id.txtSTimeValue).text = formattedTime

        val btnClose: ImageButton = view.findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val dialogWindow = dialog?.window
        val params = dialogWindow?.attributes

        // Set dialog width and height
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialogWindow?.attributes = params
    }

    private fun formatDate(dateString: String?): String {
        return try {
            val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
            val date = originalFormat.parse(dateString ?: "")
            targetFormat.format(date ?: "")
        } catch (e: Exception) {
            dateString ?: ""
        }
    }

    private fun formatTime(dateString: String?): String {
        return try {
            val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val date = originalFormat.parse(dateString ?: "")
            targetFormat.format(date ?: "")
        } catch (e: Exception) {
            dateString ?: ""
        }
    }
}

