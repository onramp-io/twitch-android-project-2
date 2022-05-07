package com.example.voyagerx.ui.fragments.userprofileac

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.voyagerx.databinding.FragmentProfile2Binding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile2Fragment : Fragment() {

    lateinit var binding : FragmentProfile2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfile2Binding.inflate(inflater, container, false)

        //val parallaxImage = binding.parallaxImage
        binding.parallaxImage.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val imageHeight = binding.parallaxImage.measuredHeight
        (binding.favoritesLayout.layoutParams as FrameLayout.LayoutParams).apply {
            topMargin = imageHeight
        }


        binding.scrollView.viewTreeObserver
            .addOnScrollChangedListener(OnScrollChangedListener {

                /* calculating the maximum distance we need to scroll for parallax */
                val maxDistance: Int = imageHeight
                /* getting the amount of scroll on Y-axis */
                val movement: Int = binding.scrollView.scrollY
                /* checking if we've reached the top. if yes then stop translation otherwise continue */
                if (movement in 0..imageHeight) {
                    /* moving the parallax image by half the distance of the total scroll */
                    binding.parallaxImage.translationY = (-movement/2).toFloat()
                }
            })

        return binding.root
    }

}