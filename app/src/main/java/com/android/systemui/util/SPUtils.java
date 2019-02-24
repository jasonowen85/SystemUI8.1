package com.android.systemui.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {

	/**
	 * 可以存储String,int,boolean
	 * @param context
	 * @param key
	 * @param value
	 */
	public static boolean put(Context context, String key, Object value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		if (value instanceof String) {
			edit.putString(key, (String) value);
		} else if (value instanceof Integer) {
			edit.putInt(key, (int) value);
		} else if (value instanceof Boolean) {
			edit.putBoolean(key, (boolean) value);
		}
		return edit.commit();
	}
	
	/**
	 * 获取字符串
	 * @param context
	 * @param key
	 * @return 默认""
	 */
	public static String getString(Context context, String key,String defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	/**
	 * 获取整数
	 * @param context
	 * @param key
	 * @return 默认0
	 */
	public static int getInt(Context context, String key,int defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	
	/**
	 * 获取布尔
	 * @param context
	 * @param key
	 * @return  默认true
	 */
	public static boolean getBoolean(Context context, String key,boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key,defValue);
	}

}
