package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.registry.ModTrackedDatas;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IEnchantCap {
	private static final EntityDataAccessor<MobEnchantCapability> MOB_ENCHANT_CAP;

	public final MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();

	public LivingEntityMixin(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	public void defineSynchedData(CallbackInfo callbackInfo) {
		this.entityData.define(MOB_ENCHANT_CAP, new MobEnchantCapability());
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		nbt.put("MobEnchantData", this.getEnchantCap().serializeNBT());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		this.getEnchantCap().deserializeNBT(nbt.getCompound("MobEnchantData"));
	}

	@Override
	public MobEnchantCapability getEnchantCap() {
		return this.entityData.get(MOB_ENCHANT_CAP);
	}

	@Override
	public void setEnchantCap(MobEnchantCapability cap) {
		this.entityData.set(MOB_ENCHANT_CAP, cap);
	}

	@Inject(method = "actuallyHurt", at = @At("HEAD"))
	protected void actuallyHurt(DamageSource source, float amount, CallbackInfo ci) {
		amount = getDamageAfterMobEnchantAbsorb(source, amount);
	}

	protected float getDamageAfterMobEnchantAbsorb(DamageSource source, float damage) {
		if (source != DamageSource.STARVE) {
			int i = MobEnchantUtils.getMobEnchantLevelFromHandler(this.getEnchantCap().getMobEnchants(), MobEnchants.PROTECTION);
			if (i > 0) {
				damage -= (double) Mth.floor(damage * (double) ((float) i * 0.15F));
			}
		}
		return damage;
	}

	static {
		MOB_ENCHANT_CAP = SynchedEntityData.defineId(LivingEntity.class, ModTrackedDatas.MOB_ENCHANT_CAPABILITY);
	}
}
