package com.zackmatthews.binarytreevisualizationproject;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


/**
 * TODO: document your custom view class.
 */
public class RenderView extends View implements SurfaceHolder {

    private TextPaint mNodePaint;
    private TextPaint mNumPaint;
    private TextPaint mSelectedNodePaint;

    private Context context;

    private NodeManager nodeManager;
    private Node selectedNode = null;

    public static Point screenSize = new Point();


    public static int MIN_TREE_SIZE = 2;
    public static int MAX_TREE_SIZE = 6;

    public static int treeSize = 5;


    public static int defaultScreenWidth = 2560;
    public static int defaultScreenHeight = 1440;

    private Button insertButton;
    private Button searchButton;


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


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(screenSize);

        // Set up a default TextPaint object
        mNodePaint = new TextPaint();
        mNodePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mNodePaint.setTextAlign(Paint.Align.LEFT);
        mNodePaint.setColor(Color.GRAY);

        mSelectedNodePaint = new TextPaint();
        mSelectedNodePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mSelectedNodePaint.setTextAlign(Paint.Align.LEFT);
        mSelectedNodePaint.setColor(Color.RED);


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
        nodeManager.populateTree(head, treeSize);


    }

    private Node head;
    private int paddingLeft, paddingTop, paddingRight, paddingBottom, contentWidth, contentHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.invalidate();
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;


        traverseNodes(head, canvas, mNumPaint);

        if (insertButton == null) {
            try {
                insertButton = (Button) this.getRootView().findViewById(R.id.pushNodeButton);


                insertButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showNodeValueInput(BinaryMode.Insert);
                    }
                });
            } catch (Exception e) {

            }

        }


        if (searchButton == null) {
            try {
                searchButton = (Button) this.getRootView().findViewById(R.id.searchButton);


                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showNodeValueInput(BinaryMode.Search);
                    }
                });
            } catch (Exception e) {

            }

        }

    }


    private PopupWindow popupWindow = null;

    protected enum BinaryMode {
        Search,
        Insert
    }

    public void showNodeValueInput(final BinaryMode mode) {
        LinearLayout rootContainer = new LinearLayout(context);
        final EditText input = new EditText(context);
        final TextView errorText = new TextView(context);
        Button dismissButton = new Button(context);
        LinearLayout inLineContainer = new LinearLayout(context);

        rootContainer.setOrientation(LinearLayout.VERTICAL);


        inLineContainer.setOrientation(LinearLayout.HORIZONTAL);


        input.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                errorText.setVisibility(View.GONE);
            }
        });

        input.setHint("Node value");
        input.setHintTextColor(Color.CYAN);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.setTextColor(Color.CYAN);
        input.setGravity(Gravity.CENTER);


        errorText.setVisibility(View.GONE);

        String modeText = (mode == BinaryMode.Insert) ? "Push Node" :
                (mode == BinaryMode.Search) ? "Search" :
                        "null operation";
        dismissButton.setText(modeText);

        dismissButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (input.getText().toString().equals("")) {
                    errorText.setText("Please enter a valid value");
                    errorText.setVisibility(View.VISIBLE);
                    input.setText("");


                } else {

                    if (selectedNode != null) {
                        nodeManager.findByValue(head, selectedNode.value, true);
                        selectedNode = null;
                    }
                    if (mode == BinaryMode.Insert) {

                            nodeManager.insert(head, Integer.parseInt(input.getText().toString()));
                            popupWindow.dismiss();

                    }
                    else if (mode == BinaryMode.Search) {


                                selectedNode = nodeManager.findByValue(head, Integer.parseInt(input.getText().toString()), false);

                                String resultText = "";
                                if (selectedNode != null) {
                                    resultText = "Node value: " + String.valueOf(selectedNode.value);
                                } else {
                                    resultText = "Value not found";
                                }
                                Toast.makeText(context, resultText, Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();




                        }
                    }
                }

        });


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.topMargin = 20;


        LinearLayout.LayoutParams layoutParamsForInlineContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParamsForInlineContainer.topMargin = 30;


        LinearLayout.LayoutParams layoutParamsForInlineET = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        layoutParamsForInlineET.weight = 1;


        LinearLayout.LayoutParams layoutParamsForInlineButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParamsForInlineButton.weight = 0;

        //inLineContainer.addView(inLineContainer, layoutParamsForInlineContainer);
        inLineContainer.addView(input, layoutParamsForInlineET);
        inLineContainer.addView(dismissButton, layoutParamsForInlineButton);
        //inLineContainer.addView(errorText, layoutParams);


        inLineContainer.setGravity(Gravity.CENTER);
        inLineContainer.setBackgroundColor(0x95000000);


        popupWindow = new PopupWindow(inLineContainer,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(inLineContainer, Gravity.CENTER, 0, 0);


    }


    /**
     * * Traverses tree to connect each child node ***
     */
    public void traverseNodes(Node child, Canvas canvas, Paint paint) {

        Node childLeft, childRight;

        Paint mPaint = null;
        if (child.type.equals(Node.NODE_TYPE_HEAD)) {

            if (!child.isHighlightedInPath) {
                mPaint = mNodePaint;
            } else {
                mPaint = mSelectedNodePaint;
            }


            canvas.drawCircle(head.pos_x,
                    head.pos_y, head.radius, mPaint);

            canvas.drawText(String.valueOf(head.value),
                    head.pos_x,
                    head.pos_y,
                    mNumPaint);
        }

        if (child.childLeft != null) {
            childLeft = child.childLeft;

            if (!childLeft.isHighlightedInPath) {
                mPaint = mNodePaint;
            } else {
                mPaint = mSelectedNodePaint;
            }


            canvas.drawCircle(childLeft.pos_x,
                    childLeft.pos_y, childLeft.radius, mPaint);


            canvas.drawText(String.valueOf(childLeft.value),
                    childLeft.pos_x,
                    childLeft.pos_y,
                    mNumPaint);

            canvas.drawLine(child.pos_x, child.pos_y, childLeft.pos_x, childLeft.pos_y, paint);


            traverseNodes(childLeft, canvas, paint);


        }
        if (child.childRight != null) {
            childRight = child.childRight;

            if (!childRight.isHighlightedInPath) {
                mPaint = mNodePaint;
            } else {
                mPaint = mSelectedNodePaint;
            }

            canvas.drawCircle(childRight.pos_x,
                    childRight.pos_y, childRight.radius, mPaint);

            canvas.drawText(String.valueOf(childRight.value),
                    childRight.pos_x,
                    childRight.pos_y,
                    mNumPaint);

            canvas.drawLine(child.pos_x, child.pos_y, childRight.pos_x, childRight.pos_y, paint);


            traverseNodes(childRight, canvas, paint);

        }


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
