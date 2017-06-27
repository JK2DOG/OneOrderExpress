package com.zc.express.utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Json数据类型转换
 * Created by ZC on 2017/3/30.
 */

public class JsonUtils {

    private static Gson sGson = new Gson();
    private static JsonParser sJsonParser = new JsonParser();


    /**
     * 将json数据转化为实体数据
     *
     * @param jsonData    json字符串
     * @param entityClass 类型
     * @return 实体
     */
    public static <T> T convertEntity(String jsonData, Class<T> entityClass) {
        T entity = null;
        try {
            entity = sGson.fromJson(jsonData.toString(), entityClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 将json数据转化为实体列表数据
     *
     * @param jsonData    json字符串
     * @param entityClass 类型
     * @return 实体列表
     */
    public static <T> List<T> convertEntities(String jsonData, Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        try {
            JsonArray jsonArray = sJsonParser.parse(jsonData).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                entities.add(sGson.fromJson(element, entityClass));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entities;
    }

    /**
     * 将json数据转化为实体列表数据
     *
     * @param jsonData json字符串
     * @param type     类型
     * @return 实体
     */
    public static <T> T toEntity(String jsonData, Type type) {
        T entity = null;
        try {
            entity = new Gson().fromJson(jsonData, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entity;
    }


}
