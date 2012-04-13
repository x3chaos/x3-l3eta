package org.x3.bukkit.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Builder {
	ArrayList<File> fileDump = new ArrayList<File>();
	private static String directory = "./", home = "./", output = "Plugin";
	private static String temp = "./temp/", tempLib = "./temp/lib/", homeLib = "./lib/";
	
	public static void main(String[] args) {
		if (args.length == 3) {
			home = args[0];
			directory = args[1];
			output = args[2];
			homeLib = home + "lib/";
			temp = home + "temp/";
			tempLib = temp + "lib/";
		}
		new Builder();
	}

	public Builder() {
		copyLibs();
	}

	public void copyLibs() {
		new File(tempLib).mkdirs();
		String[] libs = new File(homeLib).list();
		for (String lib : libs) {
			if (!lib.startsWith("bukkit")) {
				File orig = new File(homeLib, lib), copy = new File(tempLib, lib);
				try {
					copy.createNewFile();
					FileInputStream fr = new FileInputStream(orig);
					FileOutputStream fw = new FileOutputStream(copy);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = fr.read(buffer)) > 0) {
						fw.write(buffer, 0, len);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		extractLib();
	}

	public void extractLib() {
		File[] libs = new File(tempLib).listFiles();
		try {
			for (File lib : libs) {
				byte[] buffer = new byte[1024];
				ZipInputStream in = new ZipInputStream(new FileInputStream(lib));
				FileOutputStream output = null;
				int len;
				ZipEntry x;
				while ((x = in.getNextEntry()) != null) {
					File file = new File(temp, x.getName());
					if (x.isDirectory()) {
						file.mkdir();
					} else {
						file.createNewFile();
						output = new FileOutputStream(file);
						while ((len = in.read(buffer)) > 0) {
							output.write(buffer, 0, len);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		sniffFiles();
	}

	public void sniffFiles() {
		String[] dirs = { directory, temp };
		for (String dir : dirs) {
			dumpFiles(new File(dir));
		}
		for (int i = 0; i < fileDump.size(); i++) {
			File file = fileDump.get(i);
			if (!file.getName().endsWith(".class")) {
				fileDump.remove(file);
			}
		}
		fileDump.add(new File(home + "bin/" + directory + "data/", "plugin.yml"));
		fileDump.add(new File(home + "bin/" + directory + "data/",
				"MANIFEST.MF"));
		makeZip();
	}

	public void dumpFiles(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				dumpFiles(file);
			else
				fileDump.add(file);
		}
	}

	public void makeZip() {
		try {
			byte[] buffer = new byte[1024];
			File zip = new File(home, output + ".zip");
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
			for (File file : fileDump.toArray(new File[0])) {
				FileInputStream in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(getFileToZip(file.getPath())));
				int len;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
			File jar = new File(home, output + ".jar");
			if (jar.exists())
				jar.delete();
			zip.renameTo(jar);
			cleanUp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cleanUp() {
		try {
			Runtime.getRuntime().exec("cmd.exe rmdir /S temp/"); // To fix
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getFileToZip(String file) {
		file = file.replace('\\', '/');
		file = file.replaceFirst("(bin/|temp/|data/)", "");
		System.out.println(file);
		return file.endsWith(".MF") ? "META-INF/" + file : file;
	}
}
