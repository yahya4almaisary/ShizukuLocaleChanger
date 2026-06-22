package com.example.shizukulocalechanger

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

/**
 * محول لعرض قائمة اللغات
 */
class LanguageAdapter(
    private val context: Context,
    private val locales: List<Pair<String, String>>,
    private val currentLocale: String,
    private val onLocaleSelected: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private var selectedLocale = currentLocale

    inner class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val card: MaterialCardView = itemView as MaterialCardView
        private val radioButton: RadioButton
        private val languageText: TextView

        init {
            radioButton = card.findViewById(R.id.languageRadio)
            languageText = card.findViewById(R.id.languageText)

            card.setOnClickListener {
                selectedLocale = locales[adapterPosition].first
                notifyDataSetChanged()
                onLocaleSelected(selectedLocale)
            }

            radioButton.setOnClickListener {
                selectedLocale = locales[adapterPosition].first
                notifyDataSetChanged()
                onLocaleSelected(selectedLocale)
            }
        }

        fun bind(locale: Pair<String, String>) {
            languageText.text = locale.second
            radioButton.isChecked = locale.first == selectedLocale
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.language_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locales[position])
    }

    override fun getItemCount(): Int = locales.size

    fun getSelectedLocale(): String = selectedLocale
}
