package com.example.timeplateactivity.ui.Clock


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.example.timeplateactivity.R


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1 : Fragment() {


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Fragment1", "onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //  var textView: TextView = findViewById(R.id.timer) as TextView


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_clock, container, false)

        Log.d("Fragment1", "onCreateView")

        return rootView
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("Fragment1", "onAttach")
    }
    override fun onStart() {
        super.onStart()

        Log.d("Fragment1", "onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("Fragment1", "onResume")
    }

    override fun onStop() {
        Log.d("Fragment1", "onStop")

        super.onStop()
    }
    override fun onDestroyView() {
        Log.d("Fragment1", "onDestroyView")

        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("Fragment1", "onDestroy")

        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("Fragment1", "onDetach")

        super.onDetach()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment1.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}