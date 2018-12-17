package com.ctd.cymanage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctd.cymanage.R
import com.ctd.cymanage.bean.ShowData


class ShowTextAdapter(context: Context, list: ArrayList<ShowData>, listen:TextClickListen?) : RecyclerView.Adapter<ShowTextAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private  var mTotalList:ArrayList<ShowData> = list
    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mTextClickListen: TextClickListen? = listen
    private var index = 0

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]


        holder.tv_left.text = obj.order
        holder.tv_center.text = obj.where
        holder.tv_right.text = numberForrmat(obj.cash)


        holder.root_view.setOnClickListener {

            if (mTextClickListen !=null)
                mTextClickListen?.textClick(obj,position)

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mLayoutInflater.inflate(R.layout.item_showtext, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var tv_left: TextView = itemView.findViewById(R.id.tv_left) as TextView
        var tv_center: TextView = itemView.findViewById(R.id.tv_center) as TextView
        var tv_right: TextView = itemView.findViewById(R.id.tv_right) as TextView
        var root_view: RelativeLayout = itemView.findViewById(R.id.root_view) as RelativeLayout

    }

    interface TextClickListen{
        fun textClick(content:ShowData, position: Int)
    }

    private fun  numberForrmat(number:Int):String{

        var returnNum:String = number.toString()

        //大于1000  处理第一部分
        if (number >999){

            var start = returnNum.substring(0,returnNum.length-3)


            var end = returnNum.substring(returnNum.length-3)


            returnNum = "$start,$end"
        }
        //处理第二部分
        if (number >9999999){

            var start = returnNum.substring(0,returnNum.length-7)


            var end = returnNum.substring(returnNum.length-7)


            returnNum = "$start,$end"
        }
        //处理第三部分
        if (number >999999999){

            var start = returnNum.substring(0,returnNum.length-11)


            var end = returnNum.substring(returnNum.length-11)


            returnNum = "$start,$end"
        }

        return  returnNum

    }

}