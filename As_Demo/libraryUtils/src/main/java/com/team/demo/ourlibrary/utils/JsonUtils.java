package com.team.demo.ourlibrary.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * json转换
 * Created by TranGuility on 16/11/4.
 */

public class JsonUtils {

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    /**
     * 对Map进行key的升序排序
     *
     * @param map
     * @return
     */
    public static LinkedHashMap<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map.isEmpty()) {
            return null;
        }
        Object[] keys = map.keySet().toArray();
        Arrays.sort(keys);
        LinkedHashMap<String, Object> lm = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            lm.put(keys[i].toString(), map.get(keys[i]));
        }
        return lm;
    }


//    public BodResponse getPayload() {
//        Map<String, String> map = new HashMap<>();
//        try {
//            JSONObject jObject = new JSONObject(payload);
//            Iterator<?> keys = jObject.keys();
//
//            while (keys.hasNext()) {
//                String key = (String) keys.next();
//                if (key.equalsIgnoreCase("ext")) {
//                    JSONObject ext = new JSONObject(jObject.getString(key));
//                    Iterator<?> extks = ext.keys();
//                    while (extks.hasNext()) {
//                        String extk = (String) extks.next();
//                        String strext = ext.getString(extk) == null ? "" : ext.getString(extk);
//                        map.put(extk, strext);
//                    }
//                } else {
//                    JSONArray array = new JSONArray(jObject.getString(key));
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject obj = array.getJSONObject(i);
//                        Iterator<?> it = obj.keys();
//                        while (it.hasNext()) {
//                            String n = (String) it.next();
//                            String value = obj.getString(n) == null ? "" : obj.getString(n);
//                            map.put(n, value);
//                        }
//                    }
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        BodResponse bean = new BodResponse();
//        bean.setAvatar(map.containsKey("avatar") ? map.get("avatar").replace("https", "http") : "");
//        bean.setNikeName(map.containsKey("nikeName") ? map.get("nikeName") : "");
//        bean.setMsg(map.containsKey("msg") ? map.get("msg") : "");
//        bean.setType(map.containsKey("type") ? map.get("type") : "");
//        bean.setmEaseSendId(sender);
//        bean.setmEaseGetId(receiver);
//        return bean;
//    }
}
