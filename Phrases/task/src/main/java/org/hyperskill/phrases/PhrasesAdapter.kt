package org.hyperskill.phrases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class PhrasesAdapter(private val onDeleteAction: (phrase: Phrase) -> Unit) : RecyclerView.Adapter<PhrasesAdapter.PhraseHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Phrase>(){
        override fun areItemsTheSame(oldItem: Phrase, newItem: Phrase): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Phrase, newItem: Phrase): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun setPhrases (phrases: List<Phrase>){
        asyncListDiffer.submitList(phrases)
    }

    fun getPhrases() : List<Phrase> {
        return asyncListDiffer.currentList
    }

    class PhraseHolder(view: View) : RecyclerView.ViewHolder(view) {
        val phraseTextView: TextView = view.findViewById(R.id.phraseTextView)
        val deleteTextView: TextView = view.findViewById(R.id.deleteTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhraseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phrase_item, parent, false)
        return PhraseHolder(view)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: PhraseHolder, position: Int) {
        val phrase: Phrase = asyncListDiffer.currentList[position]
        holder.phraseTextView.text = phrase.phrase

        holder.deleteTextView.setOnClickListener {
            onDeleteAction(phrase)
        }
    }

}