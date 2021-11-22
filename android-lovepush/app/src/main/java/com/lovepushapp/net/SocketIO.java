package com.lovepushapp.net;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
//import io.socket.client.IO;



/**
 * Created by varinder on 24/12/18.
 */

public class SocketIO {

    private io.socket.client.Socket mSocket;

    public static SocketIO getInstance() {
        return new SocketIO();
    }

    public Socket getSocket() {
        try {
            mSocket = IO.socket("http://dev2.xicom.us:3000");
        } catch (URISyntaxException e) {}
        return mSocket;
    }
}
