package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistry {
	public static final Registry<MobEnchant> MOB_ENCHANT;

	static {
		MOB_ENCHANT = FabricRegistryBuilder.createDefaulted(MobEnchant.class, new Identifier(EnchantWithMob.MODID, "mob_enchant"), new Identifier(EnchantWithMob.MODID, "protect")).buildAndRegister();
	}

	public static void init() {

	}
}
