package com.busilinq.lovebezier;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/18
 * author: shiquan.lu
 */

public class LoveLayout extends RelativeLayout {

    private Drawable[] drawables = new Drawable[2];
    private LayoutParams params;
    private int drawWidth;
    private int drawHeight;
    private int mWidth;
    private int mHeight;

    //图片集合为了性能优化
    private LinkedList<ImageView> imageList;

    private Random random = new Random();


    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        imageList = new LinkedList<>();
        drawables[0] = getResources().getDrawable(R.mipmap.chat_room_vote_bg);
        drawables[1] = getResources().getDrawable(R.mipmap.chat_room_vote_bg_self);
        drawWidth = drawables[0].getIntrinsicWidth();
        drawHeight = drawables[0].getIntrinsicHeight();
        params = new LayoutParams(drawWidth,drawHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 创建属性动画
     * @return
     */
    private AnimatorSet getAnimatorSet(ImageView imageView,PointF pointF) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView,"scaleX",0.2F,1.0F);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView,"scaleY",0.2F,1.0F);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView,"alpha",0.2F,1.0F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX,scaleY,alpha);
        animatorSet.setDuration(500);
        //贝塞尔曲线动画
        ValueAnimator bezier  = getValueAnimator(imageView,pointF);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animatorSet,bezier);
        set.setTarget(imageView);
        return set;
    }

    private ValueAnimator getValueAnimator(final ImageView imageView, final PointF pointF) {
        //起始点
        PointF point0 = new PointF();
        point0.x = pointF.x - drawWidth/2;
        point0.y = pointF.y - drawHeight/2;

        //波点
        PointF point1 = getTogglePoint(1,pointF);
        PointF point2 = getTogglePoint(2,pointF);

        //结束点
        PointF point3 = new PointF(random.nextInt(mWidth),0);
        LoveValueEvalutor loveValueEvalutor = new LoveValueEvalutor(point1,point2);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(loveValueEvalutor,point0,point3);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF1 = (PointF) valueAnimator.getAnimatedValue();
                imageView.setX(pointF1.x);
                imageView.setY(pointF1.y);
                imageView.setAlpha(1 - valueAnimator.getAnimatedFraction());
                if(0 == 1 - valueAnimator.getAnimatedFraction()){
                    removeView(imageView);
                }
            }
        });
        return valueAnimator;
    }

    private PointF getTogglePoint(int i, PointF pointF) {
        PointF pointF1 = new PointF();
        pointF1.x = random.nextInt(mWidth);
        int reacHeight = (int) (pointF.y /2);
        if(i ==1){
           pointF1.y = reacHeight + random.nextInt(reacHeight);
        }else if (i ==2 ){
            pointF1.y =  random.nextInt(reacHeight);
        }
        return pointF1;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            PointF pointF = new PointF();
            pointF.x = event.getX();
            pointF.y = event.getY();
            addLove(pointF);
        }
        return true;
    }

    private void addLove(PointF pointF) {
        params = new LayoutParams(drawWidth,drawHeight);
        params.leftMargin = (int) (pointF.x -drawWidth/2);
        params.topMargin = (int) (pointF.y -drawHeight/2);
        ImageView imageView = new ImageView(getContext());
        AnimatorSet animatorSet = getAnimatorSet(imageView,pointF);
        imageView.setImageDrawable(drawables[random.nextInt(2)]);
        addView(imageView,params);
        animatorSet.start();
    }
}
