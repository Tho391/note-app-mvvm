package com.thomas.apps.noteapp.utils.collection

import java.util.NoSuchElementException

object CollectionUtils {

    inline fun <reified T> List<*>.asListOfType(): List<T>? =
        if (all { it is T })
            @Suppress("UNCHECKED_CAST")
            this as List<T> else
            null

    /**
     * Returns the second element, or `null` if the list size < 2.
     */
    fun <T> List<T>.secondOrNull(): T? {
        return if (isEmpty() || size < 2) null else this[1]
    }

    /**
     * Returns second element.
     * @throws [NoSuchElementException] if the list size < 2.
     */
    fun <T> List<T>.second(): T {
        if (size < 2)
            throw NoSuchElementException("List size is < 2.")
        return this[1]
    }

    /**
     * Returns the third element, or `null` if the list size < 3.
     */
    fun <T> List<T>.thirdOrNull(): T? {
        return if (size < 3) null else this[2]
    }

    /**
     * Returns third element.
     * @throws [NoSuchElementException] if the list size < 3.
     */
    fun <T> List<T>.third(): T {
        if (size < 3)
            throw NoSuchElementException("List size is < 3.")
        return this[2]
    }
}