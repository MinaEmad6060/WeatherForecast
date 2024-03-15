package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weatherforecast.Alert.View.AlertFragment
import com.example.weatherforecast.Favourite.View.FavouriteFragment
import com.example.weatherforecast.Home.View.HomeFragment
import com.example.weatherforecast.Settings.View.SettingsFragment
import com.example.weatherforecast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState ?: replaceFragment(HomeFragment())
        addNavigationListener()
    }

    private fun addNavigationListener(){
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment->{
                    replaceFragment(HomeFragment())
                    true }
                R.id.favFragment->{
                    replaceFragment(FavouriteFragment())
                    true}
                R.id.alertFragment-> {
                    replaceFragment(AlertFragment())
                    true}
                R.id.settingsFragment->{
                    replaceFragment(SettingsFragment())
                    true}

                else -> {false}
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_container,fragment)
        transaction.commit()
    }
}