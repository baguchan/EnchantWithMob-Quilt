package baguchan.enchantwithmob.item;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.ModRegistry;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class MobEnchantBookItem extends Item {
	public MobEnchantBookItem(Item.Settings group) {
		super(group);
	}


	/*
	 * Implemented onRightClick (method) inside CommonEventHandler instead of this method
	 */
    /*@Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (MobEnchantUtils.hasMobEnchant(stack)) {
            target.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
            {
                MobEnchantUtils.addMobEnchantToEntityFromItem(stack, target, cap);
            });
            user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

            stack.damageItem(1, user, (entity) -> entity.sendBreakAnimation(hand));

            return ActionResultType.SUCCESS;
        }

        return super.itemInteractionForEntity(stack, user, target, hand);
    }*/

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (MobEnchantUtils.hasMobEnchant(stack)) {
			final boolean[] flag = {false};
			if (user instanceof IEnchantCap) {
				flag[0] = MobEnchantUtils.addItemMobEnchantToEntity(stack, user, ((IEnchantCap) user).getEnchantCap());
			}

			//When flag is true, enchanting is success.
			if (flag[0]) {
				user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

				stack.damage(1, user, (entity) -> entity.sendToolBreakStatus(hand));

				user.getItemCooldownManager().set(stack.getItem(), 40);

				return TypedActionResult.success(stack);
			} else {
				user.sendMessage(new TranslatableText("enchantwithmob.cannot.enchant_yourself"), true);

				user.getItemCooldownManager().set(stack.getItem(), 20);

				return TypedActionResult.fail(stack);
			}
		}
		return super.use(world, user, hand);
	}


	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			for (Identifier identifier : ModRegistry.MOB_ENCHANT.getIds()) {
				MobEnchant enchant = ModRegistry.MOB_ENCHANT.get(identifier);
				ItemStack stack = new ItemStack(this);
				MobEnchantUtils.addMobEnchantToItemStack(stack, enchant, enchant.getMaxLevel());
				stacks.add(stack);
			}
		}
	}

	public static NbtList getEnchantmentList(ItemStack stack) {
		NbtCompound compoundnbt = stack.getNbt();
		return compoundnbt != null ? compoundnbt.getList(MobEnchantUtils.TAG_STORED_MOBENCHANTS, 10) : new NbtList();
	}

	public void appendTooltip(ItemStack stack, @org.jetbrains.annotations.Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		Formatting[] textformatting2 = new Formatting[]{Formatting.DARK_PURPLE};

		tooltip.add(new TranslatableText("mobenchant.enchantwithmob.mob_enchant_book.tooltip").formatted(textformatting2));
		if (MobEnchantUtils.hasMobEnchant(stack)) {
			NbtList listnbt = MobEnchantUtils.getEnchantmentListForNBT(stack.getNbt());

			for (int i = 0; i < listnbt.size(); ++i) {
				NbtCompound compoundnbt = listnbt.getCompound(i);

				MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
				int enchantmentLevel = MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt);

				if (mobEnchant != null) {
					Formatting[] textformatting = new Formatting[]{Formatting.AQUA};
					Identifier identifier = ModRegistry.MOB_ENCHANT.getId(mobEnchant);

					tooltip.add(new TranslatableText("mobenchant." + identifier.getNamespace() + "." + identifier.getPath()).formatted(textformatting).append(" ").append(new TranslatableText("enchantment.level." + enchantmentLevel).formatted(textformatting)));
				}
			}

			List<Pair<EntityAttribute, EntityAttributeModifier>> list1 = Lists.newArrayList();

			for (int i = 0; i < listnbt.size(); ++i) {
				NbtCompound compoundnbt = listnbt.getCompound(i);

				MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
				int mobEnchantLevel = MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt);

				if (mobEnchant != null) {
					Map<EntityAttribute, EntityAttributeModifier> map = mobEnchant.getAttributeModifiers();
					if (!map.isEmpty()) {
						for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
							EntityAttributeModifier attributemodifier = entry.getValue();
							EntityAttributeModifier attributemodifier1 = new EntityAttributeModifier(attributemodifier.getName(), mobEnchant.adjustModifierAmount(mobEnchantLevel, attributemodifier), attributemodifier.getOperation());
							list1.add(new Pair<>(entry.getKey(), attributemodifier1));
						}
					}
				}
			}


			if (!list1.isEmpty()) {
				//tooltip.add(StringTextComponent.EMPTY);
				tooltip.add((new TranslatableText("mobenchant.enchantwithmob.when_ehcnanted")).formatted(Formatting.DARK_PURPLE));

				for (Pair<EntityAttribute, EntityAttributeModifier> pair : list1) {
					EntityAttributeModifier attributemodifier2 = pair.getSecond();
					double d0 = attributemodifier2.getValue();
					double d1;
					if (attributemodifier2.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
						d1 = attributemodifier2.getValue();
					} else {
						d1 = attributemodifier2.getValue() * 100.0D;
					}

					if (d0 > 0.0D) {
						tooltip.add((new TranslatableText("attribute.modifier.plus." + attributemodifier2.getName(), ItemStack.MODIFIER_FORMAT.format(d1), new TranslatableText(pair.getFirst().getTranslationKey()))).formatted(Formatting.BLUE));
					} else if (d0 < 0.0D) {
						d1 = d1 * -1.0D;
						tooltip.add((new TranslatableText("attribute.modifier.take." + attributemodifier2.getName(), ItemStack.MODIFIER_FORMAT.format(d1), new TranslatableText(pair.getFirst().getTranslationKey()))).formatted(Formatting.RED));
					}
				}
			}
		}
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}
