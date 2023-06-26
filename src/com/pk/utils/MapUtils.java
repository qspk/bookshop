package com.pk.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtils {
    /**
     * 按值的大小对Map<String,Integer> 集合进行升序排列
     * @param map 要排序的集合
     * @return 排序完的集合
     */
    public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

    }

}
