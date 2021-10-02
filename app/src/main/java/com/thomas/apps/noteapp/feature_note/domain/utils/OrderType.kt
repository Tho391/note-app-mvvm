package com.thomas.apps.noteapp.feature_note.domain.utils

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
