package com.azmat.testdrivendevelopment.ui.day

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class NewExerciseDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        // todo with categories and exercises in constructor, build this
    }

    companion object {
        const val TAG = "NewExerciseDialogFragment"
    }

}