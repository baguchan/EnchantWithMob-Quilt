package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.capability.MobEnchantCapability;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class ModTrackedDatas {
	public static final TrackedDataHandler<MobEnchantCapability> MOB_ENCHANT_CAPABILITY = new TrackedDataHandler<MobEnchantCapability>() {
		public void write(PacketByteBuf packetByteBuf, MobEnchantCapability nbtCompound) {
			packetByteBuf.writeNbt(nbtCompound.serializeNBT());
		}

		public MobEnchantCapability read(PacketByteBuf packetByteBuf) {
			MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();
			mobEnchantCapability.deserializeNBT(packetByteBuf.readNbt());
			return mobEnchantCapability;
		}

		public MobEnchantCapability copy(MobEnchantCapability mobEnchantCapability) {
			return mobEnchantCapability;
		}
	};

	public static void init() {
		TrackedDataHandlerRegistry.register(MOB_ENCHANT_CAPABILITY);
	}
}
