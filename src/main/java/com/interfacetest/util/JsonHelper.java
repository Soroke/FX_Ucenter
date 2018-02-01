package com.interfacetest.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by han on 2017/3/3.
 */
public class JsonHelper {

    public static Object getValue(Object json, String key){
        Object obj = getValue2(json, key);
        if(obj instanceof notFond){
            throw new JSONException("没有从"+json+"中找到" + key.substring(0, key.indexOf(((notFond) obj).getValie())));
        }else {
            return obj;
        }
    }

    private static Object getValue2(Object json, String key) {
        key = key.replace("]", "");
        key = key.replace("[", ".");
        String keys[] = key.split("\\.");

        try{
            if(json instanceof JSONArray){
                if(keys.length==1){
                    return ((JSONArray) json).get(Integer.parseInt(keys[0]));
                }else {
                    return getValue2(((JSONArray) json).get(Integer.parseInt(keys[0])), key.substring(key.indexOf(".")+1));
                }
            }
            if(json instanceof JSONObject){
                if(keys.length==1){
                    return ((JSONObject) json).get(keys[0]);
                }else {
                    return getValue2(((JSONObject) json).get(keys[0]), key.substring(key.indexOf(".")+1));
                }
            }
            if(json instanceof String){
                return getValue2(JSON.parse((String) json), key);
            }
        }catch (JSONException e){
            return new notFond(keys[0]);
        }

        return null;
    }

    public Object getBByA(Object json, String key, String ak){
        String b = key.split("=")[1];
        key = key.split("=")[0];
        JSONArray  ja = (JSONArray) getValue2(json, key.substring(0, key.lastIndexOf(".")));
        for(int i=0; i< ja.size(); i++){
            JSONObject jo = ja.getJSONObject(i);
            if(jo.get(key.substring(key.lastIndexOf(".")+1)).equals(b)){
                return jo.get(ak);
            }
        }
        return null;
    }

    public static String getArrNextKey(String json, String key){
        JSONArray ja = JSON.parseArray(json);
        for(int i=0; i<ja.size()-1; i+=2){
            if(ja.get(i).toString().equals(key)){
                return ja.get(i+1).toString();
            }

        }
        return null;
    }

    static class notFond{
        String valie;
        public notFond(String valie){
            this.valie = valie;
        }
        public String getValie(){
            return this.valie;
        }
    }

}
