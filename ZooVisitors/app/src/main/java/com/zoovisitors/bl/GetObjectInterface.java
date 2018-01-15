package com.zoovisitors.bl;

/**
 * Created by Gili on 14/01/2018.
 */

public interface GetObjectInterface {
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
}
