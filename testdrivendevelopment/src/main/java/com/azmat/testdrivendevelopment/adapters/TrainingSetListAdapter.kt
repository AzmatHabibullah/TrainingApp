package com.azmat.testdrivendevelopment.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.db.model.TrainingSet

class TrainingSetListAdapter(private val onCommentClick: (trainingSetUid: Long) -> Unit,
                             private val onRecordClick: (trainingSetUid: Long) -> Unit) :
    ListAdapter<TrainingSet, TrainingSetListAdapter.TrainingSetViewHolder>(
        TrainingSetsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingSetViewHolder {
        return TrainingSetViewHolder.create(parent, onCommentClick, onRecordClick)

    }

    override fun onBindViewHolder(holder: TrainingSetViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.weight, current.reps, current.set_number, current.unit, current.uid, current.video_path, current.comment)
    }

    class TrainingSetViewHolder(itemView: View,
                                private val onCommentClick: (trainingSetUid: Long) -> Unit,
                                private val onRecordClick: (trainingSetUid: Long) -> Unit):
        RecyclerView.ViewHolder(itemView) {

        private val weightTextView: TextView = itemView.findViewById(R.id.weight_textView_recyclerItem)
        private val repsTextView: TextView = itemView.findViewById(R.id.reps_textView_recyclerItem)
        private val unitTextView: TextView = itemView.findViewById(R.id.unit_textView_recyclerItem)
        private val commentButton: ImageButton = itemView.findViewById(R.id.comment_button_recyclerItem)
        private val recordButton: ImageButton = itemView.findViewById(R.id.record_button_recyclerItem)
        private val setNumberView: TextView = itemView.findViewById(R.id.setNumber_textView_recyclerItem)

        fun bind(weight: Double, reps: Int, setNumber: Int, unit: String, uid: Long, video_path: String, comment: String) {
            weightTextView.text = "$weight"
            repsTextView.text = "$reps"
            unitTextView.text = unit
            setNumberView.text = setNumber.toString()
            commentButton.setOnClickListener { _ ->
                {}
            } // onCommentClick(uid) }
            recordButton.setOnClickListener { _ ->
                {}
            } // onRecordClick(uid) }
            if (video_path != "") {
//                recordButton.setImageBitmap()
                Log.d("TrainingSetListAdapter", "We need to update bitmap")
                recordButton.setColorFilter(androidx.appcompat.R.attr.colorControlNormal)
            }
            if (comment != "") {
                commentButton.setColorFilter(androidx.appcompat.R.attr.colorControlNormal)
            }
        }

        companion object {
            fun create(parent: ViewGroup,
                       onItemClick: (trainingSetUid: Long) -> Unit,
                       onRecordClick: (trainingSetUid: Long) -> Unit): TrainingSetViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_set, parent, false)

                return TrainingSetViewHolder(view, onItemClick, onRecordClick)
            }
        }

    }

    class TrainingSetsComparator : DiffUtil.ItemCallback<TrainingSet>() {
        override fun areItemsTheSame(oldItem: TrainingSet, newItem: TrainingSet): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TrainingSet, newItem: TrainingSet): Boolean {
            return oldItem.uid == newItem.uid // todo continue
        }

    }
}


// todo check this is correct way to adapt