package de.flo56958.MineTinkerInventories;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	private static JavaPlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}
}
