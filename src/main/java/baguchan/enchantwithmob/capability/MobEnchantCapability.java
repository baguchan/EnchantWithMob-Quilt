package baguchan.enchantwithmob.capability;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MobEnchantCapability {
	private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("6699a403-e2cc-31e6-195e-4757200e0935");

	private static final EntityAttributeModifier HEALTH_MODIFIER = new EntityAttributeModifier(HEALTH_MODIFIER_UUID, "Health boost", 0.5D, EntityAttributeModifier.Operation.MULTIPLY_BASE);


	private List<MobEnchantHandler> mobEnchants = Lists.newArrayList();
	private Optional<LivingEntity> enchantOwner = Optional.empty();
	private boolean fromOwner;


	/**
	 * add MobEnchant on Entity
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 */
	public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		//Sync Client Enchant
		/*if (!entity.world.isClient()) {
			S2CSyncMobEnchants.createPacket(entity, this.mobEnchants);
		}*/
		//size changed like minecraft dungeons
		//entity.refreshDimensions();
	}

	/**
	 * add MobEnchant on Entity From Owner
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 * @param owner        OwnerEntity with a mob Enchant attached to that mob
	 */
	public void addMobEnchantFromOwner(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, LivingEntity owner) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		//Sync Client Enchant
		/*if (!entity.world.isClient()) {
			S2CSyncMobEnchants.createPacket(entity, this.mobEnchants);
		}*/
		//size changed like minecraft dungeons
		//entity.refreshDimensions();
	}

	public void addOwner(LivingEntity entity, @Nullable LivingEntity owner) {
		this.fromOwner = true;
		this.enchantOwner = Optional.ofNullable(owner);

		/*if (!entity.world.isClient()) {
			MobEnchantFromOwnerMessage message = new MobEnchantFromOwnerMessage(entity, owner);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}*/
	}

	public void removeOwner(LivingEntity entity) {
		this.fromOwner = false;
		this.enchantOwner = Optional.empty();
		/*if (!entity.world.isClient()) {
			RemoveMobEnchantOwnerMessage message = new RemoveMobEnchantOwnerMessage(entity);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}*/
	}

	/*
	 * Remove MobEnchant on Entity
	 */
	public void removeAllMobEnchant(LivingEntity entity) {

		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant());
		}
		//Sync Client Enchant
		/*if (!entity.world.isClient()) {
			S2CSyncMobEnchants.createPacket(entity, this.mobEnchants);
		}*/
		this.mobEnchants.removeAll(mobEnchants);
		//size changed like minecraft dungeons
		//entity.refreshDimensions();
	}

	/*
	 * Remove MobEnchant on Entity from owner
	 */
	public void removeMobEnchantFromOwner(LivingEntity entity) {
		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant());
		}
		//Sync Client Enchant
		/*if (!entity.world.isClient()) {
			S2CSyncMobEnchants.createPacket(entity, this.mobEnchants);
		}*/
		this.mobEnchants.removeAll(mobEnchants);
		this.removeOwner(entity);
		//size changed like minecraft dungeons
		//entity.refreshDimensions();
	}


	/*
	 * Add Enchant Attribute
	 */
	public void onNewEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
		if (!entity.world.isClient()) {
			enchant.onApplied(entity, entity.getAttributes(), enchantLevel);

			/*if (EnchantConfig.COMMON.dungeonsLikeHealth.get()) {
				AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
				if (modifiableattributeinstance != null && !modifiableattributeinstance.hasModifier(HEALTH_MODIFIER)) {
					modifiableattributeinstance.removeModifier(HEALTH_MODIFIER);
					modifiableattributeinstance.addPermanentModifier(HEALTH_MODIFIER);
					entity.setHealth(entity.getHealth() * 1.25F);
				}
			}*/
		}
	}

	/*
	 * Changed Enchant Attribute When Enchant is Changed
	 */
	protected void onChangedEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
		if (!entity.world.isClient()) {
			enchant.onApplied(entity, entity.getAttributes(), enchantLevel);
		}
	}

	/*
	 * Remove Enchant Attribute effect
	 */
	protected void onRemoveEnchantEffect(LivingEntity entity, MobEnchant enchant) {
		if (!entity.world.isClient()) {
			enchant.onRemoved(entity.getAttributes());

			EntityAttributeInstance modifiableattributeinstance = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
			if (modifiableattributeinstance != null) {
				if (modifiableattributeinstance.hasModifier(HEALTH_MODIFIER)) {
					entity.setHealth(entity.getHealth() / 1.5F);
					modifiableattributeinstance.removeModifier(HEALTH_MODIFIER);
				}
			}
		}
	}

	public List<MobEnchantHandler> getMobEnchants() {
		return mobEnchants;
	}

	public boolean hasEnchant() {
		return !this.mobEnchants.isEmpty();
	}

	public Optional<LivingEntity> getEnchantOwner() {
		return enchantOwner;
	}

	public boolean hasOwner() {
		return this.enchantOwner.isPresent() && this.enchantOwner.get().isAlive();
	}

	//check this enchant from owner
	public boolean isFromOwner() {
		return this.fromOwner;
	}

	public NbtCompound serializeNBT() {
		NbtCompound nbt = new NbtCompound();

		NbtList listnbt = new NbtList();

		for (int i = 0; i < mobEnchants.size(); i++) {
			listnbt.add(mobEnchants.get(i).writeNBT());
		}

		nbt.put("StoredMobEnchants", listnbt);
		nbt.putBoolean("FromOwner", fromOwner);


		return nbt;
	}

	public void deserializeNBT(NbtCompound nbt) {
		NbtList list = MobEnchantUtils.getEnchantmentListForNBT(nbt);

		mobEnchants.clear();

		for (int i = 0; i < list.size(); ++i) {
			NbtCompound compoundnbt = list.getCompound(i);

			MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
			//check mob enchant is not null
			if (mobEnchant != null) {
				mobEnchants.add(new MobEnchantHandler(mobEnchant, MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt)));
			}
		}

		fromOwner = nbt.getBoolean("FromOwner");
	}
}
