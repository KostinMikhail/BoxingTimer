package com.kostlin.timeplateactivity.data

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.kostlin.timeplateactivity.R
import org.w3c.dom.Text

class BottomSheetAdapter(var context: Context, val profileTestList: ArrayList<TestList>) :
    BaseAdapter() {

    private class ViewHolder(row: View?) {
        var txtName: TextView

        init {
            this.txtName = row?.findViewById(R.id.txtName) as TextView
        }
    }

    override fun getCount(): Int {
        return profileTestList.size
    }

    override fun getItem(position: Int): Any {
        return profileTestList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View?
        var viewHolder: ViewHolder
        if (convertView == null) {
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.bottomsheet_cell, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var profileName: TestList = getItem(position) as TestList

        viewHolder.txtName.text = profileName.name

        return view as View
    }


}