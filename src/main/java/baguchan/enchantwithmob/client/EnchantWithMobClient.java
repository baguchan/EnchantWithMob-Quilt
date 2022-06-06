package baguchan.enchantwithmob.client;

import baguchan.enchantwithmob.network.S2CSyncMobEnchants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;


@Environment(EnvType.CLIENT)
public class EnchantWithMobClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(S2CSyncMobEnchants.ID, S2CSyncMobEnchants::onPacket);
	}
}
