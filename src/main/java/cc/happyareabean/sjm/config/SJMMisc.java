package cc.happyareabean.sjm.config;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.configs.yaml.YamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.Arrays;

@Getter @Setter
@SuppressWarnings("FieldMayBeFinal")
public class SJMMisc extends YamlConfiguration {

	public final Path path;

	@Comment({
			"Enable updater checker?",
			" ",
			"Depends on your need, but we generally recommend to keep it enable.",
			"So you won't miss any update! :)"
	})
	private boolean updateChecker = true;

	public SJMMisc(Path path) {
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
						"                           SimpleJoinMessage misc configuration - misc.yml",
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
