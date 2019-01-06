package fr.trxyy.interfaces.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class FileUtil {

	public static String getEtag(final HttpURLConnection connection) {
		return getEtag(connection.getHeaderField("ETag"));
	}

	public static String getEtag(String etag) {
		if (etag == null)
			etag = "-";
		else if (etag.startsWith("\"") && etag.endsWith("\""))
			etag = etag.substring(1, etag.length() - 1);

		return etag;
	}

	public static String getMD5(final File file) {
		DigestInputStream stream = null;
		try {
			stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"));
			final byte[] buffer = new byte[65536];

			int read = stream.read(buffer);
			while (read >= 1)
				read = stream.read(buffer);
		} catch (final Exception ignored) {
			return null;
		} finally {
			closeSilently(stream);
		}

		return String.format("%1$032x", new Object[] { new BigInteger(1, stream.getMessageDigest().digest()) });
	}

	public static void closeSilently(final Closeable closeable) {
		if (closeable != null)
			try {
				closeable.close();
			} catch (final IOException localIOException) {
			}
	}
	
	public static void deleteFile(File element) {
	    if (element.isDirectory()) {
	        for (File sub : element.listFiles()) {
	        	Wrapper.log("Deleting..: " + sub);
	            deleteFile(sub);
	        }
	    }
	    element.delete();
	}
	
	public static void deleteSomething(String path) {
		Path filePath_1 = Paths.get(path);
		try {
		    Files.delete(filePath_1);
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", path);
		} catch (IOException x) {
		    System.err.println(x);
		}
	}
	
	public static void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	        	Wrapper.log("Deleting directory..." + f);
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
}
