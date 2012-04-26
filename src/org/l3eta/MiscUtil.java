package org.l3eta;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.mongodb.BasicDBObject;

public class MiscUtil {
	public static String complete(String[] array, int index) {
		String str = "";
		for (int i = index; i < array.length; i++) {
			str += array[i] + " ";
		}
		return str.trim();
	}

	public static String[] sliceArray(String[] array, int index) {
		String[] o = new String[array.length - index];
		for (int i = index; i < array.length; i++) {
			o[i - index] = array[i];
		}
		return o;
	}

	public static String complete(String string, int index) {
		return complete(string.split(" "), index);
	}

	public static void throwError(Exception exception) {
		try {
			throw exception;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void throwError(String error) {
		throwError(new Exception(error));
	}

	public static BasicDBObject[] convertArray(Object[] o, int index) {
		ArrayList<Object> array = new ArrayList<Object>();
		for (int i = 1; i < o.length; i++) {
			array.add(o[i]);
		}
		return array.toArray(new BasicDBObject[0]);
	}

	public static Object[] completeArray(Object[] arr, int index) {
		ArrayList<Object> oArr = new ArrayList<Object>();
		for (int i = index; i < arr.length; i++) {
			oArr.add(arr[i]);
		}
		return oArr.toArray();
	}

	public static String color(String string) {
		// TODO update to a regex matcher
		string = string.replace("\\i", ChatColor.ITALIC + "");
		string = string.replace("\\r", ChatColor.RED + "");
		string = string.replace("\\g", ChatColor.GREEN + "");
		return string;
	}

	public static ArrayList<Object> merge(Object[] oa, ArrayList<Object> ao) {
		for (Object o : oa) {
			if (!ao.contains(o)) {
				ao.add(o);
			}
		}
		return ao;
	}
}
