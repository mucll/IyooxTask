package com.huisu.iyoox.okhttp;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseEntityToModule {
    public ResponseEntityToModule() {
    }

    public static Object parseJsonToModule(String jsonContent, Class<?> clazz) {
        Object moduleObj = null;

        try {
            JSONObject e = new JSONObject(jsonContent);
            moduleObj = parseJsonObjectToModule(e, clazz);
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return moduleObj;
    }

    public static Object parseJsonObjectToModule(JSONObject jsonObj, Class<?> clazz) {
        Object moduleObj = null;
        try {
            moduleObj = clazz.newInstance();
            setFieldValue(moduleObj, jsonObj, clazz);
        } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        } catch (JSONException var6) {
            var6.printStackTrace();
        } catch (InstantiationException var7) {
            var7.printStackTrace();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return moduleObj;
    }

    private static void setFieldValue(Object moduleObj, JSONObject jsonObj, Class<?> clazz) throws IllegalArgumentException, IllegalAccessException, JSONException, InstantiationException {
        if (clazz.getSuperclass() != null) {
            setFieldValue(moduleObj, jsonObj, clazz.getSuperclass());
        }
        Field[] fields = clazz.getDeclaredFields();
        Field[] var9 = fields;
        int var8 = fields.length;
        for (int var7 = 0; var7 < var8; ++var7) {
            Field f = var9[var7];
            f.setAccessible(true);
            Class cls = f.getType();
            String name = f.getName();
            if (jsonObj.has(name) && !jsonObj.isNull(name)) {
                if (!cls.isPrimitive() && !isWrappedPrimitive(cls)) {
                    if (cls.isAssignableFrom(String.class)) {
                        f.set(moduleObj, String.valueOf(jsonObj.get(name)));
                    } else if (cls.isAssignableFrom(ArrayList.class)) {
                        parseJsonArrayToList(f, name, moduleObj, jsonObj);
                    } else {
                        Object obj = parseJsonObjectToModule(jsonObj.getJSONObject(name), cls.newInstance().getClass());
                        f.set(moduleObj, obj);
                    }
                } else {
                    setPrimitiveFieldValue(f, moduleObj, jsonObj.get(name));
                }
            }
        }

    }

    private static ArrayList<Object> parseJsonArrayToList(Field field, String fieldName, Object moduleObj, JSONObject jsonObj) throws JSONException, IllegalArgumentException, IllegalAccessException {
        ArrayList objList = new ArrayList();
        Type fc = field.getGenericType();
        if (fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;
            if (pt.getActualTypeArguments()[0] instanceof Class) {
                Class clss = (Class) pt.getActualTypeArguments()[0];
                if (jsonObj.get(fieldName) instanceof JSONArray) {
                    JSONArray array = jsonObj.getJSONArray(fieldName);

                    for (int i = 0; i < array.length(); ++i) {
                        if (array.get(i) instanceof JSONObject) {
                            objList.add(parseJsonObjectToModule(array.getJSONObject(i), clss));
                        } else if (clss.isAssignableFrom(array.get(i).getClass())) {
                            objList.add(array.get(i));
                        }
                    }
                }

                field.set(moduleObj, objList);
            }
        }

        return objList;
    }

    private static void setPrimitiveFieldValue(Field field, Object moduleObj, Object jsonObj) throws IllegalArgumentException, IllegalAccessException {
        if (field.getType().isAssignableFrom(jsonObj.getClass())) {
            field.set(moduleObj, jsonObj);
        } else {
            field.set(moduleObj, makeTypeSafeValue(field.getType(), jsonObj.toString()));
        }

    }

    private static final Object makeTypeSafeValue(Class<?> type, String value) throws NumberFormatException, IllegalArgumentException {
        return Integer.TYPE != type && Integer.class != type ? (Long.TYPE != type && Long.class != type ? (Short.TYPE != type && Short.class != type ? (Character.TYPE != type && Character.class != type ? (Byte.TYPE != type && Byte.class != type ? (Float.TYPE != type && Float.class != type ? (Double.TYPE != type && Double.class != type ? (Boolean.TYPE != type && Boolean.class != type ? value : Boolean.valueOf(value)) : Double.valueOf(Double.parseDouble(value))) : Float.valueOf(Float.parseFloat(value))) : Byte.valueOf(value)) : Character.valueOf(value.charAt(0))) : Short.valueOf(Short.parseShort(value))) : Long.valueOf(Long.parseLong(value))) : Integer.valueOf(Integer.parseInt(value));
    }

    private static boolean isWrappedPrimitive(Class<?> type) {
        return type.getName().equals(Boolean.class.getName()) || type.getName().equals(Byte.class.getName()) || type.getName().equals(Character.class.getName()) || type.getName().equals(Short.class.getName()) || type.getName().equals(Integer.class.getName()) || type.getName().equals(Long.class.getName()) || type.getName().equals(Float.class.getName()) || type.getName().equals(Double.class.getName());
    }
}
