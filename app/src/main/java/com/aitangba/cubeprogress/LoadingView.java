package com.aitangba.cubeprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fhf11991 on 2016/11/23.
 */
public class LoadingView extends View {

    private static final int ANIMATOR_DURATION = 2200;

    private final int SIDE_LENGTH = 100;
    private final float SHADOW_DISTANCE = 50;
    private final float sin30 = (float) Math.sin(30.0 * Math.PI / 180.0);
    private final float cos30 = (float) Math.cos(30.0 * Math.PI / 180.0);

    private Path mTopPath;
    private Paint mTopPaint;

    private Path mLeftPath;
    private Paint mLeftPaint;

    private Path mRightPath;
    private Paint mRightPaint;

    private Paint mShadowPaint;

    private ValueAnimator mLoadingAnimator;

    private PathMeasure mFirstPathMeasure;
    private float[] mFirstCubePosition = new float[2];

    private PathMeasure mSecondPathMeasure;
    private float[] mSecondCubePosition = new float[2];

    private PathMeasure mThirdPathMeasure;
    private float[] mThirdCubePosition = new float[2];

    private PathMeasure mFourthPathMeasure;
    private float[] mFourthCubePosition = new float[2];

    private boolean mIsFirstPart;
    private float mOriginPointX = 400;
    private float mOriginPointY = 400;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // init top
        mTopPaint = new Paint();
        mTopPaint.setColor(Color.parseColor("#FDFDE3"));

        mTopPath = new Path();
        mTopPath.moveTo(0, 0);
        mTopPath.lineTo(-SIDE_LENGTH * sin30, -SIDE_LENGTH * cos30);
        mTopPath.lineTo(-SIDE_LENGTH * sin30 + SIDE_LENGTH, -SIDE_LENGTH * cos30);
        mTopPath.lineTo(SIDE_LENGTH, 0);
        mTopPath.close();

        // init left
        mLeftPaint = new Paint();
        mLeftPaint.setColor(Color.parseColor("#EEDC70"));

        mLeftPath = new Path();
        mLeftPath.moveTo(0, 0);
        mLeftPath.lineTo(SIDE_LENGTH * sin30, -SIDE_LENGTH * cos30);
        mLeftPath.lineTo(SIDE_LENGTH * sin30 + SIDE_LENGTH, -SIDE_LENGTH * cos30);
        mLeftPath.lineTo(SIDE_LENGTH, 0);
        mLeftPath.close();

        // init left
        mRightPaint = new Paint();
        mRightPaint.setColor(Color.parseColor("#FAECA4"));

        mRightPath = new Path();
        mRightPath.moveTo(0, 0);
        mRightPath.lineTo(-SIDE_LENGTH * sin30, -SIDE_LENGTH * cos30);
        mRightPath.lineTo(-SIDE_LENGTH * sin30 + SIDE_LENGTH, -SIDE_LENGTH * cos30);
        mRightPath.lineTo(SIDE_LENGTH, 0);
        mRightPath.close();

        //init shadow
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.parseColor("#E6E6DC"));
        mShadowPaint.setAntiAlias(true);

        mLoadingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ANIMATOR_DURATION);
        mLoadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLoadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                float firstDis = value * mFirstPathMeasure.getLength();
                if(firstDis < SIDE_LENGTH) {
                    mIsFirstPart = true;
                } else {
                    mIsFirstPart = false;
                }
                mFirstPathMeasure.getPosTan(firstDis , mFirstCubePosition, null);
                mSecondPathMeasure.getPosTan(value * mSecondPathMeasure.getLength() , mSecondCubePosition, null);
                mThirdPathMeasure.getPosTan(value * mThirdPathMeasure.getLength() , mThirdCubePosition, null);
                mFourthPathMeasure.getPosTan(value * mFourthPathMeasure.getLength() , mFourthCubePosition, null);
                postInvalidate();
            }
        });

        //轨迹
        Path firstMovePath = new Path();
        firstMovePath.moveTo(0, 0);
        firstMovePath.lineTo(SIDE_LENGTH * 2 * cos30, SIDE_LENGTH * 2  * sin30);
        mFirstPathMeasure = new PathMeasure(firstMovePath, false);

        Path secondMovePath = new Path();
        secondMovePath.moveTo(SIDE_LENGTH * cos30, SIDE_LENGTH * sin30);
        secondMovePath.lineTo(SIDE_LENGTH * 2 * cos30, SIDE_LENGTH * 2  * sin30);
        secondMovePath.lineTo(SIDE_LENGTH * 2 * cos30 - SIDE_LENGTH * cos30, SIDE_LENGTH * 2  * sin30 + SIDE_LENGTH * sin30);
        mSecondPathMeasure = new PathMeasure(secondMovePath, false);

        Path thirdMovePath = new Path();
        thirdMovePath.moveTo(0, SIDE_LENGTH * 2 * sin30);
        thirdMovePath.lineTo(-SIDE_LENGTH * cos30 , SIDE_LENGTH * 2 * sin30 - SIDE_LENGTH * sin30);
        thirdMovePath.lineTo(0, 0);
        mThirdPathMeasure = new PathMeasure(thirdMovePath, false);

        Path fourthMovePath = new Path();
        fourthMovePath.moveTo(SIDE_LENGTH * cos30, SIDE_LENGTH * sin30 * 3);
        fourthMovePath.lineTo(-SIDE_LENGTH * cos30, SIDE_LENGTH * sin30);
        mFourthPathMeasure = new PathMeasure(fourthMovePath, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mOriginPointX = getMeasuredWidth() / 2 - SIDE_LENGTH * cos30;
        mOriginPointY = getMeasuredHeight() / 2 - (10 * SIDE_LENGTH *sin30 + SIDE_LENGTH + SHADOW_DISTANCE) / 2 + 2 * SIDE_LENGTH * sin30;

        //move the origin point
        canvas.translate(mOriginPointX, mOriginPointY);
        int boomIndex = canvas.save();

        if(mIsFirstPart) {
            //first
            drawDiamond(canvas, mFirstCubePosition[0], mFirstCubePosition[1]);
            canvas.restoreToCount(boomIndex);
            boomIndex = canvas.save();

            //second
            drawDiamond(canvas, mSecondCubePosition[0], mSecondCubePosition[1]);
            canvas.restoreToCount(boomIndex);
            boomIndex = canvas.save();

            //third
            drawDiamond(canvas, mThirdCubePosition[0], mThirdCubePosition[1]);
            canvas.restoreToCount(boomIndex);
            boomIndex = canvas.save();

            //fourth
            drawDiamond(canvas, mFourthCubePosition[0], mFourthCubePosition[1]);
            canvas.restoreToCount(boomIndex);
        } else {
            //third
            drawDiamond(canvas, mThirdCubePosition[0], mThirdCubePosition[1]);
            canvas.restoreToCount(boomIndex);
            boomIndex = canvas.save();

            //first
            drawDiamond(canvas,
                    Math.min(mFirstCubePosition[0], SIDE_LENGTH * cos30),
                    Math.min(mFirstCubePosition[1], SIDE_LENGTH * sin30));
            canvas.restoreToCount(boomIndex);
            boomIndex = canvas.save();

            //fourth
            drawDiamond(canvas,
                    Math.max(mFourthCubePosition[0], 0),
                    Math.max(mFourthCubePosition[1], SIDE_LENGTH * 2 * sin30));
            canvas.restoreToCount(boomIndex);
            boomIndex = canvas.save();

            //second
            drawDiamond(canvas, mSecondCubePosition[0], mSecondCubePosition[1]);
            canvas.restoreToCount(boomIndex);
        }
    }

    private void drawDiamond(Canvas canvas, float left, float top) {
        canvas.translate(left, top);
        int secondIndex = canvas.save();

        //init top part
        canvas.rotate(-30);
        canvas.drawPath(mTopPath, mTopPaint);
        canvas.restoreToCount(secondIndex);
        secondIndex = canvas.save();

        //init left part
        canvas.translate(0, SIDE_LENGTH);
        canvas.rotate(-90);
        canvas.drawPath(mLeftPath, mLeftPaint);
        canvas.restoreToCount(secondIndex);
        secondIndex = canvas.save();

        //init right part
        canvas.rotate(90);
        canvas.drawPath(mRightPath, mRightPaint);
        canvas.restoreToCount(secondIndex);

        //shadow
        canvas.translate(0, 3 * SIDE_LENGTH * sin30 + SIDE_LENGTH + SHADOW_DISTANCE +  2 * SIDE_LENGTH * sin30);
        canvas.rotate(-30);
        canvas.drawPath(mTopPath, mShadowPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLoadingAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        mLoadingAnimator.cancel();
        super.onDetachedFromWindow();
    }
}
