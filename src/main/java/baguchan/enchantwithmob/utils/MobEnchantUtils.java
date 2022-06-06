package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.ModItems;
import baguchan.enchantwithmob.registry.ModRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MobEnchantUtils {
	public static final String TAG_MOBENCHANT = "MobEnchant";
	public static final String TAG_ENCHANT_LEVEL = "EnchantLevel";
	public static final String TAG_STORED_MOBENCHANTS = "StoredMobEnchants";

/*	//when projectile Shooter has mob enchant, start Runnable
	public static void executeIfPresent(LivingEntity entity, MobEnchant mobEnchantment, Runnable runnable) {
		if (entity != null) {
			entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
				if (MobEnchantUtils.findMobEnchantFromHandler(cap.getModRegistry(), mobEnchantment)) {
					runnable.run();
				}
			});
		}
	}*/

	/**
	 * get MobEnchant From NBT
	 *
	 * @param tag nbt tag
	 */
	@Nullable
	public static MobEnchant getEnchantFromNBT(@Nullable NbtCompound tag) {
		if (tag != null && ModRegistry.MOB_ENCHANT.containsId(Identifier.tryParse(tag.getString(TAG_MOBENCHANT)))) {
			return ModRegistry.MOB_ENCHANT.get(Identifier.tryParse(tag.getString(TAG_MOBENCHANT)));
		} else {
			return null;
		}
	}

	/**
	 * get MobEnchant Level From NBT
	 *
	 * @param tag nbt tag
	 */
	public static int getEnchantLevelFromNBT(@Nullable NbtCompound tag) {
		if (tag != null) {
			return tag.getInt(TAG_ENCHANT_LEVEL);
		} else {
			return 0;
		}
	}

	/**
	 * get MobEnchant From String
	 *
	 * @param id MobEnchant id
	 */
	@Nullable
	public static MobEnchant getEnchantFromString(@Nullable String id) {
		if (id != null && ModRegistry.MOB_ENCHANT.containsId(Identifier.tryParse(id))) {
			return ModRegistry.MOB_ENCHANT.get(Identifier.tryParse(id));
		} else {
			return null;
		}
	}

	@Nullable
	public static MobEnchant getEnchantFromIdentifier(@Nullable Identifier id) {
		if (id != null && ModRegistry.MOB_ENCHANT.containsId(id)) {
			return ModRegistry.MOB_ENCHANT.get(id);
		} else {
			return null;
		}
	}

	/**
	 * check ItemStack has Mob Enchant
	 *
	 * @param stack MobEnchanted Item
	 */
	public static boolean hasMobEnchant(ItemStack stack) {
		NbtCompound compoundnbt = stack.getNbt();
		return compoundnbt != null && compoundnbt.contains(TAG_STORED_MOBENCHANTS);
	}

	/**
	 * check NBT has Mob Enchant
	 *
	 * @param compoundnbt nbt tag
	 */
	public static NbtList getEnchantmentListForNBT(NbtCompound compoundnbt) {
		return compoundnbt != null ? compoundnbt.getList(TAG_STORED_MOBENCHANTS, 10) : new NbtList();
	}

	/**
	 * get Mob Enchantments From ItemStack
	 *
	 * @param stack MobEnchanted Item
	 */
	public static Map<MobEnchant, Integer> getEnchantments(ItemStack stack) {
		NbtList listnbt = getEnchantmentListForNBT(stack.getNbt());
		return makeMobEnchantListFromListNBT(listnbt);
	}

	/**
	 * set Mob Enchantments From ItemStack
	 *
	 * @param enchMap ModRegistry and those level map
	 * @param stack   MobEnchanted Item
	 */
	public static void setEnchantments(Map<MobEnchant, Integer> enchMap, ItemStack stack) {
		NbtList listnbt = new NbtList();

		for (Map.Entry<MobEnchant, Integer> entry : enchMap.entrySet()) {
			MobEnchant enchantment = entry.getKey();
			if (enchantment != null) {
				int i = entry.getValue();
				NbtCompound compoundnbt = new NbtCompound();
				compoundnbt.putString(TAG_MOBENCHANT, String.valueOf((Object) ModRegistry.MOB_ENCHANT.getKey(enchantment)));
				compoundnbt.putShort(TAG_ENCHANT_LEVEL, (short) i);
				listnbt.add(compoundnbt);
				if (stack.getItem() == ModItems.MOB_ENCHANT_BOOK) {
					addMobEnchantToItemStack(stack, enchantment, i);
				}
			}
		}

		if (listnbt.isEmpty()) {
			stack.removeSubNbt(TAG_STORED_MOBENCHANTS);
		}
	}

	private static Map<MobEnchant, Integer> makeMobEnchantListFromListNBT(NbtList p_226652_0_) {
		Map<MobEnchant, Integer> map = Maps.newLinkedHashMap();

		for (int i = 0; i < p_226652_0_.size(); ++i) {
			NbtCompound compoundnbt = p_226652_0_.getCompound(i);
			MobEnchant mobEnchant = getEnchantFromString(compoundnbt.getString(TAG_MOBENCHANT));
			map.put(mobEnchant, compoundnbt.getInt(TAG_ENCHANT_LEVEL));

		}

		return map;
	}

	//add MobEnchantToItemstack (example,this method used to MobEnchantBook)
	public static void addMobEnchantToItemStack(ItemStack itemIn, MobEnchant mobenchant, int level) {
		NbtList listnbt = getEnchantmentListForNBT(itemIn.getNbt());

		boolean flag = true;
		Identifier resourcelocation = ModRegistry.MOB_ENCHANT.getKey(mobenchant).get().getValue();


		for (int i = 0; i < listnbt.size(); ++i) {
			NbtCompound compoundnbt = listnbt.getCompound(i);
			Identifier resourcelocation1 = Identifier.tryParse(compoundnbt.getString("MobEnchant"));
			if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
				if (compoundnbt.getInt(TAG_ENCHANT_LEVEL) < level) {
					compoundnbt.putInt(TAG_ENCHANT_LEVEL, level);
				}

				flag = false;
				break;
			}
		}

		if (flag) {
			NbtCompound compoundnbt1 = new NbtCompound();
			compoundnbt1.putString(TAG_MOBENCHANT, String.valueOf((Object) resourcelocation));
			compoundnbt1.putInt(TAG_ENCHANT_LEVEL, level);
			listnbt.add(compoundnbt1);
		}

		itemIn.getNbt().put(TAG_STORED_MOBENCHANTS, listnbt);
	}

	/**
	 * add Mob Enchantments From ItemStack
	 *
	 * @param itemIn     MobEnchanted Item
	 * @param entity     Enchanting target
	 * @param capability MobEnchant Capability
	 */
	public static boolean addItemMobEnchantToEntity(ItemStack itemIn, LivingEntity entity, MobEnchantCapability capability) {
		NbtList listnbt = getEnchantmentListForNBT(itemIn.getNbt());
		boolean flag = false;

		for (int i = 0; i < listnbt.size(); ++i) {
			NbtCompound compoundnbt = listnbt.getCompound(i);
			if (checkAllowMobEnchantFromMob(MobEnchantUtils.getEnchantFromNBT(compoundnbt), entity, capability)) {
				capability.addMobEnchant(entity, MobEnchantUtils.getEnchantFromNBT(compoundnbt), MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt));
				flag = true;
			}
		}
		return flag;
	}

	public static void removeMobEnchantToEntity(LivingEntity entity, MobEnchantCapability capability) {
		capability.removeAllMobEnchant(entity);
	}

	public static int getExperienceFromMob(MobEnchantCapability cap) {
		int l = 0;
		for (MobEnchantHandler list : cap.getMobEnchants()) {
			MobEnchant enchantment = list.getMobEnchant();
			int integer = list.getEnchantLevel();
			l += enchantment.getMinEnchantability(integer);
		}
		return l;
	}

	/**
	 * add Mob Enchantments To Entity
	 *
	 * @param livingEntity Enchanting target
	 * @param capability   MobEnchant Capability
	 * @param random       Random
	 * @param level        max limit level MobEnchant
	 * @param allowRare    setting is allow rare enchant
	 */
	public static boolean addRandomEnchantmentToEntity(LivingEntity livingEntity, MobEnchantCapability capability, Random random, int level, boolean allowRare) {
		List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare);

		boolean flag = false;
		for (MobEnchantmentData enchantmentdata : list) {
			if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
				capability.addMobEnchant(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * add Mob Enchantments To Entity(but unstable enchant)
	 *
	 * @param livingEntity Enchanting target
	 * @param capability   MobEnchant Capability
	 * @param random       Random
	 * @param level        max limit level MobEnchant
	 * @param allowRare    setting is allow rare enchant
	 */
	public static boolean addUnstableRandomEnchantmentToEntity(LivingEntity livingEntity, LivingEntity ownerEntity, MobEnchantCapability capability, Random random, int level, boolean allowRare) {
		List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare);

		boolean flag = false;

		for (MobEnchantmentData enchantmentdata : list) {
			if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
				capability.addMobEnchantFromOwner(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel, ownerEntity);
				flag = true;
			}
		}
		if (flag) {
			capability.addOwner(livingEntity, ownerEntity);
		}
		return flag;
	}

	public static ItemStack addRandomEnchantmentToItemStack(Random random, ItemStack stack, int level, boolean allowRare) {
		List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare);

		for (MobEnchantmentData enchantmentdata : list) {
			addMobEnchantToItemStack(stack, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
		}

		return stack;
	}

	public static boolean findMobEnchantHandler(List<MobEnchantHandler> list, MobEnchant findMobEnchant) {
		for (MobEnchantHandler mobEnchant : list) {
			if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
				return true;
			}
		}
		return false;
	}

	public static boolean findMobEnchant(List<MobEnchant> list, MobEnchant findMobEnchant) {
		if (list.contains(findMobEnchant)) {
			return true;
		}
		return false;
	}

	public static boolean findMobEnchantFromHandler(List<MobEnchantHandler> list, MobEnchant findMobEnchant) {
		for (MobEnchantHandler mobEnchant : list) {
			if (mobEnchant != null) {
				if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkAllowMobEnchantFromMob(@Nullable MobEnchant mobEnchant, LivingEntity livingEntity, MobEnchantCapability capability) {
		if (mobEnchant != null && !mobEnchant.isCompatibleMob(livingEntity)) {
			return false;
		}

		for (MobEnchantHandler enchantHandler : capability.getMobEnchants()) {
			if (mobEnchant != null && enchantHandler.getMobEnchant() != null && !enchantHandler.getMobEnchant().isCompatibleWith(mobEnchant)) {
				return false;
			}
		}

		//check mob enchant is not null
		return mobEnchant != null;
	}

	public static int getMobEnchantLevelFromHandler(List<MobEnchantHandler> list, MobEnchant findMobEnchant) {
		for (MobEnchantHandler mobEnchant : list) {
			if (mobEnchant != null) {
				if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
					return mobEnchant.getEnchantLevel();
				}
			}
		}
		return 0;
	}

	/*
	 * build MobEnchantment list like vanilla's enchantment
	 */
	public static List<MobEnchantmentData> buildEnchantmentList(Random randomIn, int level, boolean allowTresure) {
		List<MobEnchantmentData> list = Lists.newArrayList();
		int i = 1; //Enchantability
		if (i <= 0) {
			return list;
		} else {
			level = level + 1 + randomIn.nextInt(i / 4 + 1) + randomIn.nextInt(i / 4 + 1);
			float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
			level = MathHelper.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE);
			List<MobEnchantmentData> list1 = makeMobEnchantmentDatas(level, allowTresure);
			if (!list1.isEmpty()) {
				Weighting.getRandom(randomIn, list1).ifPresent(list::add);

				while (randomIn.nextInt(50) <= level) {
					if (!list.isEmpty()) {
						removeIncompatible(list1, Util.getLast(list));
					}
					if (list1.isEmpty()) {
						break;
					}

					Weighting.getRandom(randomIn, list1).ifPresent(list::add);
					level /= 2;
				}
			}

			return list;
		}
	}

	/*
	 * get MobEnchantment data.
	 * when not allow rare enchantment,Ignore rare enchantment
	 */
	public static List<MobEnchantmentData> makeMobEnchantmentDatas(int p_185291_0_, boolean allowTresure) {
		List<MobEnchantmentData> list = Lists.newArrayList();

		for (Identifier identifier : ModRegistry.MOB_ENCHANT.getIds()) {
			MobEnchant enchantment = ModRegistry.MOB_ENCHANT.get(identifier);
			if ((!enchantment.isTresureEnchant() || allowTresure) && !enchantment.isOnlyChest()) {
				for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
					if (p_185291_0_ >= enchantment.getMinEnchantability(i) && p_185291_0_ <= enchantment.getMaxEnchantability(i)) {
						list.add(new MobEnchantmentData(enchantment, i));
						break;
					}
				}
			}
		}

		return list;
	}

	private static void removeIncompatible(List<MobEnchantmentData> dataList, MobEnchantmentData data) {
		Iterator<MobEnchantmentData> iterator = dataList.iterator();

		while (iterator.hasNext()) {
			if (!data.enchantment.isCompatibleWith((iterator.next()).enchantment)) {
				iterator.remove();
			}
		}

	}
}
