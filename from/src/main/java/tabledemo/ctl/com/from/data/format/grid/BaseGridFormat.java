package tabledemo.ctl.com.from.data.format.grid;


import tabledemo.ctl.com.from.data.CellInfo;

/**
 * Created by huang on 2018/3/9.
 * 通用绘制网格格式化抽象类
 */

public  class BaseGridFormat extends BaseAbstractGridFormat {


    @Override
    protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
        return true;
    }

    @Override
    protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
        return true;
    }
}
