package com.zoovisitors.cl.network;

/**
 * Created by aviv on 08-Jan-18.
 */

public interface ResponseInterface {
    /**
     * will be called when calling was a success
     * @param response
     */
    void onSuccess(String response);

    /**
     * will be called when calling was a failure
     * @param response
     */
    void onFailure(String response);
}
