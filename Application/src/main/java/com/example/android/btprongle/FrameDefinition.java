package com.example.android.btprongle;

public interface FrameDefinition {
    // These are indices to fields in the message header.
    int ID = 0;
    int NONCE = 1;
    int SRC = 2;
    int DST = 3;
    int PAYLOAD = 4;

    // Debug specific message types 1xx
    int DEBUG = 100;
    int DEBUG_PING = 101;
    int DEBUG_PONG = 102;

    // Local link (Android <--> Prongle) specific message types 2xx
    int LOCAL = 200;
    int LOCAL_ECHO = 201;
    //public static final int LOCAL_PONG = 202;

    // Mesh link (Android <--> Prongle <--> Mesh) specific message types 3xx
    int MESH = 300;
    int MESH_PING = 301;
    int MESH_PONG = 302;

    // Usertext specific message types 4xx
    int USERTEXT = 400;
    int USERTEXT_JSON = 401;
    int USERTEXT_BROADCAST = 402;

}
