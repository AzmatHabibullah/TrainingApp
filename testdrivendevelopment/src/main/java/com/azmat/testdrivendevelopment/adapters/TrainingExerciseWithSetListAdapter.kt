package com.azmat.testdrivendevelopment.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.db.model.TrainingExerciseWithSet
import com.azmat.testdrivendevelopment.db.model.TrainingSet

class TrainingExerciseWithSetListAdapter(private val onCardClick: (trainingExerciseUid: Long, exerciseUid: Long, exerciseName: String, workoutUid: Long) -> Unit) :
    ListAdapter<TrainingExerciseWithSet, TrainingExerciseWithSetListAdapter.TrainingExerciseWithSetListViewHolder>(
        TrainingExerciseWithSetListComparator()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TrainingExerciseWithSetListViewHolder {
        return TrainingExerciseWithSetListViewHolder.create(parent, onCardClick)
    }

    override fun onBindViewHolder(holder: TrainingExerciseWithSetListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.exercise, current.trainingSets)
    }

    class TrainingExerciseWithSetListViewHolder(
        itemView: View,
        private val onCardClick: (trainingExerciseUid: Long, exerciseUid: Long, exerciseName: String, workoutUid: Long) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val exerciseItemView: TextView =
            itemView.findViewById(R.id.exercise_textView_cardItem)
        val trainingSetsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.set_recyclerView_cardItem) // todo add separate recycler here?
        val cardview = itemView.findViewById<CardView>(R.id.exerciseSetList_card)
        val adapter = TrainingSetListAdapter({}, {})

        init {
            trainingSetsRecyclerView.adapter = adapter
            trainingSetsRecyclerView.setOnClickListener {
                Log.d("RecyclerView", "clicked recycler")
            }
        }


        fun bind(exercise: Exercise?, trainingSets: List<TrainingSet>) {
            val exerciseName = exercise?.exercise_name
            val exerciseUid = exercise?.uid
            exerciseItemView.text = exerciseName
            cardview.setOnClickListener { _ -> onCardClick(trainingSets[0].training_exercise_uid,
                exerciseUid!!, exerciseName!!, trainingSets[0].workout_uid) }
            adapter.submitList(trainingSets)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onCardClick: (trainingExerciseUid: Long, exerciseUid: Long, exerciseName: String, workoutUid: Long) -> Unit,
            ): TrainingExerciseWithSetListViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cardlayout_item, parent, false)
                return TrainingExerciseWithSetListViewHolder(view, onCardClick)
            }
        }
    }

    class TrainingExerciseWithSetListComparator : DiffUtil.ItemCallback<TrainingExerciseWithSet>() {
        override fun areItemsTheSame(
            oldItem: TrainingExerciseWithSet,
            newItem: TrainingExerciseWithSet,
        ): Boolean {
            return oldItem.exercise == newItem.exercise // todo
        }

        override fun areContentsTheSame(
            oldItem: TrainingExerciseWithSet,
            newItem: TrainingExerciseWithSet,
        ): Boolean {
            return oldItem.trainingSets == newItem.trainingSets // todo
        }

    }

}
