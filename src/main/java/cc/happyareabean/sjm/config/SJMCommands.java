package cc.happyareabean.sjm.config;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.configs.yaml.YamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.Arrays;

@Getter @Setter
@SuppressWarnings("FieldMayBeFinal")
public class SJMCommands extends YamlConfiguration {

	public final Path path;

	@Comment({
			"You can customize a custom show command here!",
			"This just work as the /sjm show command but without any permission.",
			" ",
			"When using /sjm reload the new command will register but",
			"old command will remain untouched.",
			" ",
			"Please restart your server if you want",
			"the old command to be removed.",
	})
	private CustomShow customShow = new CustomShow();

	@Getter @Setter
	@ConfigurationElement
	public static class CustomShow {

		private boolean enabled = true;
		private String command = "motd";

	}

	public SJMCommands(Path path) {
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
						"                           SimpleJoinMessage commands configuration - commands.yml",
						" ",
						"------------------------------------------------------------------------",
						" ",
						"https://go.happyareabean.cc/sjm",
						" ",
						"------------------------------------------------------------------------"
				)).build());
		this.path = path;

		this.loadAndSave();
	}

	public void reloadAndSave() {
		load();
		save();
	}
}
