package com.vic.test.util;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public abstract class DigestUtil {

	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA1";
	public static final String SHA256 = "SHA-256";
	public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


	public static String digest(String text, String algorithm) {
		return digest(text, DEFAULT_CHARSET, algorithm);
	}

	public static String digest(String text, Charset charset, String algorithm) {
		return digest(text.getBytes(charset), algorithm);
	}

	public static String digest(byte[] bs, String algorithm) {
		return digest(bs, 0, bs.length, algorithm);
	}

	public static String digest(byte[] bs, int offset, int count, String algorithm) {
		ByteArrayInputStream in = new ByteArrayInputStream(bs, offset, count);
		try {
			return digest(in, algorithm);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// Do-Nothing
			}
		}
	}

	public static String digest(File file, String algorithm) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return digest(in, algorithm);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Do-Nothing
				}
			}
		}
	}

	public static String digest(InputStream in, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] buf = new byte[1024 * 2];
			int c;
			while ((c = in.read(buf)) != -1) {
				if (c > 0) {
					md.update(buf, 0, c);
				}
			}
			byte[] digest = md.digest();
			return hexString(digest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String hexString(byte[] bs) {
		StringBuilder sb = new StringBuilder(bs.length * 2);
		int b;
		for (int i = 0; i < bs.length; i++) {
			b = bs[i];
			if (b < 0) {
				b += 256;
			}
			if (b < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(b));
		}
		return sb.toString();
	}

}
