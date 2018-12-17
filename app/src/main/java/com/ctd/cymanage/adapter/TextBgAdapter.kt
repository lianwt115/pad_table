package com.ctd.cymanage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctd.cymanage.R
import com.ctd.cymanage.bean.SimpleItemList


class TextBgAdapter(context: Context, list: ArrayList<SimpleItemList>, listen:TextClickListen?) : RecyclerView.Adapter<TextBgAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private  var mTotalList:ArrayList<SimpleItemList> = list
    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mTextClickListen: TextClickListen? = listen
    private var index = 0

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList.get(position)

        val textView= holder.tv_text

        textView.text=obj.content

        textView.setTextColor(mContext.resources.getColor(if (obj.status) R.color.main_text_color3 else R.color.main_text_color2))

        holder.line.visibility = if (obj.status) View.VISIBLE else View.GONE

        holder.root_view.setOnClickListener {



            mTotalList[index].status=false

            index=position

            mTotalList[index].status=true

            notifyDataSetChanged()

            if (mTextClickListen !=null)
                mTextClickListen?.textClick(obj,position)

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mLayoutInflater.inflate(R.layout.item_textbg, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var tv_text: TextView = itemView.findViewById(R.id.tv_text) as TextView
        var line: View = itemView.findViewById(R.id.line) as View
        var root_view: RelativeLayout = itemView.findViewById(R.id.root_view) as RelativeLayout

    }

    interface TextClickListen{
        fun textClick(content:SimpleItemList, position: Int)
    }

}