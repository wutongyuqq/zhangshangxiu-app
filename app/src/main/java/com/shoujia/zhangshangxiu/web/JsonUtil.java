package com.shoujia.zhangshangxiu.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pengli on 2018/6/14.
 */

public class JsonUtil {


    /************** ============================ add by lip85 on date 2018-04-19 start ============================== ***********************/


    public static String objectTojson(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String) {
            json.append("\"").append(stringTojson(obj.toString())).append("\"");
        } else if (obj instanceof Integer || obj instanceof Float || obj instanceof Boolean || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal || obj instanceof BigInteger || obj instanceof Byte) {
            json.append(stringTojson(obj.toString()));
        } else if (obj instanceof Object[]) {
            json.append(arrayTojson((Object[]) obj));
        } else if (obj instanceof List) {
            json.append(listTojson((List<?>) obj));
        } else if (obj instanceof Map) {
            json.append(mapTojson((Map<?, ?>) obj));
        } else if (obj instanceof Set) {
            json.append(setTojson((Set<?>) obj));
        } else {
            json.append(beanTojson(obj));
        }
        return json.toString();
    }

    @SuppressWarnings("rawtypes")
    public static String beanTojson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        // 得到类对象
        Class props = bean.getClass();
        Field[] fs = props.getDeclaredFields();
        Object value = null;
        String name = "";
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            name = objectTojson(f.getName());
            try {
                value = objectTojson(f.get(bean));
                // 对值进行加密
                // value = Encryption.Encrypt(value, Encryption.cKey);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            json.append(name);
            json.append(":");
            json.append(value);
            json.append(",");
        }
        json.setCharAt(json.length() - 1, '}');
        return json.toString();
    }

    public static String listTojson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(objectTojson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String arrayTojson(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(objectTojson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    // 已设置加密
    public static String mapTojson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        String value = "";
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(objectTojson(key));
                json.append(":");
                value = objectTojson(map.get(key));
                // 对字符串值进行加密
                // value = Encryption.Encrypt(value, Encryption.cKey);
                json.append(value);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String setTojson(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(objectTojson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String stringTojson(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= '\u0000' && ch <= '\u001F') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }


    public static String[] jsonToArray(String json) {
        // 去除utf-8中的bom格式
        if (json != null && json.startsWith("/ufeff")) {
            json = json.substring(1);
        }
        json = json.substring(2, json.length() - 2);
        String[] arr = json.split("\",\"");
        return arr;
    }


    public static String[] stringArrayToList(JSONArray array) {
        try {
            int c = array.length();
            if (c == 0)
                return null;
            String[] vs = new String[array.length()];
            for (int i = 0; i < c; i++) {
                vs[i] = array.getString(i);
            }
            return vs;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object jsonToMapOrList(String json) {
        if (json == null || json.length() < 1)
            return null;

        try {
            if (json.indexOf("[") == 0) {
                ArrayList<Object> list = new ArrayList<Object>();
                JSONArray array = new JSONArray(json);
                for (int i = 0, j = array.length(); i < j; i++) {
                    String value = String.valueOf(array.get(i));
                    list.add(jsonToMapOrList(value));
                }
                return list;
            } else if (json.indexOf("{") == 0) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                JSONObject obj = new JSONObject(json);
                Iterator<?> iter = obj.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = String.valueOf(obj.get(key));
                    map.put(key, jsonToMapOrList(value));
                }
                return map;
            } else {
                return json;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将json字符串转换成List<Map>(List中是Map类型)
     *
     * @param json
     * @return
     */
    public static List jsToList(String json) {
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsArrayToMap(jsonarray);
    }

    /**
     * 数组解析
     *
     * @param jsonarray
     * @return
     */
    public static List jsArrayToMap(JSONArray jsonarray) {
        List<Object> listMap = new ArrayList<Object>();

        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                Object jo = jsonarray.get(i);
                if (jo instanceof JSONArray) {
                    jo = jsArrayToMap((JSONArray) jo);
                } else if (jo instanceof JSONObject) {
                    jo = jsToMap((JSONObject) jo);
                }
                listMap.add(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return listMap;
    }

    /**
     * 递归处理多层JSON
     *
     * @param jsonString
     * @return
     */
    public static Map jsToMap(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsToMap(jsonObject);
    }

    /**
     * 递归处理多层JSON
     *
     * @param json
     * @return
     */
    public static Map jsToMap(JSONObject json) {
        Map valueMap = new HashMap();
        Iterator keyIter = json.keys();
        while (keyIter.hasNext()) {
            String key = (String) keyIter.next();
            Object value = null;
            try {
                value = json.get(key);
                if (value instanceof JSONArray) {
                    //1. 它是嵌套的JSON数组对象

                    //递归解析数组
                    value = jsArrayToMap((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    //2. 它是嵌套的JSON对象

                    //递归解析JSON
                    value = jsToMap((JSONObject) value);
                } else {
                    //3. 普通的string\Integer值

                    if (value == null)//对空值特殊处理返回
                        value = "";
                    value = value.toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            valueMap.put(key, value);
        }
        return valueMap;
    }
    /************** ============================ add by lip85 on date 2017-04-19  end ============================== ***********************/
}
