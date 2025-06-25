package cc.happyareabean.sjm;

import cc.happyareabean.sjm.commands.CustomShowCommand;
import cc.happyareabean.sjm.commands.SJMCommand;
import cc.happyareabean.sjm.config.SJMCommands;
import cc.happyareabean.sjm.config.SJMConfig;
import cc.happyareabean.sjm.config.SJMMisc;
import cc.happyareabean.sjm.listener.PlayerJoinListener;
import cc.happyareabean.sjm.listener.UpdateNotifyListener;
import cc.happyareabean.sjm.utils.AdventureWebEditorAPI;
import cc.happyareabean.sjm.utils.Util;
import cc.happyareabean.sjm.utils.message.AbstractMessageUtil;
import cc.happyareabean.sjm.utils.message.impl.BukkitMessageUtil;
import cc.happyareabean.sjm.utils.message.impl.PaperMessageUtil;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.BukkitLampConfig;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.orphan.Orphans;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class SimpleJoinMessage extends JavaPlugin {

	public static String NEXT_VERSION = "";
	public static String DOWNLOAD_URL = "https://www.spigotmc.org/resources/103413/";

	@Getter private static SimpleJoinMessage instance;
	@Getter public static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
	@Getter public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacy('&');
	@Getter private static AbstractMessageUtil messageUtil;
	@Getter private AdventureWebEditorAPI adventureWebEditorAPI;
	@Getter private Lamp<BukkitCommandActor> commandHandler;
	@Getter private SJMConfig SJMConfig;
	@Getter private SJMMisc miscConfig;
	@Getter private SJMCommands commandsConfig;

	@Override
	public void onEnable() {
		instance = this;

		if (Util.isPaperAdventure()) {
			messageUtil = new PaperMessageUtil(this);
		} else {
			messageUtil = new BukkitMessageUtil(this);
		}

		getLogger().info("Loading settings...");
		SJMConfig = new SJMConfig(new File(getDataFolder(), "settings.yml").toPath());
		miscConfig = new SJMMisc(new File(getDataFolder(), "misc.yml").toPath());
		commandsConfig = new SJMCommands(new File(getDataFolder(), "commands.yml").toPath());

		getLogger().info("Registering listener...");
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new UpdateNotifyListener(), this);

		getLogger().info("Registering commands...");
		BukkitLampConfig.Builder<BukkitCommandActor> commandConfig = BukkitLampConfig.builder(this);
		if (!Util.isPaperAdventure()) commandConfig.audiences(BukkitAudiences.create(this));
		commandHandler = BukkitLamp.builder(commandConfig.build()).build();
		commandHandler.register(new SJMCommand());

		if (commandsConfig.getCustomShow().isEnabled())
			loadCustomCommand(false);

		adventureWebEditorAPI = new AdventureWebEditorAPI(URI.create(this.SJMConfig.getAdventureWebURL()));

		getLogger().info("Check supported plugins...");
		checkSupportedPlugin();

		new Metrics(this, 15462);

		getLogger().info("SimpleJoinMessage version " + getDescription().getVersion() + " has been successfully enabled!");

		checkUpdate();
	}

	public void checkUpdate() {
		String version = getDescription().getVersion();
		if (version.contains("-SNAPSHOT")) {
			Arrays.asList(
					"******************************************",
					"You are currently using development build of SimpleJoinMessage!",
					"Please report issues here: https://go.happyareabean.cc/sjm/issues",
					"******************************************"
			).forEach(s -> getLogger().warning(s));
			return;
		}

		if (!miscConfig.isUpdateChecker()) return;

		SpigetUpdate updater = new SpigetUpdate(this, 103413);
		updater.setVersionComparator(VersionComparator.SEM_VER);
		updater.setDebug(false);
		updater.checkForUpdate(new UpdateCallback() {
			@Override
			public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
				NEXT_VERSION = newVersion;

				Arrays.asList(
						"******************************************",
						"",
						"There is a new version of SimpleJoinMessage available!",
						"",
						"Your Version: " + version,
						"New Version: " + NEXT_VERSION,
						"",
						"Download at " + downloadUrl,
						"",
						"******************************************"
				).forEach(s -> getLogger().warning(s));
			}

			@Override
			public void upToDate() {
				getLogger().info(String.format("SimpleJoinMessage is up to date! (%s)", version));
			}

			@Override
			public void updateCheckFailed() {
				getLogger().warning("Failed to check update.");
			}
		});
	}

	public void checkSupportedPlugin() {
		if (!isPAPISupported()) {
			getLogger().warning("PlaceholderAPI not found, you will not be able to use any placeholder from PlaceholderAPI.");
		}
	}

	public boolean isPAPISupported() {
		return getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
	}

	public String regenerateSettings() {
		Path path = SJMConfig.getPath();
		String fileName = path.getFileName().toString();
		String timestamp = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd.HH-mm-ss"));
		String backupFileName = fileName + ".backup." + timestamp;
		FileUtil.copy(path.toFile(), path.getParent().resolve(backupFileName).toFile());
		if (path.toFile().delete()) {
			SJMConfig = new SJMConfig(path);
		}
		return backupFileName;
	}

	public void loadCustomCommand(boolean reload) {
		String oldCustom = commandsConfig.getCustomShow().getCommand();
		String newCustom;

		if (reload) {
			commandsConfig.reloadAndSave();
			newCustom = commandsConfig.getCustomShow().getCommand();

			if (newCustom.equals(oldCustom)) return;
		}

		commandHandler.register(commandHandler.register(Orphans.path(commandsConfig.getCustomShow().getCommand()).handler(new CustomShowCommand())));
	}
}
