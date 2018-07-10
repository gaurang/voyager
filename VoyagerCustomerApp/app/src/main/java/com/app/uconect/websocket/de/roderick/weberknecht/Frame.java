package com.app.uconect.websocket.de.roderick.weberknecht;

/**
 * Created by osigroups on 3/6/2016.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frame {
//  private final static String CONTENT_LENGTH = "content-length";

    private String command;
    private Map<String, String> headers;
    private String body;

    /**
     * Constructor of a Frame object. All parameters of a frame can be instantiate
     *
     * @param command
     * @param headers
     * @param body
     */
    public Frame(String command, Map<String, String> headers, String body){
        this.command = command;
        this.headers = headers != null ? headers : new HashMap<String, String>();
        this.body = body != null ? body : "";
    }

    public String getCommand(){
        return command;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public String getBody(){
        return body;
    }

    /**
     * Transform a frame object into a String. This method is copied on the objective C one, in the MMPReactiveStompClient
     * library
     * @return a frame object convert in a String
     */
    private String toStringg(){
        String strLines = this.command;
        strLines += Byte.LF;
        for(String key : this.headers.keySet()){
            strLines += key + ":" + this.headers.get(key);
            strLines += Byte.LF;
        }
        strLines += Byte.LF;
        strLines += this.body;
        strLines += Byte.NULL;

        return strLines;
    }

    /**
     * Create a frame from a received message. This method is copied on the objective C one, in the MMPReactiveStompClient
     * library
     *
     * @param data
     *  a part of the message received from network, which represented a frame
     * @return
     *  An object frame
     */
    public static Frame fromString(String data){
        List<String> contents = new ArrayList<String>(Arrays.asList(data.split(Byte.LF)));

        while(contents.size() > 0 && contents.get(0).equals("")){
            contents.remove(0);
        }

        String command = contents.get(0);
        Map<String, String> headers = new HashMap<String, String>();
        String body = "";

        contents.remove(0);
        boolean hasHeaders = false;
        for(String line : contents){
            if(hasHeaders){
                for(int i=0; i < line.length(); i++){
                    Character c = line.charAt(i);
                    if(!c.equals('\0'))
                        body += c;
                }
            } else{
                if(line.equals("")){
                    hasHeaders = true;
                } else {
                    String[] header = line.split(":");
                    headers.put(header[0], header[1]);
                }
            }
        }
        return new Frame(command, headers, body);
    }

//    No need this method, a single frame will be always be send because body of the message will never be excessive
//    /**
//     * Transform a message received from server in a Set of objects, named frame, manageable by java
//     *
//     * @param datas
//     *        message received from network
//     * @return
//     *        a Set of Frame
//     */
//    public static Set<Frame> unmarshall(String datas){
//      String data;
//      String[] ref = datas.split(Byte.NULL + Byte.LF + "*");//NEED TO VERIFY THIS PARAMETER
//      Set<Frame> results = new HashSet<Frame>();
//
//      for (int i = 0, len = ref.length; i < len; i++) {
//            data = ref[i];
//
//            if ((data != null ? data.length() : 0) > 0){
//              results.add(unmarshallSingle(data));//"unmarshallSingle" is the old name method for "fromString"
//            }
//        }
//      return results;
//    }

    /**
     * Create a frame with based fame component and convert them into a string
     *
     * @param command
     * @param headers
     * @param body
     * @return  a frame object convert in a String, thanks to <code>toStringg()</code> method
     */
    public static String marshall(String command, Map<String, String> headers, String body){
        Frame frame = new Frame(command, headers, body);
        return frame.toStringg();
    }

    private class Byte {
        public static final String LF = "\n";
        public static final String NULL = "\0";
    }
}