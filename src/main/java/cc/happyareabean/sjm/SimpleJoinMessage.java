package cc.happyareabean.sjm;

import cc.happyareabean.sjm.commands.SJMCommand;
import cc.happyareabean.sjm.config.SJMConfig;
import cc.happyareabean.sjm.listener.PlayerJoinListener;
import cc.happyareabean.sjm.listener.UpdateNotifyListener;
import cc.happyareabean.sjm.utils.AdventureWebEditorAPI;
import cc.happyareabean.sjm.utils.Constants;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.net.URI;
import java.util.Arrays;

public class SimpleJoinMessage extends JavaPlugin {

	public static String NEXT_VERSION = "";
	public static String DOWNLOAD_URL = "https://www.spigotmc.org/resources/103413/";

	@Getter private static SimpleJoinMessage instance;
	@Getter public static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
	@Getter public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacy('&');
	@Getter private static BukkitAudiences adventure;
	@Getter private AdventureWebEditorAPI adventureWebEditorAPI;
	@Getter private BukkitCommandHandler commandHandler;
	@Getter private SJMConfig SJMConfig;

	@Override
	public void onEnable() {
		instance = this;
		adventure = BukkitAudiences.create(this);

		getLogger().info("Loading settings...");
		SJMConfig = new SJMConfig(new File(getDataFolder(), "settings.yml").toPath());

		getLogger().info("Registering listener...");
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new UpdateNotifyListener(), this);

		getLogger().info("Registering commands...");
		commandHandler = BukkitCommandHandler.create(this);
		commandHandler.enableAdventure(adventure);
		commandHandler.setMessagePrefix(LEGACY_SERIALIZER.serialize(Constants.PREFIX));
		commandHandler.setHelpWriter((command, actor) -> {
			StringBuilder sb = new StringBuilder();
			sb.append("&8• &e/");
			sb.append(command.getPath().toRealString());
			if (!command.getUsage().isEmpty()) {
				sb.append(" ");
				sb.append(command.getUsage());
			}
			sb.append(" &7- &f");
			sb.append(command.getDescription());
			return sb.toString();
		});
		commandHandler.register(new SJMCommand());

		adventureWebEditorAPI = new AdventureWebEditorAPI(URI.create(this.SJMConfig.getAdventureWebURL()));

		getLogger().info("Check supported plugins...");
		checkSupportedPlugin();

		new Metrics(this, 15462);

		getLogger().info("SimpleJoinMessage version " + getDescription().getVersion() + " has been successfully enabled!");

		checkUpdate();
	}

	public void checkUpdate() {
		String version = getDescription().getVersion();
		if (version.endsWith("-SNAPSHOT")) {
			Arrays.asList(
					"******************************************",
					"You are currently using development build of SimpleJoinMessage!",
					"Please report issues here: https://go.happyareabean.cc/sjm/issues",
					"******************************************"
			).forEach(s -> getLogger().warning(s));
			return;
		}

		SpigetUpdate updater = new SpigetUpdate(this, 103413);
		updater.setVersionComparator(VersionComparator.SEM_VER);
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
		});
	}

	public void checkSupportedPlugin() {
		if (!isPAPISupported()) {
			getLogger().warning("PlaceholderAPI is not enabled, you will not be able to use any placeholder from PlaceholderAPI.");
		}
	}

	public boolean isPAPISupported() {
		return getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
}
