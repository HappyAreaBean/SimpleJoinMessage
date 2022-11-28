package cc.happyareabean.sjm.utils;

import cc.happyareabean.sjm.SimpleJoinMessage;
import net.kyori.adventure.text.Component;

import static cc.happyareabean.sjm.SimpleJoinMessage.MINIMESSAGE;

public class Constants {

	public static final String PREFIX_MINIMESSAGE = "<white>[<gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient>]</white> ";
	public static final Component PREFIX = MINIMESSAGE.deserialize(PREFIX_MINIMESSAGE);
	public static final Component HEADER = MINIMESSAGE.deserialize("<gradient:green:blue>－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－</gradient>");

	public static final String VERSION = SimpleJoinMessage.getInstance().getDescription().getVersion();
	public static final String USER_AGENT = "SimpleJoinMessage/" + VERSION;
}
