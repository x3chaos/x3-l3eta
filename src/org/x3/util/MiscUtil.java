package org.x3.util;

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
}
