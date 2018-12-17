package com.ctd.cymanage.utils


import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import com.ctd.cymanage.R
import com.ctd.cymanage.R.id.select_year_start
import com.ctd.cymanage.widget.ChooseDialog
import com.ctd.cymanage.widget.PickDateDialog
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_paymentinfo.*


/**
 * Created by Administrator on 2018\1\8 0008.
 */
class DialogUtils {


    companion object {

        fun showSelectOneDialog(context: Context, title:String, data1Array: Array<String>, selectContent:String, listen: ChooseDialog.ChooseDialogListen) {


            var datas: HashMap<String, Array<String>> = HashMap()

            var index = data1Array.indexOf(selectContent)

            datas["one"] = data1Array

            var build = ChooseDialog.Builder(context)


            var dialog = build.create(
                listen,
                1,
                datas,
                arrayOf(if (index < 0) 0 else index),
                title
            )

            dialog.show()

        }

        fun showPickDateDialogDialog(context: Context,data:String, listen: PickDateDialog.PickDateDialogListen?=null) {

            val dialog: PickDateDialog
            val builder = PickDateDialog.Builder(context,listen)

            var year = -1
            var month = -1
            var dayOfMonth = -1
            //    2018/08/23
            if (!TextUtils.isEmpty(data)) {
                var intData = data.split("/")

                year = intData[0].toInt()
                month = intData[1].toInt()
                dayOfMonth = intData[2].toInt()
            }

            dialog = builder.create(year, month, dayOfMonth)

            dialog.show()

        }

    }

}