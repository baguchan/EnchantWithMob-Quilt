package baguchan.enchantwithmob.network;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.registry.ModRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.ArrayList;
import java.util.List;

public class S2CSyncMobEnchants {
	public static final Identifier ID = new Identifier(EnchantWithMob.MODID, "sync_mob_enchants");

	public static Packet<?> createPacket(Entity mobEntity, List<MobEnchantHandler> mobenchant) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(mobEntity.getId());
		buf.writeShort(mobenchant.size());
		for (MobEnchantHandler mobEnchant : mobenchant) {
			buf.writeIdentifier(ModRegistry.MOB_ENCHANT.getId(mobEnchant.getMobEnchant()));
			buf.writeInt(mobEnchant.getEnchantLevel());
		}
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) {
		int id = buffer.readInt();

		int size = buffer.readShort();
		List<MobEnchantHandler> mobenchant = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			mobenchant.add(new MobEnchantHandler(ModRegistry.MOB_ENCHANT.get(buffer.readIdentifier()), buffer.readInt()));
		}
		client.execute(() -> {
			if (client.world != null) {
				Entity entity = client.world.getEntityById(id);

				List<MobEnchantHandler> list = ((IEnchantCap) entity).getEnchantCap().getMobEnchants();
				list.clear();
				list.addAll(mobenchant);
			}
		});
	}
}
