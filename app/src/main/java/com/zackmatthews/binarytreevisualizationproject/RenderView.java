package com.zackmatthews.binarytreevisualizationproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;


/**
 * TODO: document your custom view class.
 */
public class RenderView extends View implements SurfaceHolder {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mNodePaint;
    private TextPaint mNumPaint;
    private float mTextWidth;
    private float mTextHeight;


    private Context context;
    private NodeManager nodeManager;


    public static Point screenSize = new Point();


    public static int MIN_TREE_SIZE = 2;
    public static int MAX_TREE_SIZE = 6;

    public static int treeSize = 5;
    public RenderView(Context context) {
        super(context);

        this.context = context;
        init(null, 0);
    }

    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public RenderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RenderView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.RenderView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.RenderView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.RenderView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.RenderView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.RenderView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();



        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(screenSize);

        // Set up a default TextPaint object
        mNodePaint = new TextPaint();
        mNodePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mNodePaint.setTextAlign(Paint.Align.LEFT);
        mNodePaint.setColor(Color.GRAY);


        mNumPaint = new TextPaint();
        mNumPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mNumPaint.setTextAlign(Paint.Align.CENTER);
        mNumPaint.setTextSize(32.0f);
        mNumPaint.setColor(Color.CYAN);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        head = new Node(2000, screenSize.x / 2, 50, NodeManager.radius); //hard coded for test
        head.type = Node.NODE_TYPE_HEAD;
        head.pos_x -= head.radius;


        nodeManager = new NodeManager(head);


        nodeManager.populateTree(head, treeSize); //hardcoded for test

    }

    private Node head;
    private int paddingLeft, paddingTop, paddingRight, paddingBottom, contentWidth, contentHeight;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.invalidate();
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
//        canvas.drawText("Hello World",
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);


            traverseNodes(head, canvas, mNumPaint);



    }

/**** Traverses tree to connect each child node ****/
    public void traverseNodes(Node child, Canvas canvas, Paint paint){

        Node childLeft, childRight;

        if(child.type.equals(Node.NODE_TYPE_HEAD)){

            canvas.drawCircle(head.pos_x,
                    head.pos_y, head.radius, mNodePaint);

            canvas.drawText(String.valueOf(head.value),
                    head.pos_x,
                    head.pos_y,
                    mNumPaint);
        }

        if(child.childLeft != null){
            childLeft = child.childLeft;

            canvas.drawCircle(childLeft.pos_x,
                    childLeft.pos_y, childLeft.radius, mNodePaint);


            canvas.drawText(String.valueOf(childLeft.value),
                    childLeft.pos_x,
                    childLeft.pos_y,
                    mNumPaint);

            canvas.drawLine(child.pos_x , child.pos_y, childLeft.pos_x, childLeft.pos_y, paint);


            traverseNodes(childLeft, canvas, paint);


        }
        if(child.childRight != null) {
            childRight = child.childRight;

            canvas.drawCircle(childRight.pos_x,
                    childRight.pos_y, childRight.radius, mNodePaint);

            canvas.drawText(String.valueOf(childRight.value),
                    childRight.pos_x,
                    childRight.pos_y,
                    mNumPaint);

            canvas.drawLine(child.pos_x , child.pos_y, childRight.pos_x, childRight.pos_y, paint);


            traverseNodes(childRight, canvas, paint);

        }


    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;

    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;

    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;

    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    @Override
    public void addCallback(Callback callback) {

    }

    @Override
    public void removeCallback(Callback callback) {

    }

    @Override
    public boolean isCreating() {
        return false;
    }

    @Override
    public void setType(int type) {

    }

    @Override
    public void setFixedSize(int width, int height) {

    }

    @Override
    public void setSizeFromLayout() {

    }

    @Override
    public void setFormat(int format) {

    }

    @Override
    public void setKeepScreenOn(boolean screenOn) {

    }

    @Override
    public Canvas lockCanvas() {
        return null;
    }

    @Override
    public Canvas lockCanvas(Rect dirty) {
        return null;
    }

    @Override
    public void unlockCanvasAndPost(Canvas canvas) {

    }

    @Override
    public Rect getSurfaceFrame() {
        return null;
    }

    @Override
    public Surface getSurface() {
        return null;
    }
}
