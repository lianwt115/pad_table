package com.ctd.cymanage.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import com.ctd.cymanage.R
import java.util.*




/**
 * Created by Administrator on 2018\1\19 0019.
 */
class PickDateDialog: Dialog {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}


    interface PickDateDialogListen {

        fun dataCheck(pickDateDialog:PickDateDialog?,content:String)

    }

      class Builder(private val mContext: Context,listener: PickDateDialogListen?){


        private var mPickDateDialogListen: PickDateDialogListen?  =  listener


        fun  create( year:Int, month:Int, dayOfMonth:Int) :PickDateDialog {
            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // instantiate the dialog with the custom Theme
             var dialog =  PickDateDialog(mContext, R.style.MyDialog)
            dialog.setCanceledOnTouchOutside(true)

            var layout = inflater.inflate(R.layout.dialog_pickdata, null)

            var datePicker =  layout!!.findViewById(R.id.date_picker) as DatePicker

            datePicker.maxDate = Date().time
            datePicker.minDate = minData()

            if (year != -1
                    && month != -1
                    && dayOfMonth != -1) {
                datePicker.updateDate(year, month - 1, dayOfMonth)
            }



            dialog.addContentView(layout, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))


            if (mPickDateDialogListen != null){

                (layout!!.findViewById(R.id.positiveButton) as TextView)
                    .setOnClickListener {

                        mPickDateDialogListen!!.dataCheck(
                            dialog,
                                String.format(Locale.US, "%02d/%02d/%02d",
                                    datePicker.year,
                                    datePicker.month + 1,
                                    datePicker.dayOfMonth
                                )

                            )
                        dialog.dismiss()
                    }


            }


            (layout!!.findViewById(R.id.negativeButton) as TextView)
                        .setOnClickListener {

                            dialog.dismiss()
                        }

            dialog.setContentView(layout)
            return dialog
        }

          fun minData(): Long {
              val c = Calendar.getInstance()
              c.set(2008, 0, 1, 0, 0, 0)
              c.set(Calendar.MILLISECOND, 0)
              return c.time.time
          }

    }


}