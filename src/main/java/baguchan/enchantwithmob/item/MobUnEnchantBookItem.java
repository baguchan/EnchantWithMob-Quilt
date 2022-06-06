package baguchan.enchantwithmob.item;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class MobUnEnchantBookItem extends Item {
	public MobUnEnchantBookItem(Settings group) {
		super(group);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (user instanceof IEnchantCap) {
			MobEnchantUtils.removeMobEnchantToEntity(user, ((IEnchantCap) user).getEnchantCap());
		}
		user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

		stack.damage(1, user, (entity) -> entity.sendToolBreakStatus(hand));

		user.getItemCooldownManager().set(stack.getItem(), 80);

		return TypedActionResult.success(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, @org.jetbrains.annotations.Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		Formatting[] textformatting2 = new Formatting[]{Formatting.DARK_PURPLE};

		tooltip.add(new TranslatableText("mobenchant.enchantwithmob.mob_unenchant_book.tooltip").formatted(textformatting2));

	}


	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}
