package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.mobenchant.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MobEnchants {
	public static final MobEnchant PROTECTION = new ProtectionMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4));
	public static final MobEnchant TOUGH = new ToughMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.ARMOR, "313644c5-ead2-4670-b9eb-0b41d59ce5a2", (double) 2.0F, AttributeModifier.Operation.ADDITION).addAttributesModifier(Attributes.ARMOR_TOUGHNESS, "8135df8f-38d9-490a-8d6f-c908fa973b34", (double) 0.5F, AttributeModifier.Operation.ADDITION);
	public static final MobEnchant SPEEDY = new SpeedyMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.UNCOMMON, 2)).addAttributesModifier(Attributes.MOVEMENT_SPEED, "501f27a9-4a75-4c2e-a2ab-91eeed71d748", (double) 0.05F, AttributeModifier.Operation.ADDITION);
	public static final MobEnchant STRONG = new StrongMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3)).addAttributesModifier(Attributes.ATTACK_DAMAGE, "3bfc67a1-3e3f-fa8e-8815-b2a93a5659ba", 0.5F, AttributeModifier.Operation.ADDITION);
	public static final MobEnchant THORN = new ThornEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 3));
	public static final MobEnchant HEALTH_BOOST = new HealthBoostMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 5)).addAttributesModifier(Attributes.MAX_HEALTH, "f5d32c9f-2a3d-4157-bbf7-469d348ce097", 2.0D, AttributeModifier.Operation.ADDITION);

	public static void init() {
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "protection"), PROTECTION);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "tough"), TOUGH);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "speedy"), SPEEDY);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "strong"), STRONG);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "thorn"), THORN);
		Registry.register(ModRegistry.MOB_ENCHANT, new ResourceLocation(EnchantWithMob.MODID, "health_boost"), HEALTH_BOOST);
	}
}
