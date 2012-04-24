package org.x3.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

	public static String[] getLines(File file) {
		final ArrayList<String> lines = new ArrayList<String>();
		new Reader(file) {
			@Override
			public void onLine(String line) {
				lines.add(line);
			}
		}.read();
		return lines.toArray(new String[0]);
	}

	private static abstract class Reader {
		private final File file;

		public Reader(File file) {
			this.file = file;
		}

		public void read() {
			new Threader().start();
		}

		public abstract void onLine(String line);

		private class Threader extends Thread {
			public void run() {
				try {
					BufferedReader in = new BufferedReader(new FileReader(file));
					String line;
					while ((line = in.readLine()) != null) {
						onLine(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
