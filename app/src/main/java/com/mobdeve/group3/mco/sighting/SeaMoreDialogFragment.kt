package com.mobdeve.group3.mco.sighting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.mobdeve.group3.mco.R

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
        val sightingDate = arguments?.getString("sightingDate")
        val sightingTime = arguments?.getString("sightingTime")

        view.findViewById<TextView>(R.id.txtSpeciesValue).text = species
        view.findViewById<TextView>(R.id.txtCommonNameValue).text = commonName
        view.findViewById<TextView>(R.id.txtGroupSizeValue).text = groupSize
        view.findViewById<TextView>(R.id.txtLocationValue).text = location
        view.findViewById<TextView>(R.id.txtDistanceValue).text = distance
        view.findViewById<TextView>(R.id.txtObserverTypeValue).text = observerType
        view.findViewById<TextView>(R.id.txtSDateValue).text = sightingDate
        view.findViewById<TextView>(R.id.txtSTimeValue).text = sightingTime

        // Close button functionality
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
}

