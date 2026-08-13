package com.example.sweepapp.ui.screens

import com.example.sweepapp.R

sealed interface AboutSweepPage

data class SingleTextPage(
    val backgroundRes: Int,
    val textRes: Int
) : AboutSweepPage

data class CenteredTextPage(
    val textRes: Int
) : AboutSweepPage

data class WordCascadePage(
    val words: List<CascadeWord>
) : AboutSweepPage

data class CascadeWord(
    val textRes: Int,
    val align: WordAlign
)

enum class WordAlign { Start, End }

val aboutSweepPages: List<AboutSweepPage> = listOf(
    SingleTextPage(R.drawable.about1, R.string.about_sweep_text1),
    SingleTextPage(R.drawable.about2, R.string.about_sweep_text2),
    SingleTextPage(R.drawable.about3, R.string.about_sweep_text3),
    SingleTextPage(R.drawable.about4, R.string.about_sweep_text4),
    SingleTextPage(R.drawable.about5, R.string.about_sweep_text5),
    SingleTextPage(R.drawable.about6, R.string.about_sweep_text6),
    SingleTextPage(R.drawable.about7, R.string.about_sweep_text7),
    SingleTextPage(R.drawable.about8, R.string.about_sweep_text8),
    SingleTextPage(R.drawable.about_blank, R.string.about_sweep_text8),
    CenteredTextPage(R.string.about_sweep_text9),
    CenteredTextPage(R.string.about_sweep_text10),
    WordCascadePage(
        words = listOf(
            CascadeWord(R.string.about_sweep_cascade1, WordAlign.Start),
            CascadeWord(R.string.about_sweep_cascade2, WordAlign.End),
            CascadeWord(R.string.about_sweep_cascade3, WordAlign.Start),
            CascadeWord(R.string.about_sweep_cascade4, WordAlign.End),
            CascadeWord(R.string.about_sweep_cascade5, WordAlign.Start),
            CascadeWord(R.string.about_sweep_cascade6, WordAlign.End),
            CascadeWord(R.string.about_sweep_cascade7, WordAlign.Start)
        )
    ),
    CenteredTextPage(R.string.about_sweep_text11),
    CenteredTextPage(R.string.about_sweep_text12)
)