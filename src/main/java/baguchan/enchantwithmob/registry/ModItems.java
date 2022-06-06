package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.item.MobEnchantBookItem;
import baguchan.enchantwithmob.item.MobUnEnchantBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
	public static final Item MOB_ENCHANT_BOOK = new MobEnchantBookItem((new Item.Settings()).maxCount(1).maxDamage(5).group(ItemGroup.MISC));
	public static final Item MOB_UNENCHANT_BOOK = new MobUnEnchantBookItem((new Item.Settings()).maxCount(1).maxDamage(5).group(ItemGroup.MISC));


	public static void init() {
		Registry.register(Registry.ITEM, new Identifier(EnchantWithMob.MODID, "mob_enchant_book"), MOB_ENCHANT_BOOK);
		Registry.register(Registry.ITEM, new Identifier(EnchantWithMob.MODID, "mob_unenchant_book"), MOB_UNENCHANT_BOOK);
	}
}
