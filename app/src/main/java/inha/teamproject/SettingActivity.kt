package inha.teamproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = "설정"
    }
}
