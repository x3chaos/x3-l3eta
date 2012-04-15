package org.l3eta;

import java.security.MessageDigest;

public class Crypto {

	public static String randomHash() {
		return sha(Math.random());
	}

	public static String randomHash(int length) {
		return sha(Math.random(), null, length);
	}

	public static String sha(Object o) {
		return sha(o, null);
	}

	public static String sha(Object o, String salt) {
		return sha(o, salt, 0);
	}

	public static String sha(Object o, String salt, int length) {
		StringBuilder key = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("sha1");
			md.update(String.valueOf(o).getBytes());
			if (salt != null)
				md.update(salt.getBytes());
			for (byte b : md.digest()) {
				key.append(Integer.toHexString((0xff & b) | 0xffffff00)
						.substring(6));
			}
		} catch (Exception ex) {
		}
		String finalKey = key.toString();
		if (length == 0 || length >= finalKey.length())
			return finalKey;
		return finalKey.substring(0, length);
	}
}