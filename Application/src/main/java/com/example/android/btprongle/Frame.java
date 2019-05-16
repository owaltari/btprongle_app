package com.example.android.btprongle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.android.common.logger.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Random;

public class Frame {

    private static final String TAG = "Frame";

    private int ID;
    private int nonce;
    private String src;
    private String dst = "bcast"; // Default destination is broadcast for now.
    private JSONObject payload = null;

    private Context applicationContext = MainActivity.getContextOfApplication();
    private SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

    public Frame(int type) {
        ID = type;
        nonce = generateNonce();
        src = myHandle();
    }

    /*public Frame() {
        ID = FrameDefinition.DEBUG;
        nonce = generateNonce();
        src = myHandle();
    }*/

    public Frame(String buffer) {
        // Constructor for incoming frames
        try {
            JSONArray obj = new JSONArray(buffer);
            ID = obj.getInt(FrameDefinition.ID);
            nonce = obj.getInt(FrameDefinition.NONCE);
            src = obj.getString(FrameDefinition.SRC);
            dst = obj.getString(FrameDefinition.DST);

            if (obj.length() > 4) {
                byte[] data = Base64.decode(obj.getString(FrameDefinition.PAYLOAD), Base64.NO_WRAP);
                String text = new String(data, "UTF-8");

                payload = new JSONObject(text);
            }

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + buffer + "\"");
        }

    }

    // Setters
    public void setID(int id) {
        this.ID = id;
    }

    public void setNonce(int nonce) {
        // This is used only in the case of pingtest, where nonce is used as a decreasing sequence number
        this.nonce = nonce;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public void setPayload(String key, Object value) {
        if (this.payload == null) {
            this.payload = new JSONObject();
        }

        try {
            this.payload.put(key, value);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            this.payload = null;
        }
    }

    // Getters
    public int getID() {
        return ID;
    }

    public int getNonce() {
        return nonce;
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }

    public Object getPayload(String key) {
        try {
            return this.payload.get(key);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    public String getPayloadJSON() {
        String result = "";
        Iterator<String> iter = payload.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = payload.getString(key);
                result += "\n"+key+" = "+value;
            } catch (JSONException e) {
                result = e.toString();
                break;
            }
        }

        return result;

    }

    public String toString() {
        String result = "\tID: " + this.ID;
        result += "\n\tnonce: " + this.nonce;
        result += "\n\tsrc: " + this.src;
        result += "\n\tdst: " + this.dst;

        if (this.payload != null) {
            result += "\n\tpayload:";
            Iterator<String> iter = payload.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String value = payload.getString(key);
                    result += "\n\t\t" + key + " = " + value;
                } catch (JSONException e) {
                    result = e.toString();
                    break;
                }
            }
        }

        return result;
    }
    public String toSimpleString() {
        String result = this.ID + "\t" + this.nonce + "\t" + this.src + "\t" + this.dst + "\t";
        result += this.payload.toString();
        return result;
    }

    public byte[] toBytes() {
        JSONArray frame = new JSONArray();

        frame.put(this.ID);
        frame.put(this.nonce);
        frame.put(this.src);
        frame.put(this.dst);

        if (this.payload != null) {
            try {
                byte[] data = payload.toString().getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
                frame.put(base64);
            } catch (UnsupportedEncodingException e) {
                frame.put("Unsupported ecoding");
            }
        }

        return (frame.toString()).getBytes();
    }

    private int generateNonce() {
        return new Random().nextInt(((int) Math.pow(2, 14)));
    }

    private String myHandle() {
        return prefs.getString("instanceID", "N/A");
    }

}
