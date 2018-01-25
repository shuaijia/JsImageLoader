package com.jia.jsloader.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hm on 2016/1/12.
 *
 *  这里是关于网络Json的解析JavaBean的工具类
 *
 */
public abstract class BaseParser<T> {

    public T getBeanFromJson(String str){

        /**
         *  下面添加的网络判断操作是关于服务器出现突发错误的时候
         */
        if (null == str) {
            return null;
        } else {
            String result=null;
            try {
                JSONObject jsonObject = new JSONObject(str);
                result = jsonObject.getString("response");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (result != null && !result.equals("error")) {
                return parseJSON(str);
            } else {
                return null;

            }

        }

    }


    public abstract T parseJSON(String str);

}