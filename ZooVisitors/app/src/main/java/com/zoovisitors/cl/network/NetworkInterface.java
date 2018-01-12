package com.zoovisitors.cl.network;

/**
 * Created by aviv on 08-Jan-18.
 */

public interface NetworkInterface {
    /**
     * This method is for calling to server
     * @param innerURL - the inner path in the url, i.e. "animals/1"
     * @param responseInterface
     */
    void post(String innerURL, final ResponseInterface responseInterface);
}
