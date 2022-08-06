package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.ActivityItemsBinding
import com.google.android.material.snackbar.Snackbar

class ItemsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityItemsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarItems.toolbar)
        val toolbar = binding.appBarItems.toolbar

        binding.appBarItems.fab.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

//        var drawer_layout = findViewById<>(R.id.drawer_layout)
        val listItems = findViewById<RecyclerView>(R.id.listItems)
        listItems.layoutManager = LinearLayoutManager(this)
        listItems.adapter = NoteRecyclerAdapter(this, DataManager.notes)

        var drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, "Open navigation drawer", "Close navigation drawer")
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
            R.id.nav_notes -> {
                handleSelection("notes")
            }
            R.id.nav_courses -> {
                handleSelection("courses")
            }
        }
        var drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleSelection(message: String) {
        val listItems = findViewById<RecyclerView>(R.id.listItems)
        Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
    }
}