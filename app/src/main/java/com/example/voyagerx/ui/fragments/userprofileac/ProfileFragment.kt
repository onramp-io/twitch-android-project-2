package com.example.voyagerx.ui.fragments.userprofileac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.voyagerx.R


class ProfileFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val editProfileBtn : ImageButton = view.findViewById(R.id.editProfileImageBtn)
        editProfileBtn.setOnClickListener {//uncomment when EditProfile() Fragment has been created
            //val transaction = requireFragmentManager().beginTransaction().replace(R.id.frame, EditProfile()).commit()
        }
        return view
    }
}