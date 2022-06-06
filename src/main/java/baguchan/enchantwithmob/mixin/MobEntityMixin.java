package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements IEnchantCap {
	public final MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initialize", at = @At("HEAD"))
	public void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
		this.spawnEntityMobEnchanted(world, difficulty, spawnReason);
	}

	public void spawnEntityMobEnchanted(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason) {
		// On add MobEnchant Alway Enchantable Mob

		if (isSpawnEnchantableEntity(this)) {
			if (spawnReason != SpawnReason.BREEDING && spawnReason != SpawnReason.CONVERSION && spawnReason != SpawnReason.STRUCTURE) {
				if (!world.isClient()) {
					int i = 0;
					float difficultScale = world.getLocalDifficulty(this.getBlockPos()).getLocalDifficulty() - 0.2F;
					switch (world.getDifficulty()) {
						case EASY:
							i = (int) MathHelper.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

							MobEnchantUtils.addRandomEnchantmentToEntity(this, this.getEnchantCap(), world.getRandom(), i, true);
							break;
						case NORMAL:
							i = (int) MathHelper.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

							MobEnchantUtils.addRandomEnchantmentToEntity(this, this.getEnchantCap(), world.getRandom(), i, true);
							break;
						case HARD:
							i = (int) MathHelper.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

							MobEnchantUtils.addRandomEnchantmentToEntity(this, this.getEnchantCap(), world.getRandom(), i, true);
							break;
					}

					this.setHealth(this.getMaxHealth());
				}

			}
		}

	}

	private static boolean isSpawnEnchantableEntity(Entity entity) {
		return !(entity instanceof PlayerEntity) && !(entity instanceof ArmorStandEntity) && !(entity instanceof BoatEntity) && !(entity instanceof MinecartEntity);
	}
}
