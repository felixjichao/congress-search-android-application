package edu.usc.jichaozh.ownnavigation.other;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LocalStorage
{
    public static final String FILE_NAME = "favorite";

    public static void put(Context context, String key, String value)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static String get(Context context, String key)
    {
        return context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE).getString(key, "");
    }

    public static void remove(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public static boolean contains(Context context, String key)
    {
        return context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE).contains(key);
    }

    public static Map<String, ?> getAll(Context context)
    {
        return context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE).getAll();
    }

    private static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod()
        {
            try             {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            }
            catch (NoSuchMethodException e) {
                Log.e("find apply method", "error");
            }
            return null;
        }

        public static void apply(SharedPreferences.Editor editor)
        {
            try             {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            }
            catch (IllegalArgumentException e) {
                Log.e("apply", "error");
            }
            catch (IllegalAccessException e) {
                Log.e("apply", "error");
            }
            catch (InvocationTargetException e) {
                Log.e("apply", "error");
            }
            editor.commit();
        }
    }
}
