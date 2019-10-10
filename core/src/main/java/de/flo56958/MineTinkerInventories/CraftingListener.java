package de.flo56958.MineTinkerInventories;

import de.flo56958.MineTinker.Utilities.nms.NBTUtils;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.List;

public class CraftingListener implements Listener {

	//TODO: add manual crafting recipe for Shulker with the crafted shulkers in the newly crafted shulker already inside
	//TODO: shulker in output has dimension + 1

	//Recipe for next dimension
	/*
	SNS
	SSS
	SNS

	s: Shulker box
	N: Nether star

	needs 7 shulkers from the same dimension to make a dimension + 1
	 */

	//normal shulkers have dimension 0 or no nbt tag

	@EventHandler(ignoreCancelled = true)
	public void onCraftPrepare(PrepareItemCraftEvent e) {
		CraftingInventory inv = e.getInventory();
		ItemStack[] matrix = inv.getMatrix();

		if (matrix[1] != null && matrix[1].getType() == Material.NETHER_STAR) {
			if (matrix[7] != null && matrix[7].getType() == Material.NETHER_STAR) {
				if (matrix[0] != null && matrix[0].getType() == Material.SHULKER_BOX) {
					final int dimension = NBTUtils.getHandler().getInt(matrix[0], "MT-InventoryDimension");
					if (matrix[2] != null && matrix[2].getType() == Material.SHULKER_BOX && NBTUtils.getHandler().getInt(matrix[2], "MT-InventoryDimension") == dimension) {
						if (matrix[3] != null && matrix[3].getType() == Material.SHULKER_BOX && NBTUtils.getHandler().getInt(matrix[3], "MT-InventoryDimension") == dimension) {
							if (matrix[4] != null && matrix[4].getType() == Material.SHULKER_BOX && NBTUtils.getHandler().getInt(matrix[4], "MT-InventoryDimension") == dimension) {
								if (matrix[5] != null && matrix[5].getType() == Material.SHULKER_BOX && NBTUtils.getHandler().getInt(matrix[5], "MT-InventoryDimension") == dimension) {
									if (matrix[6] != null && matrix[6].getType() == Material.SHULKER_BOX && NBTUtils.getHandler().getInt(matrix[6], "MT-InventoryDimension") == dimension) {
										if (matrix[8] != null && matrix[8].getType() == Material.SHULKER_BOX && NBTUtils.getHandler().getInt(matrix[8], "MT-InventoryDimension") == dimension) {
											ItemStack item = createShulker(dimension + 1);
											if (item.getItemMeta() instanceof BlockStateMeta) {
												BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
												if (im.getBlockState() instanceof ShulkerBox) {
													ShulkerBox shulker = (ShulkerBox) im.getBlockState();
													Inventory s_inv = shulker.getInventory(); //FIXME: Inventory is instantly removed again as a inventory
													for (int i = 0; i < 9; i++) {
														if (i == 1 || i == 7) continue;
														s_inv.setItem(i, matrix[i]);
													}


													inv.setResult(item);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private ItemStack createShulker(int dimension) {
		ItemStack item = new ItemStack(Material.SHULKER_BOX);
		if (dimension == 0) return item;
		if (item.getItemMeta() instanceof BlockStateMeta) {
			BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
			if (im.getBlockState() instanceof ShulkerBox) {
				ShulkerBox shulker = (ShulkerBox) im.getBlockState();
				List<String> lore = im.getLore();
				if (lore == null) {
					lore = new ArrayList<>();
				}
				lore.add("Dimension: " + dimension);
				im.setLore(lore);
				item.setItemMeta(im);

				NBTUtils.getHandler().setInt(item, "MT-InventoryDimension", dimension);

				Inventory s_inv = shulker.getInventory();
				ItemStack filler = createShulker(dimension - 1);
				for (int i = 0; i < s_inv.getSize(); i++) {
					s_inv.setItem(i, filler.clone());
				}
			}
		}

		return item;
	}
}
