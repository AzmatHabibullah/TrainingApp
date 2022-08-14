package com.azmat.testdrivendevelopment.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.azmat.testdrivendevelopment.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarVCalendarF.setOnDateChangeListener { calendarView, year, month, date ->
            val selectedCal = Calendar.getInstance()
            selectedCal.set(year, month, date, 0, 0, 0)
            selectedCal.set(Calendar.MILLISECOND, 0)
            Log.d(tag, "Changing date to $date-$month-$year with id ${selectedCal.timeInMillis}")
            val action = CalendarFragmentDirections.actionCalendarFragmentToDayFragment(selectedCal.timeInMillis)
            calendarView.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}