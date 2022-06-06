package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.util.collection.Weighted;

public class MobEnchantmentData extends Weighted.Absent {
	public final MobEnchant enchantment;
	public final int enchantmentLevel;

	public MobEnchantmentData(MobEnchant enchantmentObj, int enchLevel) {
		super(enchantmentObj.getRarity().getWeight());
		this.enchantment = enchantmentObj;
		this.enchantmentLevel = enchLevel;
	}
}
