package com.example.demo

import org.junit.Assert.*

import org.junit.Test

class DataManagerTest {

    @Test
    fun addNote() {

        val course = DataManager.courses.get("android_async")!!
        val noteTitle = "This is a test note"
        val noteText = "This is the body of my test note"

        val index = DataManager.addNote(course, noteTitle, noteText)
        val note = DataManager.notes[index]

    }
}