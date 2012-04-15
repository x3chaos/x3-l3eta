package org.x3.bukkit.permissions.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {
	
	public static void copy(File src, File dst) {
		try {			
			copy(new FileInputStream(src), new FileOutputStream(dst));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copy(InputStream in, File dst) {		
		try {
			if (!dst.exists()) {
				dst.createNewFile();
			}
			copy(in, new FileOutputStream(dst));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copy(InputStream in, FileOutputStream out) {
		try {
			BufferedReader br = createReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				out.write(line.getBytes());
			}
			br.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static BufferedReader createReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}
}
