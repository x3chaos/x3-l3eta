package org.x3.bukkit.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.x3.bukkit.permissions.util.Util;

public class Builder {
	ArrayList<File> fileDump = new ArrayList<File>();
	private static String directory = "./", home = System
			.getProperty("user.dir").replace("bin", ""), output = "Plugin";
	private static String temp = "./temp/", homeLib = "./lib/", data = "./";
	private static File plugin;
	private static String[] other;
	private static File tempFile;

	public static void main(String[] args) {
		if (args.length >= 2) {
			plugin =  new File(home + "bin/org/x3/bukkit/", args[0] + ".class");
			output = args[1];
			directory = args[2];
			homeLib = home + "lib/";
			temp = home + "temp/";
			data = directory.replace('\\', '/') + "data/";
			tempFile = new File(temp);
			if (args.length > 3) {
				other = Util.sliceArray(args, 3);
			}
		}
		new Builder();
	}

	public Builder() {
		if (!tempFile.exists())
			tempFile.mkdirs();
		this.extractLibs();
	}

	public void extractLibs() {
		System.out.println(homeLib);
		File[] libs = new File(homeLib).listFiles();
		try {
			for (File lib : libs) {
				if (!lib.getName().contains("bukkit")) {
					byte[] buffer = new byte[1024];
					ZipInputStream in = new ZipInputStream(new FileInputStream(
							lib));
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
							output.close();
						}
					}
					in.close();
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
		if (other != null && other.length != 0) {
			for (String o : other) {
				dumpFiles(new File(o));
			}
		}
		for (File file : fileDump.toArray(new File[0])) {
			String f = file.getPath() + file.getName();
			String[] blackList = { ".jar", ".java", ".zip", ".MF" };
			if (!f.contains("."))
				fileDump.remove(file);
			for (String bad : blackList) {
				if (f.endsWith(bad))
					fileDump.remove(file);
			}
		}
		fileDump.add(plugin);
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
			makeManifest(out);
			out.close();
			File jar = new File(home, output + ".jar");
			if (jar.exists())
				jar.delete();
			zip.renameTo(jar);
			cleanUp(tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void makeManifest(ZipOutputStream out) {
		try {
			String manifest = "Manifest-Version: 1.0\n";
			out.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			out.write(manifest.getBytes());
			out.closeEntry();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void cleanUp(File dir) {
		try {
			File[] list = dir.listFiles();
			dir.deleteOnExit();
			for (File file : list) {
				if (file.isDirectory()) {
					cleanUp(file);
				} else {
					file.delete();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getFileToZip(String file) {
		file = file.replace(home, "").replace('\\', '/');
		file = file.replaceFirst("(bin|temp)/", "");
		if (file.endsWith("plugin.yml")) {
			file = file.replace(data, "");
		}
		System.out.println("Adding: " + file);
		return file;
	}

	public static class ReflectionScan {
		public static String[] getImports(String c) {
			try {

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}
}
