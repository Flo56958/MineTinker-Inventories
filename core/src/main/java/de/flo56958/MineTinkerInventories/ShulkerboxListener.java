package de.flo56958.MineTinkerInventories;

import de.flo56958.MineTinker.Utilities.nms.NBTUtils;
import de.flo56958.MineTinker.api.gui.ButtonAction;
import de.flo56958.MineTinker.api.gui.GUI;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerboxListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onShulkerPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		if (item == null) return;

		if (item.getType() == Material.SHULKER_BOX) {
			if (NBTUtils.getHandler().hasTag(item, "MT-InventoryDimension")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onMiddleClick(InventoryClickEvent e) {
		if (e.getClick() == ClickType.MIDDLE) {
			ItemStack item = e.getClickedInventory().getItem(e.getSlot());
			if (item == null) return;

			if (item.getType() == Material.SHULKER_BOX) {
				if (item.getItemMeta() instanceof BlockStateMeta) {
					BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
					if (im.getBlockState() instanceof ShulkerBox) {
						e.setCancelled(true);

						ShulkerBox shulker = (ShulkerBox) im.getBlockState();
						int dimension = NBTUtils.getHandler().getInt(item, "MT-InventoryDimension");
						if (dimension <= 0) {
							e.getWhoClicked().openInventory(shulker.getInventory());
							return;
						}

						GUI.Window window = null;

						for (GUI g : Main.guis) {
							window = g.getWindowFromInventory(shulker.getInventory());
							if(window != null) break;
						}

						if (window == null) {
							GUI ret = createShulkerGUI(shulker, dimension);
							if (e.getWhoClicked() instanceof Player) {
								ret.show((Player) e.getWhoClicked());
							}
						}

						//TODO: Change GUI constructor so it can register Events from different plugins
					}
				}
			}
		}
	}

	private GUI createShulkerGUI(ShulkerBox shulker, int dimension) {
		GUI gui = new GUI();
		Main.guis.add(gui);
		GUI.Window window = gui.addWindow(shulker.getInventory());

		for (int i = 0; i < shulker.getInventory().getSize(); i++) {
			ItemStack slot = shulker.getInventory().getItem(i);
			if (slot == null) continue;
			if (slot.getItemMeta() instanceof BlockStateMeta) {
				BlockStateMeta im = (BlockStateMeta) slot.getItemMeta();
				if (im.getBlockState() instanceof ShulkerBox) {
					GUI.Window.Button button = window.addButton(i, slot);
					if (dimension == 1) {
						button.addAction(ClickType.LEFT, new ButtonAction.RUN_RUNNABLE_ON_PLAYER(button, ((player, s) -> player.openInventory(shulker.getInventory()))));
					} else {
						GUI.Window window2 = null;

						for (GUI g : Main.guis) {
							window2 = g.getWindowFromInventory(shulker.getInventory());
							if(window2 != null) break;
						}

						if (window2 == null) {
							GUI ret = createShulkerGUI((ShulkerBox) slot, dimension - 1);
							button.addAction(ClickType.LEFT, new ButtonAction.PAGE_GOTO(button, ret.getWindow(0)));
						} else {
							button.addAction(ClickType.LEFT, new ButtonAction.PAGE_GOTO(button, window2));
						}

					}
				}
			}
		}

		return gui;
	}
}
