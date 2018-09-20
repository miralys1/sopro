package swarm.swarmcomposerapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import swarm.swarmcomposerapp.ActivitiesAndViews.CompositionView;
import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Edge;
import swarm.swarmcomposerapp.Model.Node;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.R;

public class PDFCreator {
    private static final int WIDTH = 595 ,HEIGHT = 842;
    private static final int PADDING = 50;
    private static final int ACTUAL_WIDTH = WIDTH-2*PADDING ,ACLTUAL_HEIGHT = HEIGHT-2*PADDING;
    private static Paint paint_title, paint_subtitle, paint_text, paint_divider, paint_compatible, paint_alternative, paint_incompatible;
    private static int textSize_title = 30, textSize_subtitle = 20, textSize = 14;
    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");




    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    /**
     * Create a PDF of the current composition; asks for permissions first
     * @param activity
     * @param context
     * @param comp composition
     * @return path of the saved pdf
     */
    public static String createPDF(Activity activity, Context context, Composition comp){

        if(verifyStoragePermissions(activity)) {
            Toast.makeText(context, context.getText(R.string.loading), Toast.LENGTH_SHORT).show();
            return writePDFtoFile(context, comp);
        }
        return null;
    }

    private static String writePDFtoFile(Context context, Composition comp){
        // create a new document
        PdfDocument document = new PdfDocument();


        paint_title = new Paint();
        paint_title.setColor(context.getColor(R.color.colorPrimaryDark));
        paint_title.setTextSize(textSize_title);

        paint_subtitle = new Paint();
        paint_subtitle.setColor(context.getColor(R.color.colorPrimary));
        paint_subtitle.setTextSize(textSize_subtitle);

        paint_text = new Paint();
        paint_text.setColor(context.getColor(R.color.textColorPrimary));
        paint_text.setTextSize(textSize);

        paint_divider = new Paint();
        paint_divider.setColor(context.getColor(R.color.textColorPrimary));
        paint_divider.setStrokeWidth(2);

        paint_compatible = new Paint();
        paint_compatible.setColor(ContextCompat.getColor(context, R.color.compatibility_green));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.font_awesome);
        paint_compatible.setTypeface(typeface);

        paint_alternative = new Paint();
        paint_alternative.setColor(ContextCompat.getColor(context, R.color.compatibility_yellow));
        paint_alternative.setTypeface(typeface);

        paint_incompatible = new Paint();
        paint_incompatible.setColor(ContextCompat.getColor(context, R.color.compatibility_red));
        paint_incompatible.setTypeface(typeface);


        ////////////////////Page 1 ///////////////////////
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(WIDTH, HEIGHT, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        Canvas canvas = page.getCanvas();

        float offset_top = 2*PADDING;


        canvas.drawText(comp.getName(), PADDING, offset_top, paint_title);
        offset_top += textSize_title;
        canvas.drawText(comp.getOwner().getFullName(), PADDING, offset_top, paint_subtitle);
        offset_top += textSize_subtitle;
        canvas.drawText(context.getText(R.string.lastupdate)+": "+dateFormat.format(comp.getLastUpdate()), PADDING, offset_top, paint_text);
        offset_top += textSize;


        //create a temporary CompositionView, convert to Bitmap and add to PDF
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_graph, null);
        view.layout(0, 0, WIDTH, (int)(HEIGHT-offset_top));

        ((CompositionView) view.findViewById(R.id.pdf_compview)).setComp(comp);
        //Get the dimensions of the view so we can re-layout the view at its current size and create a bitmap of the same size
        int width = view.getWidth();
        int height = view.getHeight();

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        //Cause the view to re-layout
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create a bitmap backed Canvas to draw the view into
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.draw(c);

        canvas.drawBitmap(b, 0, offset_top, new Paint());

        // finish the page
        document.finishPage(page);

        /////////////////// Page 2 ///////////////////

        // crate a page description
        PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(WIDTH, HEIGHT, 2).create();

        // start a page
        PdfDocument.Page page2 = document.startPage(pageInfo2);

        // draw something on the page
        canvas = page2.getCanvas();
        offset_top = 2*PADDING;
        canvas.drawText(comp.getName(), PADDING, offset_top, paint_title);
        offset_top += textSize_title;
        String graphAsText = "";
        ArrayList<Edge> edges = new ArrayList<>();
        edges.addAll(comp.getEdgeList());



        //////Edges ////
        canvas.drawText(context.getText(R.string.edges).toString(), PADDING, offset_top, paint_subtitle);
        offset_top += textSize_subtitle;
        Edge temp;
        for(int i = 0; i < edges.size(); i++){
            temp = edges.get(i);
            canvas.drawText(temp.getOut().getSendService().getServiceName() + " -> " + temp.getIn().getSendService().getServiceName(), 1.5f*PADDING, offset_top, paint_text);
            if(temp.getCompatibility().isCompatible()){
                canvas.drawText(context.getText(R.string.ic_compatible).toString(), PADDING, offset_top, paint_compatible);
                String formats = "";
                for(String str : temp.getCompatibility().getSuitingFormats()){
                    formats += str + ", ";
                }
                canvas.drawText(context.getText(R.string.suitable).toString()+": "+formats.substring(0, formats.length()-2), PADDING+WIDTH/2, offset_top, paint_text);
            } else {
                //incompatible or alternatives
                if(temp.getCompatibility().getAlternatives() == null || temp.getCompatibility().getAlternatives().isEmpty()){
                    //incompatible
                    canvas.drawText(context.getText(R.string.ic_incompatible).toString(), PADDING, offset_top, paint_incompatible);
                    canvas.drawText(context.getText(R.string.alternative).toString()+": "+context.getText(R.string.none).toString(), PADDING+WIDTH/2, offset_top, paint_text);
                } else {
                    //alternatives
                    canvas.drawText(context.getText(R.string.ic_alternative).toString(), PADDING, offset_top, paint_alternative);
                    String alt = "";
                    //for(Alternative a : alternatives){
                    //there will be only one alternative
                        for(String str : temp.getCompatibility().getAlternatives().get(0).getNames()) {
                            alt += " -> " + str;
                        }
                    canvas.drawText(context.getText(R.string.alternative).toString()+": "+alt+" ->", PADDING+WIDTH/2, offset_top, paint_text);
                }
            }
            offset_top += textSize;
        }

        offset_top += PADDING;
        ///////Services ///////
        canvas.drawText(context.getText(R.string.services).toString(), PADDING, offset_top, paint_subtitle);
        offset_top += textSize_subtitle;
        String[] serviceStandardInfo = {
                context.getText(R.string.service_name) + ": ",
                context.getText(R.string.service_organisation) + ": ",
                context.getText(R.string.service_version) + ": ",
                context.getText(R.string.service_date) + ": ",
                context.getText(R.string.service_certified) + ": "};


        float offset_left = PADDING;
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.addAll(comp.getNodeList());
        for(int j = 0; j < nodes.size(); j++){
            offset_left = (j%2 == 0) ? PADDING : ACTUAL_WIDTH/2+ 1.5f*PADDING;
            for(int i = 0; i < serviceStandardInfo.length; i++){
                canvas.drawText(serviceStandardInfo[i], offset_left, offset_top+textSize*i, paint_text);
            }
            Service service = nodes.get(j).getSendService();
            String[] serviceInfo = {service.getServiceName(), service.getOrganisation(),
                    service.getVersion(), dateFormat.format(service.getDate()*1000),
                    ((service.getCertified() != null && service.getCertified().equals("true")) ?
                            context.getText(R.string.service_certified_yes) : context.getText(R.string.service_certified_no)).toString()};
            for(int k = 0; k < serviceInfo.length; k++){
                canvas.drawText(serviceInfo[k], offset_left+ACTUAL_WIDTH/4, offset_top+textSize*k, paint_text);
            }
            if(j%2 == 1) {
                canvas.drawLine(WIDTH/2, offset_top-textSize, WIDTH/2, offset_top+5*textSize, paint_divider);
                offset_top += 6 * textSize;
            }
        }

        canvas.drawText(graphAsText, PADDING, offset_top, paint_subtitle);
        offset_top += textSize_subtitle;


        // finish the page
        document.finishPage(page2);

        ////////////////// End Of Document /////////////

        // write the document content
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath()+"/"+context.getText(R.string.compositions));
        if(!dir.exists()){
            dir.mkdirs(); //or mkdir();
        }
        File file = new File(dir, comp.getName()+".pdf");

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            document.writeTo(fileOutputStream);

            // close the document
            document.close();
            return file.toURI().toString();

        } catch (IOException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    private static boolean verifyStoragePermissions(Activity activity) {

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }
        return true;
    }
}
