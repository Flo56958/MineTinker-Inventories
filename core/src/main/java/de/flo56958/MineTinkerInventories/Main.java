package de.flo56958.MineTinkerInventories;

import de.flo56958.MineTinker.api.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Main extends JavaPlugin {

	private static JavaPlugin plugin;

	static ArrayList<GUI> guis = new ArrayList<>();

	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getPluginManager().registerEvents(new ShulkerboxListener(), this);
		Bukkit.getPluginManager().registerEvents(new CraftingListener(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}
}
