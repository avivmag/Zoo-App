package com.zoovisitors.cl.network;

/**
 * Created by aviv on 08-Jan-18.
 */

public interface ResponseInterface<T> {
    /**
     * will be called when calling was a success
     * @param response
     */
    void onSuccess(T response);

    /**
     * will be called when calling was a failure
     * @param response
     */
    void onFailure(String response);
}
