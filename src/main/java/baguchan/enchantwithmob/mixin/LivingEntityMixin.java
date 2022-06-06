package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.network.S2CSyncMobEnchants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IEnchantCap {
	public final MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();

	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	public void onWriteCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.put("MobEnchantData", this.getEnchantCap().serializeNBT());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	public void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		this.getEnchantCap().deserializeNBT(nbt.getCompound("MobEnchantData"));
		S2CSyncMobEnchants.createPacket(this, this.getEnchantCap().getMobEnchants());
	}

	@Override
	public MobEnchantCapability getEnchantCap() {
		return mobEnchantCapability;
	}
}
