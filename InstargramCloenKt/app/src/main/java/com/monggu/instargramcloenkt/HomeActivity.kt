package com.monggu.instargramcloenkt

import android.Manifest.*
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.monggu.instargramcloenkt.databinding.ActivityHomeBinding

import navigation.*

import java.util.jar.Manifest

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var binding = ActivityHomeBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bottomNavMain.setOnNavigationItemSelectedListener(this)
        ActivityCompat.requestPermissions(this, arrayOf(permission.READ_EXTERNAL_STORAGE),1)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_home ->{
                var detailViewFragment = DetailViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,detailViewFragment).commit()
            }
            R.id.action_search ->{
                var girdFragment = GirdFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,girdFragment).commit()
            }
            R.id.action_photo ->{
                if (ContextCompat.checkSelfPermission(this,permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(Intent(this,AddphotoActivity::class.java))
                }
            }
            R.id.action_favorite ->{
                var alarmFragment = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,alarmFragment).commit()
            }
            R.id.action_account ->{
                var userFragment = UserFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,userFragment).commit()
            }
        }
        return false
    }
}