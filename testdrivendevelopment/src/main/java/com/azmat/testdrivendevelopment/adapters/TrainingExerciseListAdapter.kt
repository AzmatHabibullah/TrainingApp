package com.azmat.testdrivendevelopment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.db.model.TrainingExercise
import com.azmat.testdrivendevelopment.db.model.TrainingSet

class TrainingExerciseListAdapter(private val context: Context)
    : ListAdapter<TrainingExercise, TrainingExerciseListAdapter.TrainingExerciseViewHolder>(
    TrainingExerciseComparator()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingExerciseViewHolder {
        return TrainingExerciseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TrainingExerciseViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind("UID ${current.exercise_uid} #${current.exercise_number}")
        holder.trainingSetsRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.trainingSetsRecyclerView.setHasFixedSize(true)

        val adapter = TrainingSetListAdapter({}, {})

        holder.trainingSetsRecyclerView.adapter = adapter
    }

    class TrainingExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trainingExerciseItemView: TextView = itemView.findViewById(R.id.exercise_textView_cardItem)
        val trainingSetsRecyclerView: RecyclerView = itemView.findViewById(R.id.set_recyclerView_cardItem)

        fun bind(trainingExerciseName: String?) {
            trainingExerciseItemView.text = trainingExerciseName
        }

        companion object {
            fun create(parent: ViewGroup): TrainingExerciseViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cardlayout_item, parent, false)
                return TrainingExerciseViewHolder(view)
            }
        }
    }

    class TrainingExerciseComparator : DiffUtil.ItemCallback<TrainingExercise>() {
        override fun areItemsTheSame(oldItem: TrainingExercise, newItem: TrainingExercise): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: TrainingExercise, newItem: TrainingExercise): Boolean {
            return oldItem.uid == newItem.uid // todo continue
        }

    }

}