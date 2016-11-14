package com.team.demo.ourlibrary.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 集合（map ）
 * Created by TranGuility on 16/11/4.
 */

public class SetUtils {
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
}
