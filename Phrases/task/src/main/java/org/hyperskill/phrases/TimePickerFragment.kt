package org.hyperskill.phrases

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener{

    interface OnTimeSetListener {
        fun sendTime(hour: Int, minute: Int)
    }

    private lateinit var onTimeSetListener: OnTimeSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        onTimeSetListener.sendTime(hourOfDay, minute)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onTimeSetListener = activity as OnTimeSetListener
        } catch (ex: ClassCastException){
            Log.e(TAG, "onAttach: ClassCastException: " + ex.message);
        }
    }

    companion object {
        const val TAG = "TimePickerDialog"
    }

}