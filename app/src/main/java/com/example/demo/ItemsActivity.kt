package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.ActivityItemsBinding
import com.google.android.material.snackbar.Snackbar
import com.jwhh.notekeeper.CourseRecyclerAdapter

class ItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityItemsBinding


    private val noteLayoutManager by lazy {
        LinearLayoutManager(this)
     }
    private val noteRecyclerAdapter by lazy{
        NoteRecyclerAdapter(this, DataManager.notes)
    }

    private val courseLayoutManager by lazy{ GridLayoutManager(this, 2)}
    private val courseRecyclerAdapter by lazy{
        CourseRecyclerAdapter(this, DataManager.courses.values.toList())
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this)[ItemsActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarItems.toolbar)
        val toolbar = binding.appBarItems.toolbar

        binding.appBarItems.fab.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        var drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_items)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_notes, R.id.nav_courses, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(this)
    }

    private fun displayNotes() {
        val listItems = findViewById<RecyclerView>(R.id.listItems)
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = noteRecyclerAdapter
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        val listItems = findViewById<RecyclerView>(R.id.listItems)
        listItems.layoutManager = courseLayoutManager
        listItems.adapter = courseRecyclerAdapter
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.menu.findItem(R.id.nav_courses).isChecked = true
    }

//    private fun ActionBarDrawerToggle(itemsActivity: ItemsActivity, drawerLayout: DrawerLayout, toolbar: Toolbar, s: String, s1: String): ActionBarDrawerToggle {
//        //Nothing
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        val listItems = findViewById<RecyclerView>(R.id.listItems)
        listItems.adapter?.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_items)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here
        when (item.itemId){
            R.id.nav_notes,
            R.id.nav_courses -> {
                handleDisplaySelection(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
            }
        }

        var drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun handleDisplaySelection(itemId: Int){
        when(itemId){
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
        }
    }

    private fun handleSelection(message: String) {
        val listItems = findViewById<RecyclerView>(R.id.listItems)
        Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
    }
}
