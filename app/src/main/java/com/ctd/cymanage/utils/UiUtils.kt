package com.ctd.cymanage.utils


import android.view.Gravity
import android.widget.Toast
import com.ctd.cymanage.App


class UiUtils {


    companion object {

        private var mToast:Toast = Toast.makeText(App.instanceApp(),"",Toast.LENGTH_SHORT)

        fun showToast(str: String,isShort:Boolean = true ){


            var short=if (isShort)Toast.LENGTH_SHORT else Toast.LENGTH_LONG

            mToast.setText(str)

            mToast.duration = short

            mToast.setGravity(Gravity.CENTER,0,0)

            mToast.show()

        }


    }

}