package com.example.testreadmoreproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_two.*

class TwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        val yourEnum = intent.getSerializableExtra("key") as TestingEnum?
        texting.text = yourEnum.toString()
    }
}