package baguchan.enchantwithmob.capability;


import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.ModRegistry;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.nbt.NbtCompound;

public class MobEnchantHandler {
	private MobEnchant mobEnchant;
	private int enchantLevel;

	public MobEnchantHandler(MobEnchant mobEnchant, int enchantLevel) {
		this.mobEnchant = mobEnchant;
		this.enchantLevel = enchantLevel;
	}


	public MobEnchant getMobEnchant() {
		return mobEnchant;
	}

	public int getEnchantLevel() {
		return enchantLevel;
	}

	public NbtCompound writeNBT() {
		NbtCompound nbt = new NbtCompound();

		if (mobEnchant != null) {
			nbt.putString("MobEnchant", ModRegistry.MOB_ENCHANT.getId(mobEnchant).toString());
			nbt.putInt("EnchantLevel", enchantLevel);
		}

		return nbt;
	}

	public void readNBT(NbtCompound nbt) {
		mobEnchant = MobEnchantUtils.getEnchantFromNBT(nbt);
		enchantLevel = MobEnchantUtils.getEnchantLevelFromNBT(nbt);
	}
}
