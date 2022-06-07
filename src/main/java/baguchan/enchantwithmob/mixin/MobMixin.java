package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements IEnchantCap {
	public final MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();

	protected MobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "finalizeSpawn", at = @At("HEAD"))
	public void onInitialize(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt, CallbackInfoReturnable<SpawnGroupData> cir) {
		this.spawnEntityMobEnchanted(world, difficulty, spawnReason);
	}

	public void spawnEntityMobEnchanted(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason) {
		// On add MobEnchant Alway Enchantable Mob

		if (isSpawnEnchantableEntity(this)) {
			if (spawnReason != MobSpawnType.BREEDING && spawnReason != MobSpawnType.CONVERSION && spawnReason != MobSpawnType.STRUCTURE) {
				if (world.getRandom().nextFloat() < (0.005F * world.getDifficulty().getId()) + world.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty() * 0.025F) {
					if (!world.isClientSide()) {
						int i = 0;
						float difficultScale = world.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty() - 0.2F;
						switch (world.getDifficulty()) {
							case EASY:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

								MobEnchantUtils.addRandomEnchantmentToEntity(this, this.getEnchantCap(), world.getRandom(), i, true);
								break;
							case NORMAL:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

								MobEnchantUtils.addRandomEnchantmentToEntity(this, this.getEnchantCap(), world.getRandom(), i, true);
								break;
							case HARD:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

								MobEnchantUtils.addRandomEnchantmentToEntity(this, this.getEnchantCap(), world.getRandom(), i, true);
								break;
						}

						this.setHealth(this.getMaxHealth());
					}
				}

			}
		}

	}

	private static boolean isSpawnEnchantableEntity(Entity entity) {
		return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart);
	}
}
