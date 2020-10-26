package com.example.testreadmoreproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnClickSeeMore {

    lateinit var testAdapter: TestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testAdapter = TestAdapter(this)
        testAdapter.setData(loadData())
        rv_testing.adapter = testAdapter
        rv_testing.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun loadData(): ArrayList<TestingModel> {
        val dataList = ArrayList<TestingModel>()
        for (i in 1..100) {
            dataList.add(TestingModel(i, getString(R.string.sample_text), false))
//            if (i % 2 == 0) {
//                dataList.add(TestingModel(i, getString(R.string.sample_text), false))
//            } else {
//                dataList.add(TestingModel(i, getString(R.string.sample_text), true))
//            }
        }
        return dataList
    }

    override fun clickSeeMore() {
        Toast.makeText(this, "FCUKING SHIT ", Toast.LENGTH_SHORT).show()
    }
}