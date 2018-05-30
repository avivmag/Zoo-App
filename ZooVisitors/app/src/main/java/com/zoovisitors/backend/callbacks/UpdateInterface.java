package com.zoovisitors.backend.callbacks;

/**
 * Created by Gili on 26/05/2018.
 */

public interface UpdateInterface {
    /**
     * will be called when calling was a success
     * @param response
     */
    void onSuccess(Object response);

    /**
     * will be called when calling was a failure
     * @param response
     */
    void onFailure(Object response);

    /**
     * will be called when calling was an update
     * @param response
     */
    void onUpdate(Object response);
}
