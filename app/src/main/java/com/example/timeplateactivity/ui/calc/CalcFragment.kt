package com.example.timeplateactivity.ui.calc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.FragmentCalcBinding

class CalcFragment : Fragment() {

    private var _binding: FragmentCalcBinding? = null



    private val binding get() = _binding!!

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CalcViewModel::class.java)

        _binding = FragmentCalcBinding.inflate(inflater, container, false)
with (binding){
//    spinner.getBackground().setColorFilter(resources)


}

        //       binding.sexButtonMale.background.setTint(R.color.primary50Transparent)


        /*      val textView: TextView = binding.textSlideshow
              slideshowViewModel.text.observe(viewLifecycleOwner) {
                  textView.text = it
              }
       */       return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}