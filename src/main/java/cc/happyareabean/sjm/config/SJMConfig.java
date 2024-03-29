package cc.happyareabean.sjm.config;

import cc.happyareabean.sjm.SimpleJoinMessage;
import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.configs.yaml.YamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@SuppressWarnings("FieldMayBeFinal")
public class SJMConfig extends YamlConfiguration {

	public final Path path;

	@Comment({
			"The message to be sent when player joins the server.",
			" ",
			"Placeholders available:",
			"   {player} - Player name",
			"   {time} - Current time",
			"   {online} - Server online players count",
			" ",
			"--------------------------------- NOTE ---------------------------------",
			" ",
			" We recommend you edit your join message through web editor by using /sjm editor",
			" instead of editing directly with the setting file.",
			" ",
			" If you know what you're doing, you can safely ignore this warning.",
			" ",
			"------------------------------------------------------------------------",
	})
	private List<String> joinMessage = Arrays.asList(
			" ",
			"<b>Hello <color:#3cff00>{player}</color>! Welcome to <color:#00d9ff>My Server</color>!</b>",
			"Currently have <yellow>{online}</yellow> players online! <b><color:#54585c>|</color></b> Time: <color:#ffac05>{time}</color>",
			" ",
			"<b><color:#5865f2><click:open_url:'https://go.happyareabean.cc/supportdiscord'><hover:show_text:'<green>Click to join our <b><color:#5865f2>Discord</color></b> server!'>[Click here to join our Discord server]</hover></click></color></b>",
			"<b><gradient:#f7ff00:#db36a4><click:open_url:'https://go.happyareabean.cc/sjm'><hover:show_text:'Click to Download <gradient:#f7ff00:#db36a4><b>SimpleJoinMessage</b></gradient>'>[Download SimpleJoinMessage]</hover></click></gradient></b>",
			" "
	);

	@Comment({
			"The time format will be used in the {time} placeholder.",
			"You can see all the available patterns here: https://go.happyareabean.cc/O5GZR"
	})
	private String timeFormat = "dd/MM/yyyy hh:mm:ss";

	@Comment({
			"Whether the message should be delayed? (In ticks! 1 second = 20 ticks)",
			"Change it to 0 to disable delay."
	})
	private int delayTicks = 20;

	@Comment({
			"The URL to send adventure web interface request.",
			" ",
			"You usually don't need to change it, keep it as default.",
	})
	private String adventureWebURL = "https://webui.advntr.dev/";

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
						" All the Message format only supported MiniMessage, you can read more about format at: https://docs.advntr.dev/minimessage/format.html",
						" MiniMessage also have web interface available: https://webui.advntr.dev/",
						" ",
						" PlaceholderAPI are also supported.",
						" ",
						"------------------------------------------------------------------------",
						" ",
						"https://go.happyareabean.cc/sjm",
						" ",
						"------------------------------------------------------------------------"
				)).build());
		this.path = path;

		this.loadAndSave();

		// Update old url
		if (adventureWebURL.equalsIgnoreCase("https://webui.adventure.kyori.net")) {
			adventureWebURL = "https://webui.advntr.dev/";
			save();
			load();
		}
	}

	public void reloadAndSave() {
		load();
		save();
		SimpleJoinMessage.getInstance().getAdventureWebEditorAPI().setRoot(URI.create(adventureWebURL));
	}
}
