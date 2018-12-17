package tabledemo.ctl.com.from.data.format.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import tabledemo.ctl.com.from.core.TableConfig;
import tabledemo.ctl.com.from.data.CellInfo;
import tabledemo.ctl.com.from.data.column.Column;
import tabledemo.ctl.com.from.data.format.bg.ICellBackgroundFormat;
import tabledemo.ctl.com.from.utils.DrawUtils;

/**
 * Created by huang on 2017/10/30.
 */

public class TextDrawFormat<T> implements IDrawFormat<T> {


    private Map<String,SoftReference<String[]>> valueMap; //避免产生大量对象

    public TextDrawFormat() {
        valueMap = new HashMap<>();
    }

    @Override
    public int measureWidth(Column<T> column, int position, TableConfig config) {

        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        return DrawUtils.getMultiTextWidth(paint,getSplitString(column.format(position)));
    }


    @Override
    public int measureHeight(Column<T> column,int position, TableConfig config) {
        Paint paint = config.getPaint();
        config.getContentStyle().fillPaint(paint);
        return DrawUtils.getMultiTextHeight(paint,getSplitString(column.format(position)));
    }

    @Override
    public void draw(Canvas c, Rect rect, CellInfo<T> cellInfo, TableConfig config) {
        Paint paint = config.getPaint();
        setTextPaint(config,cellInfo, paint);
        if(cellInfo.column.getTextAlign() !=null) {
            paint.setTextAlign(cellInfo.column.getTextAlign());
        }
        drawText(c, cellInfo.value, rect, paint);
    }

    protected void drawText(Canvas c, String value, Rect rect, Paint paint) {
        DrawUtils.drawMultiText(c,paint,rect,getSplitString(value));
    }



    public void setTextPaint(TableConfig config,CellInfo<T> cellInfo, Paint paint) {
        config.getContentStyle().fillPaint(paint);
        ICellBackgroundFormat<CellInfo> backgroundFormat = config.getContentCellBackgroundFormat();
        if(backgroundFormat!=null && backgroundFormat.getTextColor(cellInfo) != TableConfig.INVALID_COLOR){
            paint.setColor(backgroundFormat.getTextColor(cellInfo));
        }
        paint.setTextSize(paint.getTextSize()*config.getZoom());

    }

    protected String[] getSplitString(String val){
        String[] values = null;
        if(valueMap.get(val)!=null){
            values= valueMap.get(val).get();
        }
        if(values == null){
            values = val.split("\n");

            valueMap.put(val, new SoftReference<>(values));
        }
        return values;
    }
}
