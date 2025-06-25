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

	public static boolean isPaperAdventure() {

		try {
			Class.forName("io{}papermc{}paper{}text{}PaperComponents".replace("{}", "."));
		} catch (ClassNotFoundException e) {
            return false;
		}

		return true;
    }
}
