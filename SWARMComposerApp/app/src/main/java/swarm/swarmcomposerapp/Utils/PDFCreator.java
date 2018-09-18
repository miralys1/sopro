package swarm.swarmcomposerapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.R;

public class PDFCreator {
    private static final int WIDTH = 595 ,HEIGHT = 842;
    private static final int PADDING = 50;


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
            return writePDFtoFile(context, comp);
        }
        return null;
    }

    private static String writePDFtoFile(Context context, Composition comp){
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(WIDTH, HEIGHT, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        Canvas canvas = page.getCanvas();
        float offset_top = PADDING;
        Paint paint_title = new Paint();
        paint_title.setColor(context.getColor(R.color.colorPrimaryDark));
        paint_title.setTextSize(30);
        canvas.drawText(comp.getName(), PADDING, offset_top, paint_title);
        offset_top += 30;
        Paint paint_subtitle = new Paint();
        paint_subtitle.setColor(context.getColor(R.color.colorPrimary));
        paint_subtitle.setTextSize(20);
        canvas.drawText(comp.getOwner().getFullName(), PADDING, offset_top, paint_subtitle);


        // finish the page
        document.finishPage(page);
        // add more pages

        // write the document content
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, comp.getName()+".pdf");

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            document.writeTo(fileOutputStream);
            //Toast.makeText(context, context.getText(R.string.pdf_saved)+ " "+file.toURI().toString(), Toast.LENGTH_SHORT).show();

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
