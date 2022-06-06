package baguchan.enchantwithmob.mobenchant;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GuardianEntity;

public class ThornEnchant extends MobEnchant {
	public ThornEnchant(Properties properties) {
		super(properties);
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 10 + 20 * (enchantmentLevel - 1);
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean isCompatibleMob(LivingEntity livingEntity) {
		return !(livingEntity instanceof GuardianEntity);
	}
}
