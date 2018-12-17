package tabledemo.ctl.com.from.listener;


import tabledemo.ctl.com.from.data.column.Column;

/**
 * Created by huang on 2017/11/4.
 */

public interface OnColumnItemClickListener<T> {

    void onClick(Column<T> column, String value, T t, int position);
}
