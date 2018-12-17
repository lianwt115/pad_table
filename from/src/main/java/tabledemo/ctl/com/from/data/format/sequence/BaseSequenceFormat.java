package tabledemo.ctl.com.from.data.format.sequence;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import tabledemo.ctl.com.from.core.TableConfig;
import tabledemo.ctl.com.from.utils.DrawUtils;


/**
 * Created by huang on 2018/3/21.
 */

public abstract class BaseSequenceFormat implements ISequenceFormat{
    @Override
    public void draw(Canvas canvas, int sequence, Rect rect, TableConfig config) {
        //字体缩放
        Paint paint  = config.getPaint();
        paint.setTextSize(paint.getTextSize()*(config.getZoom()>1?1:config.getZoom()));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(format(sequence+1),rect.centerX(), DrawUtils.getTextCenterY(rect.centerY(),paint) ,paint);
    }
}
