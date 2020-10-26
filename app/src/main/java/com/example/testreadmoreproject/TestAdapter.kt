package com.example.testreadmoreproject

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class TestAdapter(private val onClickSeeMore: OnClickSeeMore) :
    RecyclerView.Adapter<TestAdapter.TestViewHolder>() {
    private val stringList = ArrayList<TestingModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_four, parent, false)
        return TestViewHolder(inflatedView, onClickSeeMore)
    }

    override fun getItemCount() = stringList.size

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.tvTest.text = "$position ${stringList[position].testString}"
        if (stringList[position].isExpanded) {
            holder.tvTest.expand()
        } else {
            holder.tvTest.maxLines = 3
            holder.tvTest.collapse()
        }


        holder.tvTest.setOnClickListener(View.OnClickListener {
//            if (stringList[position].isExpanded) {
//                holder.tvTest.setExpanded(false)
//                collapseTextView(holder.tvTest, 3)
//            } else {
//                expandTextView(holder.tvTest)
//                holder.tvTest.setExpanded(true)
//            }

            holder.tvTest.toggle();
            if (stringList[position].isExpanded) {
//                holder.tvTest.collapse()
                stringList[position].isExpanded = false
                Toast.makeText(
                    holder.itemView.context,
                    "COLLAPSE",
                    Toast.LENGTH_SHORT
                ).show()
//                buttonToggle.setText(R.string.expand);
            } else {
                stringList[position].isExpanded = true
//                holder.tvTest.expand()
                Toast.makeText(
                    holder.itemView.context,
                    "EXPAND",
                    Toast.LENGTH_SHORT
                ).show()
//                buttonToggle.setText(R.string.collapse);
            }
//            cycleTextViewExpansion(holder.tvTest)

        })
    }

    fun setData(list: ArrayList<TestingModel>) {
        this.stringList.clear()
        this.stringList.addAll(list)
        notifyDataSetChanged()
    }

    class TestViewHolder(itemView: View, private val onClickSeeMore: OnClickSeeMore) :
        ViewHolder(itemView) {
        var tvTest: ExpandableTextView = itemView.findViewById(R.id.tv_test)

        init {
//            tvTest.setShowingLine(3)
        }
    }

//    private fun cycleTextViewExpansion(tv: TextView) {
//        val collapsedMaxLines = 3
//        val animation = ObjectAnimator.ofInt(
//            tv, "maxLines",
//            if (tv.maxLines == collapsedMaxLines) tv.lineCount else collapsedMaxLines
//        )
//        animation.setDuration(200).start()
//    }

    private fun expandTextView(tv: TextView) {
        val animation = ObjectAnimator.ofInt(tv, "maxLines", tv.lineCount)
        animation.setDuration(200).start()
    }

    private fun collapseTextView(tv: TextView, numLines: Int) {
        val animation = ObjectAnimator.ofInt(tv, "maxLines", numLines)
        animation.setDuration(200).start()
    }
}