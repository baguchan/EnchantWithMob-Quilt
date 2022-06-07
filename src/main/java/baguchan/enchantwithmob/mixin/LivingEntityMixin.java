package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.registry.ModTrackedDatas;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IEnchantCap {
	private static final TrackedData<MobEnchantCapability> MOB_ENCHANT_CAP;

	public final MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();

	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initDataTracker(CallbackInfo callbackInfo) {
		this.dataTracker.startTracking(MOB_ENCHANT_CAP, new MobEnchantCapability());
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void onWriteCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.put("MobEnchantData", this.getEnchantCap().serializeNBT());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		this.getEnchantCap().deserializeNBT(nbt.getCompound("MobEnchantData"));
	}

	@Override
	public MobEnchantCapability getEnchantCap() {
		return this.dataTracker.get(MOB_ENCHANT_CAP);
	}

	@Override
	public void setEnchantCap(MobEnchantCapability cap) {
		this.dataTracker.set(MOB_ENCHANT_CAP, cap);
	}

	static {
		MOB_ENCHANT_CAP = DataTracker.registerData(LivingEntity.class, ModTrackedDatas.MOB_ENCHANT_CAPABILITY);
	}
}
