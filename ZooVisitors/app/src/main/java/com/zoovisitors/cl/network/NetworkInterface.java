package com.zoovisitors.cl.network;

import android.graphics.Bitmap;

/**
 * Created by aviv on 08-Jan-18.
 */

public interface NetworkInterface {
    /**
     * This method is for calling to server
     * @param innerURL - the inner path in the url, i.e. "animals/1"
     * @param responseInterface
     */
    void post(String innerURL, final ResponseInterface<String> responseInterface);
    void postImage(String innerURL, int width, int height, final ResponseInterface<Bitmap> responseInterface);
    void postImageWithoutPrefix(String url, int width, int height, final ResponseInterface<Bitmap> responseInterface);
}
