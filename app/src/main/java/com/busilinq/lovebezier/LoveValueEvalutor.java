package com.busilinq.lovebezier;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/18
 * author: shiquan.lu
 */

public class LoveValueEvalutor implements TypeEvaluator<PointF> {

    private PointF pointF1;
    private PointF pointF2;

    public LoveValueEvalutor(PointF pointF1, PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }


    @Override
    public PointF evaluate(float t, PointF startValue, PointF endValue) {
        PointF pointF = new PointF();
        pointF.x = startValue.x*(1-t)*(1-t)*(1-t) + pointF1.x*(1-t)*(1-t)*3*t +3*pointF2.x*(1-t)*t*t + endValue.x*t*t*t;
        pointF.y = startValue.y*(1-t)*(1-t)*(1-t) + pointF1.y*(1-t)*(1-t)*3*t +3*pointF2.y*(1-t)*t*t + endValue.y*t*t*t;
        return pointF;
    }

}
