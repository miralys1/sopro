package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swarm.swarmcomposerapp.Model.CompatibilityAnswer;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Edge;
import swarm.swarmcomposerapp.Model.Node;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.R;

/**
 * Custom View for displaying a composition.
 */
public class CompositionView extends View {

    //Padding instance vars
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBot;

    //Paints for specifying how the view should draw objects
    /**
     * Paint for nodes and possible other objects.
     */
    private Paint drawPaint;
    /**
     * Paint of the edges
     */
    private Paint edgePaint;
    /**
     * Paint for highlighting objects.
     */
    private Paint highlightPaint;

    private float offsetX;
    private float offsetY;

    private PointF focusPoint;


    /**
     * Scale factor for zooming on pinch
     */
    private float scaleFactor = 1.f;

    /**
     * Initial X coordinate for drawing
     */
    private float initX = 100;
    /**
     * Initial Y coordinate for drawing
     */
    private float initY = 100;


    /**
     * Radius of nodes
     */
    private float radius = 100;

    /**
     * The currently selected Node
     */
    private Node selectedNode;


    private int maxX = 0;
    private int maxY = 0;
    private int minX = 0;
    private int minY = 0;

    /**
     * Returns the currently selected Node. Attention: It returns NULL if no node is selected.
     *
     * @return
     */
    public Node getSelectedNode() {
        return selectedNode;
    }

    /**
     * Returns the composition that is drawn by the CompositionView. It's NULL
     * if no composition has been set.
     *
     * @return
     */
    public Composition getComp() {
        return comp;
    }


    /**
     * This is the compositions that is drawn by the CompositionView.
     */
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


    /**
     * Gesture Detector for selecting edges or nodes and panning.
     */
    private final GestureDetector.SimpleOnGestureListener gestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            initX -= distanceX;
            initY -= distanceY;

            invalidate();

            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            final List<Node> nodeList = comp.getNodeList();

            if (comp != null && nodeList != null && !nodeList.isEmpty()) {
                float posX = (e.getX() - initX - offsetX) / scaleFactor;
                float posY = (e.getY() - initY - offsetY) / scaleFactor;

                //float posX = (e.getX()-initX)/scaleFactor;
                //float posY = (e.getY()-initY)/scaleFactor;
                boolean set = false;

                for (Node n : nodeList) {
                    float absX = Math.abs(n.getX() - posX);
                    float absY = Math.abs(n.getY() - posY);
                    double distance = Math.sqrt(absX * absX + absY * absY);
                    Log.d("SingleTap", "posX: " + posX + " posY " + posY + " node X " + n.getX()
                            + " node Y " + n.getY() + " dist " + distance + " radius " + radius * scaleFactor);

                    //If the single tap occurs within a node select it
                    //The first encountered node is selected
                    if (distance <= radius) {
                        selectedNode = n;
                        set = true;
                        invalidate();
                    }
                }


                if (!set) {
                    selectedNode = null;
                    invalidate();
                }
            }
            return true;

        }


    };

    private final ScaleGestureDetector scaleDetec
            = new ScaleGestureDetector(getContext(), new ScaleListener());
    private final GestureDetector gestureDetec
            = new GestureDetector(getContext(), gestureListener);


    public void setComp(Composition comp) {
        this.comp = comp;
    }


    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CompositionView, defStyle, 0);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBot = getPaddingBottom();

        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setColor(Color.GRAY);
        drawPaint.setStyle(Paint.Style.FILL);

        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint.setColor(Color.BLUE);
        edgePaint.setStyle(Paint.Style.FILL);
        edgePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        edgePaint.setStrokeWidth(4);

        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setColor(Color.BLUE);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(3);


    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        scaleDetec.onTouchEvent(ev);
        gestureDetec.onTouchEvent(ev);


        return true;
    }

    /**
     * Small helper method for removing .png ending from a file name.
     *
     * @param urlPic
     * @return
     */
    public static String removePNGEnding(String urlPic) {

        if (urlPic.endsWith(".png")) {
            return (String) urlPic.subSequence(0, urlPic.length() - 4);

        } else {
            return urlPic;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();


        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBot;


        if (comp != null) {

            List<Node> nodes = comp.getNodeList();


            int initialLength = (int) radius;
            float halfLength = initialLength / (float) 2;


//
//            float dX = maxX-minX;
//            float dY = maxY-minY;
//
//            float xS = contentWidth/dX;
//            float xY = contentHeight/dY;
//
//            float bigger = xS>xY?xS : xY;
//
//            Log.i("Werte",""+maxX+" "+maxY );


            //translate for moving the canvas

            canvas.translate(initX, initY);
            //zoom scale
            canvas.translate(offsetX, offsetY);
            canvas.scale(scaleFactor, scaleFactor);

            for (Edge e : comp.getEdgeList()) {
                Node source = e.getIn();
                Node target = e.getOut();


                final CompatibilityAnswer compatibilityN = e.getCompatibility();

                // Draw the edge in a color indicating the compatibility
                if (compatibilityN.isCompatible()) {
                    //nodes are compatible
                    edgePaint.setColor(Color.GREEN);
                } else {
                    if (compatibilityN.getAlternatives() == null
                            || compatibilityN.getAlternatives().isEmpty()) {
                        //nodes are not compatible
                        edgePaint.setColor(Color.RED);
                    } else {
                        //nodes are not compatible, but there are alternatives
                        edgePaint.setColor(Color.YELLOW);
                    }
                }

                //Draw an edge as a line styled by edgePaint
                canvas.drawLine(halfLength + source.getX(), halfLength + source.getY(),
                        halfLength + target.getX(), halfLength + target.getY(), edgePaint);


            }


            for (Node n : nodes) {
                //draw the current node as a filled, grey circle
                canvas.drawCircle(n.getX(), n.getY(), initialLength, drawPaint);
                //Log.d("Node", "X: " + n.getX() + " Y: " + n.getY());

                //The current node has been selected be a single tap event.
                if (n == selectedNode) {
                    canvas.drawCircle(n.getX(), n.getY(), initialLength, highlightPaint);
                }

                /*
                  Attempt to get the resource of the image
                 */
                int drawableID = getContext().getResources()
                        .getIdentifier(removePNGEnding(n.getSendService().getPicture()).toLowerCase(),
                                "drawable", getContext().getPackageName());
                //Log.d("Drawable", n.getSendService().getPicture().toLowerCase() + " id: " + drawableID);

                //In the case that the drawableID is 0 the resource couldn't be found.
                if (drawableID != 0) {

                    final Drawable drawable = getContext().getDrawable(drawableID);

                    drawable.setBounds(0, 0, (int) initialLength, (int) initialLength);
                    canvas.translate(n.getX() - halfLength, n.getY() - halfLength);
                    drawable.draw(canvas);
                    canvas.translate(-n.getX() + halfLength, -n.getY() + halfLength);
                }
            }


        }

        canvas.restore();


    }

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(", ");
        }
        String toReturn = sb.toString();
        return toReturn.substring(0, toReturn.length() - 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBot = getPaddingBottom();
        // Try for a width based on our minimum
        int minw = paddingLeft + paddingRight + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + paddingBot + paddingTop;
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);


        if (comp != null && comp.getNodeList() != null && !comp.getNodeList().isEmpty()) {
            //Search for the maximal coordinates in the list of nodes.
            maxX = 0;
            maxY = 0;
            minX = 0;
            minY = 0;

            for (Node n : comp.getNodeList()) {
                int tX = n.getX();
                int tY = n.getY();

                if (tX > maxX) {
                    maxX = tX;
                }
                if (tY > maxY) {
                    maxY = tY;
                }

                if (tX < minX) {
                    minX = tX;
                }

                if (tY < minY) {
                    minY = tY;
                }
            }

            initX = minX + w / 5;
            initY = minY + h / 5;

        }


        setMeasuredDimension(w, h);
    }


    /**
     * This Listener is used for scaling in the CompositionView.
     */
    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float oldScale = scaleFactor;

            scaleFactor *= detector.getScaleFactor();

            float scaleChange = scaleFactor - oldScale;
            offsetX = -focusPoint.x * scaleChange;
            offsetY = -focusPoint.y * scaleChange;

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            invalidate();
            return true;
        }


        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            focusPoint = new PointF(detector.getFocusX(), detector.getFocusY());

            return true;
        }


    }


}
