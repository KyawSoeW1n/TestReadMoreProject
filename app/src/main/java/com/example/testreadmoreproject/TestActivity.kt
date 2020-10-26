package com.example.testreadmoreproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        btn_click.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("key", TestingEnum.TEXT)

            startActivity(Intent(this@TestActivity, TwoActivity::class.java).putExtras(bundle))
        })
    }
}