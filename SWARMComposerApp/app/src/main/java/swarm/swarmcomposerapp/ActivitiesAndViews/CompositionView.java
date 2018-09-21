package swarm.swarmcomposerapp.ActivitiesAndViews;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.List;

import swarm.swarmcomposerapp.Model.CompatibilityAnswer;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Edge;
import swarm.swarmcomposerapp.Model.Node;
import swarm.swarmcomposerapp.R;

/**
 * Custom View for displaying a composition. It should usually be embed in a DetailActivity.
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

    /**
     * Inverted View Matrix of the last call of onDraw().
     */
    private Matrix inverse;

    /**
     * Stores the user's desired point of focus.
     */
    private PointF focusPoint = new PointF(0, 0);


    /**
     * Scale factor for zooming on pinch
     */
    private float scaleFactor = 1.f;

    /**
     * Initial X coordinate for drawing
     */
    private float initX;
    /**
     * Initial Y coordinate for drawing
     */
    private float initY;


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
    private boolean onStartUp = true;

    private float currentWidth = 1000;
    private float currentHeight = 1000;

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

    /**
     * The parent of a CompositionView
     * Normal instances of CompositionViews are meant to be used inside of a DetailActivity.
     */
    private DetailActivity parent;

    /**
     * Sets the parent of a CompositionView.
     * Normal instances of CompositionViews are meant to be used inside of a DetailActivity.
     */
    public void setParentActivity(DetailActivity parent) {
        this.parent = parent;
    }

    public float getCurrentWidth() {
        return currentWidth;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

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

    /**
     * Gesture Detector for selecting edges or nodes and panning.
     */
    private final GestureDetector.SimpleOnGestureListener gestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            initX -= distanceX / scaleFactor;
            initY -= distanceY / scaleFactor;

            invalidate();

            return true;
        }

        /**
         * This EventHandling is needed for selecting nodes.
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            final List<Node> nodeList = comp.getNodeList();

            if (comp != null && nodeList != null && !nodeList.isEmpty()) {
                e.transform(inverse);
                float posX = e.getX();
                float posY = e.getY();

                boolean set = false;

                for (Node n : nodeList) {
                    float absX = Math.abs(n.getX() - posX);
                    float absY = Math.abs(n.getY() - posY);
                    double distance = Math.sqrt(absX * absX + absY * absY);

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
                parent.onNodeSelected(selectedNode);
            }
            return true;
        }
    };

    private final ScaleGestureDetector scaleDetec
            = new ScaleGestureDetector(getContext(), new ScaleListener());
    private final GestureDetector gestureDetec
            = new GestureDetector(getContext(), gestureListener);

    /**
     * SetComp() assigns a Composition Object to the CompositionView.
     *
     * @param comp
     */
    public void setComp(Composition comp) {
        this.comp = comp;

    }

    /**
     * init() is meant for initializing crucial variables and settings of the CompositionView.
     *
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CompositionView, defStyle, 0);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBot = getPaddingBottom();

        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setColor(ContextCompat.getColor(getContext(), R.color.background_light));
        drawPaint.setStyle(Paint.Style.FILL);

        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        edgePaint.setStyle(Paint.Style.FILL);
        edgePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        edgePaint.setStrokeWidth(15);

        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(10);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        scaleDetec.onTouchEvent(ev);
        if (!scaleDetec.isInProgress()) {

            gestureDetec.onTouchEvent(ev);
        }


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

        if (comp != null) {

            List<Node> nodes = comp.getNodeList();


            int initialLength = (int) radius;

            currentWidth = canvas.getClipBounds().right;
            currentHeight = canvas.getClipBounds().bottom;
            //translate for moving the canvas
            //scale for zooming
            Matrix matrix = new Matrix();
            if(onStartUp){
                initX = -minX + currentWidth / 5;
                initY = -minY + currentHeight / 5;
                float xD = Math.abs(minX-currentWidth / 5-initialLength-maxX);
                float yD = Math.abs(minY-currentHeight / 5-initialLength-maxY);

                if(xD > currentWidth || yD>currentHeight){
                    scaleFactor = Math.min(currentWidth/xD, currentHeight/yD);
                }
                onStartUp = false;
            }
            matrix.postTranslate(initX, initY);
            matrix.postScale(scaleFactor, scaleFactor, focusPoint.x, focusPoint.y);
            canvas.concat(matrix);

            //Invert the matrix and store it for later reverse calculations
            inverse = new Matrix(matrix);
            inverse.invert(inverse);

            //Draw the edges - drawing the edges first eases the task.
            for (Edge e : comp.getEdgeList()) {
                Node source = e.getOut();
                Node target = e.getIn();
                float sX = source.getX();
                float sY = source.getY();

                float tX = target.getX();
                float tY = target.getY();

                final CompatibilityAnswer compatibilityN = e.getCompatibility();

                // Draw the edge in a color indicating the compatibility
                if (compatibilityN.isCompatible()) {
                    //nodes are compatible
                    edgePaint.setColor(ContextCompat.getColor(getContext(), R.color.compatibility_green));
                } else {
                    if (compatibilityN.getAlternatives() == null
                            || compatibilityN.getAlternatives().isEmpty()) {
                        //nodes are not compatible
                        edgePaint.setColor(ContextCompat.getColor(getContext(), R.color.compatibility_red));
                    } else {
                        //nodes are not compatible, but there are alternatives
                        edgePaint.setColor(ContextCompat.getColor(getContext(), R.color.compatibility_yellow));
                    }
                }

                //Draw an edge as a line styled by edgePaint
                canvas.drawLine(sX, sY, tX, tY, edgePaint);

                //Draw an arrowhead in the mid of the vector from the centre of source to centre of target
                float vX = tX - sX;
                float vY = tY - sY;
                canvas.drawPath(drawArrowInDirectionOfVector(vX, vY,
                        initialLength / 8, sX + vX * 0.5f, sY + vY * 0.5f)
                        , edgePaint);


            }

            //Draw the nodes
            for (Node n : nodes) {
                //draw the current node as a filled, grey circle
                canvas.drawCircle(n.getX(), n.getY(), initialLength, drawPaint);

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

                //In the case that the drawableID is 0 the resource couldn't be found.
                if (drawableID != 0) {

                    final Drawable drawable = getContext().getDrawable(drawableID);

                    drawable.setBounds(0, 0, (int) (1.5*initialLength), (int) (1.5*initialLength));
                    canvas.translate(n.getX() - 0.75f*initialLength, n.getY() - 0.75f*initialLength);
                    drawable.draw(canvas);
                    canvas.translate(-n.getX() + 0.75f*initialLength, -n.getY() + 0.75f*initialLength);
                }
            }
        }
        canvas.restore();
    }

    /**
     * Creates a Path resembling an arrowhead that is pointed in the direction of a vector
     * specified by (vectorX,vectorY).
     * Its baseline is centred on (startX,startY).
     *
     * @param vectorX
     * @param vectorY
     * @param arrowHeadBaseLine
     * @param startX
     * @param startY
     * @return
     */
    private Path drawArrowInDirectionOfVector(float vectorX, float vectorY, float arrowHeadBaseLine,
                                              float startX, float startY) {

        float vLength = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        float vXNormed = vectorX / vLength;
        float vYNormed = vectorY / vLength;

        float baseLineHalf = arrowHeadBaseLine / 2;

        Path arrowHead = new Path();

        arrowHead.moveTo(startX, startY);
        arrowHead.lineTo(startX - vectorY / vLength * baseLineHalf,
                startY + vectorX / vLength * baseLineHalf);

        arrowHead.lineTo(startX + vXNormed * arrowHeadBaseLine,
                startY + vYNormed * arrowHeadBaseLine);

        arrowHead.lineTo(startX + vectorY / vLength * baseLineHalf,
                startY - vectorX / vLength * baseLineHalf);
        arrowHead.lineTo(startX, startY);

        arrowHead.close();

        return arrowHead;

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

    /**
     * OnMeasure() is always called when the size of the CompositionView is forced to change.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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


        int minh = MeasureSpec.getSize(w) + paddingBot + paddingTop;
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);


        if (comp != null && comp.getNodeList() != null && !comp.getNodeList().isEmpty()) {
            //Search for the maximal coordinates in the list of nodes.
            maxX = Integer.MIN_VALUE;
            maxY = Integer.MIN_VALUE;
            minX = Integer.MAX_VALUE;
            minY = Integer.MAX_VALUE;


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

            //Set a minimum and maximum for scaling
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