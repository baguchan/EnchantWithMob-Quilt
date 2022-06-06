package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.ModRegistry;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MobEnchant {
	private final Map<EntityAttribute, EntityAttributeModifier> attributeModifiers = Maps.newHashMap();
	protected final Rarity enchantType;
	private final int level;
	private int minlevel = 1;

	@Nullable
	private String translationKey;

	@Nullable
	public static MobEnchant byRawId(int rawId) {
		return (MobEnchant) ModRegistry.MOB_ENCHANT.get(rawId);
	}

	public static int getRawId(MobEnchant type) {
		return ModRegistry.MOB_ENCHANT.getRawId(type);
	}


	public MobEnchant(Properties properties) {
		this.enchantType = properties.enchantType;
		this.level = properties.level;
	}

	public Rarity getRarity() {
		return enchantType;
	}

	public MobEnchant setMinLevel(int level) {
		this.minlevel = level;

		return this;
	}

	protected String loadTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("mob_enchant", ModRegistry.MOB_ENCHANT.getId(this));
		}

		return this.translationKey;
	}

	public String getTranslationKey() {
		return this.loadTranslationKey();
	}


	/**
	 * Returns the minimum level that the enchantment can have.
	 */
	public int getMinLevel() {
		return minlevel;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	public int getMaxLevel() {
		return level;
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + enchantmentLevel * 10;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 5;
	}


	public void tick(LivingEntity entity, int level) {

	}

	public final boolean isCompatibleWith(MobEnchant enchantmentIn) {
		return this.canApplyTogether(enchantmentIn) && enchantmentIn.canApplyTogether(this);
	}

	public boolean isTresureEnchant() {
		return false;
	}

	public boolean isOnlyChest() {
		return false;
	}

	public boolean isCompatibleMob(LivingEntity livingEntity) {
		return true;
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this enchantment.
	 */
	protected boolean canApplyTogether(MobEnchant ench) {
		return this != ench;
	}

	public MobEnchant addAttributeModifier(EntityAttribute attribute, String uuid, double amount, EntityAttributeModifier.Operation operation) {
		EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(UUID.fromString(uuid), this::getTranslationKey, amount, operation);
		this.attributeModifiers.put(attribute, entityAttributeModifier);
		return this;
	}

	public Map<EntityAttribute, EntityAttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}

	public void onRemoved(AttributeContainer attributes) {
		Iterator var4 = this.attributeModifiers.entrySet().iterator();

		while (var4.hasNext()) {
			Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry) var4.next();
			EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance((EntityAttribute) entry.getKey());
			if (entityAttributeInstance != null) {
				entityAttributeInstance.removeModifier((EntityAttributeModifier) entry.getValue());
			}
		}

	}

	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		Iterator var4 = this.attributeModifiers.entrySet().iterator();

		while (var4.hasNext()) {
			Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry) var4.next();
			EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance((EntityAttribute) entry.getKey());
			if (entityAttributeInstance != null) {
				EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier) entry.getValue();
				entityAttributeInstance.removeModifier(entityAttributeModifier);
				entityAttributeInstance.addPersistentModifier(new EntityAttributeModifier(entityAttributeModifier.getId(), this.getTranslationKey() + " " + amplifier, this.adjustModifierAmount(amplifier, entityAttributeModifier), entityAttributeModifier.getOperation()));
			}
		}

	}

	public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
		return modifier.getValue() * (double) (amplifier + 1);
	}

	/*public boolean isDisabled() {
		return EnchantConfig.COMMON.DISABLE_ENCHANTS.get().contains(this.getRegistryName().toString());
	}*/


	public static class Properties {
		private final Rarity enchantType;
		private final int level;

		public Properties(Rarity enchantType, int level) {
			this.enchantType = enchantType;
			this.level = level;
		}
	}

	public static enum Rarity {
		COMMON(10),
		UNCOMMON(5),
		RARE(2),
		VERY_RARE(1);

		private final int weight;

		private Rarity(int rarityWeight) {
			this.weight = rarityWeight;
		}

		/**
		 * Retrieves the weight of Rarity.
		 */
		public int getWeight() {
			return this.weight;
		}
	}
}
