package org.bonygod.gymroutine.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class BiggerPasswordVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = AnnotatedString.Builder().apply {
            repeat(text.length) {
                append('\u25CF')
            }
        }.toAnnotatedString()

        return TransformedText(transformedText, OffsetMapping.Identity)
    }
}