package cc.happyareabean.sjm.config;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.configs.yaml.YamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Getter
@SuppressWarnings("FieldMayBeFinal")
public class SJMConfig extends YamlConfiguration {

	@Comment({
			"The message to be send when player join the server",
			" ",
			"Placeholders available:",
			"   {player} - Player name",
			"   {time} - Current time",
			"   {online} - Server online players count",
	})
	private List<String> joinMessage = Arrays.asList(
			"",
			"<b>Hello <color:#3cff00>{player}</color>! Welcome to <color:#00d9ff>My Server</color>!</b>",
			"Currently have <yellow>{online}</yellow> players online! <b><color:#54585c>|</color></b> Time: <color:#ffac05>{time}</color>",
			"",
			"<b><color:#5865f2><click:open_url:'https://go.happyareabean.cc/supportdiscord'><hover:show_text:'<green>Click to join our <b><color:#5865f2>Discord</color></b> server!'>[Click here to join our Discord server]</hover></click></color></b>",
			"<b><gradient:#f7ff00:#db36a4><click:open_url:'https://go.happyareabean.cc/sjm'><hover:show_text:'Click to Download <gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient>'>[Download SimpleJoinMessage]</hover></click></gradient></b>",
			""
	);

	@Comment({
			"The time format will be used in the {time} placeholder.",
			"You can see all the available patterns here: https://go.happyareabean.cc/O5GZR"
	})
	private String timeFormat = "dd/MM/yyyy hh:mm:ss";

	@Comment({
			"Whether the message should be delayed? (In ticks)",
			"Change it to 0 to disable delay."
	})
	private int delayTicks = 20;

	public SJMConfig(Path path) {
		super(path, YamlProperties.builder()
				.setFormatter(FieldNameFormatters.IDENTITY)
				.setPrependedComments(Arrays.asList(
						"------------------------------------------------------------------------",
						" ",
						"   _____ _                 _           _       _       __  __                                ",
						"  / ____(_)               | |         | |     (_)     |  \\/  |                               ",
						" | (___  _ _ __ ___  _ __ | | ___     | | ___  _ _ __ | \\  / | ___  ___ ___  __ _  __ _  ___ ",
						"  \\___ \\| | '_ ` _ \\| '_ \\| |/ _ \\_   | |/ _ \\| | '_ \\| |\\/| |/ _ \\/ __/ __|/ _` |/ _` |/ _ \\",
						"  ____) | | | | | | | |_) | |  __/ |__| | (_) | | | | | |  | |  __/\\__ \\__ \\ (_| | (_| |  __/",
						" |_____/|_|_| |_| |_| .__/|_|\\___|\\____/ \\___/|_|_| |_|_|  |_|\\___||___/___/\\__,_|\\__, |\\___|",
						"                    | |                                                            __/ |     ",
						"                    |_|                                                           |___/      ",
						" ",
						"                           SimpleJoinMessage main configuration - settings.yml",
						" ",
						"------------------------------------------------------------------------",
						" ",
						" All the Message format only supported MiniMessage, you can read more about format at: https://docs.adventure.kyori.net/minimessage/format.html",
						" MiniMessage also have web interface available: https://webui.adventure.kyori.net/",
						" ",
						" PlaceholderAPI are also supported.",
						" ",
						"------------------------------------------------------------------------",
						" ",
						"https://go.happyareabean.cc/sjm",
						" ",
						"------------------------------------------------------------------------"
				)).build());
		this.loadAndSave();
	}
}
