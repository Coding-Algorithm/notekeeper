package com.example.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class NoteActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapterCourses = ArrayAdapter<CourseInfo>(this,
            android.R.layout.simple_spinner_item, DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var spinnerCourses = findViewById<Spinner>(R.id.spinnerCourses)
        spinnerCourses.adapter = adapterCourses

        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if(notePosition != POSITION_NOT_SET)
            displayNote()
        else{
            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex
        }

        Log.d(tag, "onCreate")

    }

    override fun onSaveInstanceState(outState: Bundle): Unit {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }



    private fun displayNote() {
        if(notePosition > DataManager.notes.lastIndex){
            showMessage("Note not found")
            Log.e(tag, "Invalid note position $notePosition, max valid position ${DataManager.notes.lastIndex}")
            return
        }

        Log.i(tag, "Displaying note for position $notePosition")
        val note = DataManager.notes[notePosition]
        val textNoteTitle = findViewById<TextView>(R.id.textNoteTitle)
        textNoteTitle.setText(note.title)
        val textNoteText = findViewById<TextView>(R.id.textNoteText)
        textNoteText.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)

        var spinnerCourses = findViewById<Spinner>(R.id.spinnerCourses)
        spinnerCourses.setSelection(coursePosition)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_settings -> true
            R.id.action_next -> {

                if(notePosition < DataManager.notes.lastIndex){
                    moveNext()
                }else{

                    val message = "No more notes"
                    showMessage(message)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(message: String){
        var textNoteTitle = findViewById<TextView>(R.id.textNoteTitle)
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_LONG).show()
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if(notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)

            if(menuItem != null){
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false
            }

        }

        return menu?.let { super.onPrepareOptionsMenu(it) }
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d(tag, "onPause")
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]

        note.title = findViewById<TextView>(R.id.textNoteTitle).text.toString()
        note.text = findViewById<TextView>(R.id.textNoteText).text.toString()

        var spinnerCourses = findViewById<Spinner>(R.id.spinnerCourses)
        note.course = spinnerCourses.selectedItem as CourseInfo

    }




}





