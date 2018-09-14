package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Edge;
import swarm.swarmcomposerapp.Model.Node;
import swarm.swarmcomposerapp.R;

/**
 * TODO: document your custom view class.
 */
public class CompositionView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBot;

    private int contentWidth;
    private int contentHeight;

    public void setComp(Composition comp) {
        this.comp = comp;
    }

    private Composition comp;

    public CompositionView(Context context) {
        super(context);
        init(null, 0);
    }

    public CompositionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CompositionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public CompositionView(Context context, AttributeSet attrs, Composition comp) {
        super(context, attrs);
        init(attrs, 0);
        this.comp = comp;
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CompositionView, defStyle, 0);

        mExampleString = "bla";
        mExampleColor = a.getColor(
                R.styleable.CompositionView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CompositionView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.CompositionView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CompositionView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    public RectF[] createRectsForNodes(List<Node> nodes, int maxX, int maxY, int length) {
        /*If  maxX or maxY are bigger than the view is able to handle
        / then scale all coordinates down.*/
        float convertX = 1;
        float convertY = 1;
        if (maxX > contentWidth) {
            convertX = contentWidth / (float) maxX;
            maxX = contentWidth;
        }
        if (maxY > contentHeight) {
            convertY = contentHeight / (float) maxY;
            maxY = contentHeight;
        }


        // rects stores the coordinates for every node
        RectF[] rects = new RectF[nodes.size()];
        //pointer for the array
        int pointer = 0;


        boolean xHasToBeConverted = convertX != 1;
        boolean yHasToBeConverted = convertY != 1;
        float left;
        float top;
        float right;
        float bot;
        for (Node n : nodes) {
            left = n.getX();
            top = n.getY();
            if (xHasToBeConverted || yHasToBeConverted) {

                if (xHasToBeConverted) {
                    if (left != maxX) {
                        left *= convertX;
                    } else {
                        left = maxX;
                    }
                }

                if (yHasToBeConverted) {
                    if (left != maxY) {
                        top *= convertY;
                    } else {
                        top = maxY;
                    }
                }
            }

            bot = top + length;
            right = left + length;

            rects[pointer] = new RectF(left, top, right, bot);
            pointer++;
        }

        return rects;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStyle(Paint.Style.FILL);

        Paint edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint.setColor(Color.BLUE);
        edgePaint.setStyle(Paint.Style.FILL);
        edgePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        edgePaint.setStrokeWidth(8);

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBot;
        Toast.makeText(getContext(),"Test",Toast.LENGTH_LONG).show();


        if(comp != null){
            List<Node> nodes = comp.getNodeList();


            int initialLength = 100;

            //Search for the maximal coordinates in the list of nodes.
            int maxX = 0;
            int maxY = 0;
            int minX = 0;
            int minY = 0;

            for (Edge e : comp.getEdgeList()) {
                Node source = e.getIn();
                Node target = e.getOut();

                float halfLength = initialLength /  (float)2;



                canvas.drawLine(halfLength+source.getX(),halfLength+source.getY(),
                        halfLength+target.getX(),halfLength+target.getY(), edgePaint);


            }

            for (Node n : nodes) {
                int tX = n.getX();
                int tY = n.getY();

                if (tX > maxX) {
                    maxX = tX;
                }
                if (tY > maxY) {
                    maxY = tY;
                }

                if(tX < minX){
                    minX = tX;
                }

                if(tY < minY){
                    minY = tY;
                }
            }


            for (Node n : nodes){
                canvas.drawCircle(n.getX(),n.getY(),initialLength,drawPaint);

                final Drawable drawable = getContext().getDrawable(R.drawable.tp_angebote);
                drawable.setBounds(0,0,150,150);
                canvas.translate(n.getX()-75,n.getY()-75);
                drawable.draw(canvas);
                canvas.translate(- n.getX()+75,-n.getY()+75);
            }



        }

        if (false) {
            List<Node> nodes = comp.getNodeList();


            //Calculate an edge length that is small enough to place all nodes on the screen
            //int length = contentHeight * contentWidth / (4 * nodes.size());



            //Draw the edges
            //TODO: Draw arrowheads
            //TODO: Draw with compatibility
            for (Edge e : comp.getEdgeList()) {
                Node source = e.getIn();
                Node target = e.getOut();

                int sIndex = nodes.indexOf(source);
                int tIndex = nodes.indexOf(target);
               // RectF srect = rects[sIndex];
               // RectF trect = rects[tIndex];
               // float halfLength = length / (float) 2;
//                float sMidX = srect.left + halfLength;
//                float sMidY = srect.top + halfLength;
//                float tMidX = trect.left + halfLength;
//                float tMidY = trect.top + halfLength;

//                canvas.drawLine(sMidX, sMidY, tMidX, tMidY, edgePaint);


            }
            // Draw the nodes as RoundRects
            //TODO: Draw node images :)
//            for (RectF r : rects) {
//                canvas.drawRoundRect(r, length / (float) 4, length / (float) 4, drawPaint);
//            }
        }
        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBot = getPaddingBottom();
        // Try for a width based on our minimum
        int minw = paddingLeft + paddingRight + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - (int) mTextWidth + paddingBot + paddingTop;
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int) mTextWidth, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
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
        invalidateTextPaintAndMeasurements();
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
        invalidateTextPaintAndMeasurements();
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
        invalidateTextPaintAndMeasurements();
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


}
