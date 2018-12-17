package tabledemo.ctl.com.from.data.style;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;

import tabledemo.ctl.com.from.utils.DensityUtils;


/**
 * Created by huang on 2017/9/27.
 */

public class LineStyle implements IStyle {

    private float width =-1;
    private static int mLineColor =Color.parseColor("#e6e6e6");
    private static float mLineSize =2f;
    private boolean isFill;
    private PathEffect effect = new PathEffect();

    public LineStyle() {

    }


    public LineStyle(float width, int color) {
        this.width = width;
        this.mLineColor = color;
    }
    public LineStyle(Context context,float dp, int color) {
        this.width = DensityUtils.dp2px(context,dp);
        this.mLineColor = color;
    }
    public  void setDefaultLineSize(float width){
        mLineSize = width;
    }

    public static void setDefaultLineSize(Context context,float dp){
        mLineSize = DensityUtils.dp2px(context,dp);
    }

    public  void setDefaultLineColor(int color){
        mLineColor = color;
    }

    public float getWidth() {
        if(width == -1){
            return mLineSize;
        }
        return width;
    }

    public LineStyle setWidth(float width) {
        this.width = width;
        return this;
    }
    public LineStyle setWidth(Context context,int dp) {
        this.width = DensityUtils.dp2px(context,dp);
        return this;
    }

    public int getColor() {

        return mLineColor;
    }

    public boolean isFill() {
        return isFill;
    }

    public LineStyle setFill(boolean fill) {
        isFill = fill;
        return this;
    }

    public LineStyle setColor(int color) {

        this.mLineColor = color;
        return this;
    }

    public LineStyle setEffect(PathEffect effect) {
        this.effect = effect;
        return this;
    }

    @Override
    public void fillPaint(Paint paint){
        paint.setColor(getColor());
        paint.setStyle(isFill?Paint.Style.FILL:Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth());
        paint.setPathEffect(effect);

    }
}
