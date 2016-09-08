package stepview.qwy.com.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：仇伟阳
 *
 * description: StepsViewIndicator
 */
public class HorizontalStepsViewIndicator extends View{
    // 定义默认的高度
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension
            (TypedValue.COMPLEX_UNIT_DIP,40,getResources().getDisplayMetrics());

    private float mCompletedLineHeight; //完成线的高度
    private float mCircleRadius; // 圆的半径

    private Drawable mCompleteIcon; //完成的默认图片
    private Drawable mAttentionIcon; // 正在进行的默认图片
    private Drawable mDefaultIcon; // 默认的背景图（还没有执行到的步骤）
    private float mCenterY; // 该view的Y轴中间位置
    private float mLeftY; // 左上方的位置
    private float mRightY; // 右下方的位置

    private int mStepNum = 0; // 当前有几步流程
    private float mLinePadding; //两条线之间的距离

    private List<Float> mCircleCenterPointPositionList;//所有圆心的集合
    private Paint mUnCompletedPaint; //未完成 paint
    private Paint mCompletedPaint; //完成 paint
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(),
            R.color.uncompleted_color);//定义默认未完成线的颜色
    private int mCompletedLineColor = Color.WHITE;//定义默认完成线的颜色
    private PathEffect mEffects; //画虚线需要使用的API

    private int mComplectingPosition;//正在进行position   underway position
    private Path mPath;

    private int screenWidth;//this screen width

    private OnDrawIndicatorListener mOnDrawListener;

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener)
    {
        mOnDrawListener = onDrawListener;
    }

    public HorizontalStepsViewIndicator(Context context) {
        this(context, null);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPath = new Path();

        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);
        mCircleCenterPointPositionList = new ArrayList<>();

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();

        mUnCompletedPaint.setAntiAlias(true); //设置消除锯齿
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);//设置样式
        mUnCompletedPaint.setStrokeWidth(2); //paint的外框高度

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);


        //已经完成线的宽高
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //圆的半径
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的间距
        mLinePadding = 0.85f * defaultStepIndicatorNum;


        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);//未完成的icon

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec))
        {
            //自定义view的父控件宽度
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec))
        {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        //需要画的view的宽度
        width = (int) (mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding);
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取中间的高度,目的是为了让该view绘制的线和圆在该view垂直居中   get view centerY，keep current stepview center vertical
        mCenterY = 0.5f * getHeight();
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        for(int i = 0; i < mStepNum; i++)
        {
            //先计算全部最左边的padding值（getWidth()-（圆形直径+两圆之间距离））/2
            float paddingLeft = (screenWidth - mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding) / 2;
            //add to list
            mCircleCenterPointPositionList.add(paddingLeft + mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
        }

        /**
         * set listener
         */
        if(mOnDrawListener != null)
        {
            mOnDrawListener.ondrawIndicator();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mOnDrawListener != null)
        {
            mOnDrawListener.ondrawIndicator();
        }

        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);
        //________________________________画线________________________________
            for (int i =0; i< mCircleCenterPointPositionList.size()-1;i++){
                //前一个圆的圆心
                float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
                //后一个圆的圆心
                float afterComplectedXPosition = mCircleCenterPointPositionList.get(i+1);
                if(i<mComplectingPosition){
                    canvas.drawRect(preComplectedXPosition+mCircleRadius-10,mLeftY,
                            afterComplectedXPosition-mCircleRadius+10,mRightY,mCompletedPaint);
                }else{
                    mPath.moveTo(preComplectedXPosition + mCircleRadius, mCenterY);
                    mPath.lineTo(afterComplectedXPosition - mCircleRadius, mCenterY);
                    canvas.drawPath(mPath, mUnCompletedPaint);
                }
            }
        //________________________________画线________________________________________
        //-------------------------------画图标----------------------------------------
            for (int i=0;i<mCircleCenterPointPositionList.size();i++){
                float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
                Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius), (int)
                        (mCenterY - mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius),
                        (int) (mCenterY + mCircleRadius));
                if(i<mComplectingPosition){
                    //已经完成的步骤
                    mCompleteIcon.setBounds(rect);
                    mCompleteIcon.draw(canvas);
                }else if(i == mComplectingPosition && mCircleCenterPointPositionList.size() != 1){
                    //正在进行的步骤
                    mCompletedPaint.setColor(Color.WHITE);
                    canvas.drawCircle(currentComplectedXPosition,mCenterY,mCircleRadius*1.0f,mCompletedPaint);
                    mAttentionIcon.setBounds(rect);
                    mAttentionIcon.draw(canvas);
                }else{
                    //还没有完成的步骤
                    mDefaultIcon.setBounds(rect);
                    mDefaultIcon.draw(canvas);
                }

            }
        //-------------------------------画图标----------------------------------------
    }

    /**
     * 得到所有远点所在的位置
     * @return
     */
    public List<Float> getCircleCenterPointPositionList(){
        return mCircleCenterPointPositionList;
    }

    /**
     * 设置流程步数
     * @param stepNum
     */
    public void setStepNum(int stepNum){
        mStepNum = stepNum;
    }


    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener
    {
        void ondrawIndicator();
    }

    /**
     * 设置正在进行的步骤
     * @param complectingPosition
     */
    public void setCompleteingPosition(int complectingPosition){
        mComplectingPosition = complectingPosition;
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor)
    {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor)
    {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon)
    {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon)
    {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon)
    {
        this.mAttentionIcon = attentionIcon;
    }
}
