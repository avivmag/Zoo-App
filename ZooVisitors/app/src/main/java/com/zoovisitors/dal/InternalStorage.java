package com.zoovisitors.dal;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aviv on 23-Mar-18.
 */

public class InternalStorage {
    private Context context;
    public InternalStorage(Context context)
    {
        this.context = context;
    }
    // Custom method to save a bitmap into internal storage
    public void saveImageToInternalStorage(String fileName, Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(context);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, fileName);

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();
        } catch (IOException e) {
            Log.e("AVIV", "InternalStorage2 " + e);
        }
//        // Parse the gallery image url to uri
//        Uri savedImageURI = Uri.parse(file.getAbsolutePath());
//
//        // Return the saved image Uri
//        return savedImageURI;
    }
    public Drawable loadImageFromInternalStorage(String fileName){
        // load image
        try {
            // get input stream
            InputStream ims = context.getAssets().open(fileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            return d;
        }
        catch(IOException ex) {
            Log.e("AVIV", "InternalStorage " + ex);
        }
        return null;
    }

    public boolean isExists(String url) {
        return context.getResources().getIdentifier(url, "drawable", context.getPackageName()) != 0;
    }
}
