package de.flo56958.MineTinkerInventories;

import de.flo56958.MineTinker.Utilities.nms.NBTUtils;
import de.flo56958.MineTinker.api.gui.GUI;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
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
						ShulkerBox shulker = (ShulkerBox) im.getBlockState();
						int dimension = NBTUtils.getHandler().getInt(item, "MT-InventoryDimension");
						if (dimension <= 0) {
							e.getWhoClicked().openInventory(shulker.getInventory());
							return;
						}

						for (GUI g : Main.guis) {
							//TODO: search if a gui was created for shulker dimension
						}

						//TODO: create if not found

						//this event will not trigger inside a gui

						//make buttonaction with runnable that will open the inventory of the clicked shulker (left click)

						//TODO: Change GUI constructor so it can register Events from different plugins
					}
				}
			}
		}
	}
}
