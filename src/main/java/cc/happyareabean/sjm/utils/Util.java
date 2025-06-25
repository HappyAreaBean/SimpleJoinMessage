package cc.happyareabean.sjm.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Util {

	public static URL convertToUrl(URI uri) {
		try {
			return uri.toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
