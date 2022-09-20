package com.example.timeplateactivity

import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.Menu
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.timeplateactivity.databinding.ActivityMainBinding
import androidx.room.Room
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.databinding.NavHeaderMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries()
            .build()

        val userDao = db.profileDao()
        val profiles: List<Profile> = userDao.getAll()
        if (profiles.size == 0) {
            userDao.insertAll(Profile(0, "mma", 5000, 2000, 3, true))
            userDao.insertAll(Profile(0, "box", 4000, 2000, 3, true))
            userDao.insertAll(Profile(0, "interval", 3000, 2000, 3, true))

        } else {

        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)




        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_settings, R.id.nav_calc
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val closeImg = binding.drawerLayout.findViewById<ImageButton>(R.id.btnCloseHeader)
//        closeImg.setOnClickListener{
//            Toast.makeText(this,"hello", Toast.LENGTH_LONG).show()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
