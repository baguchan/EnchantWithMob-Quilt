package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.mobenchant.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class MobEnchants {
	public static final MobEnchant PROTECTION = new ProtectionMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4));
	public static final MobEnchant TOUGH = new ToughMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "313644c5-ead2-4670-b9eb-0b41d59ce5a2", (double) 2.0F, EntityAttributeModifier.Operation.ADDITION).addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "8135df8f-38d9-490a-8d6f-c908fa973b34", (double) 0.5F, EntityAttributeModifier.Operation.ADDITION);
	public static final MobEnchant SPEEDY = new SpeedyMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.UNCOMMON, 2)).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "501f27a9-4a75-4c2e-a2ab-91eeed71d748", (double) 0.05F, EntityAttributeModifier.Operation.ADDITION);
	public static final MobEnchant STRONG = new StrongMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3));
	public static final MobEnchant THORN = new ThornEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 3));
	public static final MobEnchant HEALTH_BOOST = new HealthBoostMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 5)).addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "f5d32c9f-2a3d-4157-bbf7-469d348ce097", 2.0D, EntityAttributeModifier.Operation.ADDITION);

	public static void init() {
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "protection"), PROTECTION);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "tough"), TOUGH);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "speedy"), SPEEDY);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "strong"), STRONG);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "thorn"), THORN);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "health_boost"), HEALTH_BOOST);
	}
}
