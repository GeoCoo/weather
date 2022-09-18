package coo.apps.weather.activities

import android.content.Intent
import android.os.Bundle
import coo.apps.weather.base.BaseActivity
import coo.apps.weather.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Timer().schedule(2000) {
            startMainActivity()
        }


    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}