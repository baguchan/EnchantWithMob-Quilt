package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.ModRegistry;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MobEnchant {
	private final Map<Attribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
	protected final Rarity enchantType;
	private final int level;
	private int minlevel = 1;

	@Nullable
	private String descriptionId;

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

	protected String getOrCreateDescriptionId() {
		if (this.descriptionId == null) {
			this.descriptionId = Util.makeDescriptionId("mob_enchant", ModRegistry.MOB_ENCHANT.getKey(this));
		}

		return this.descriptionId;
	}

	public String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}


	public MobEnchant addAttributeModifier(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
		AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString(uuid), this::getDescriptionId, amount, operation);
		this.attributeModifierMap.put(attribute, attributeModifier);
		return this;
	}

	public Map<Attribute, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifierMap;
	}

	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		Iterator var4 = this.attributeModifierMap.entrySet().iterator();

		while (var4.hasNext()) {
			Map.Entry<Attribute, AttributeModifier> entry = (Map.Entry) var4.next();
			AttributeInstance attributeInstance = attributes.getInstance((Attribute) entry.getKey());
			if (attributeInstance != null) {
				attributeInstance.removeModifier((AttributeModifier) entry.getValue());
			}
		}

	}

	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		Iterator var4 = this.attributeModifierMap.entrySet().iterator();

		while (var4.hasNext()) {
			Map.Entry<Attribute, AttributeModifier> entry = (Map.Entry) var4.next();
			AttributeInstance attributeInstance = attributes.getInstance((Attribute) entry.getKey());
			if (attributeInstance != null) {
				AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
				attributeInstance.removeModifier(attributeModifier);
				attributeInstance.addPermanentModifier(new AttributeModifier(attributeModifier.getId(), this.getDescriptionId() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributeModifier), attributeModifier.getOperation()));
			}
		}

	}

	public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
		return modifier.getAmount() * (double) (amplifier);
	}


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
