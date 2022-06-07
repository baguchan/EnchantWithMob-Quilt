package baguchan.enchantwithmob;

import net.fabricmc.api.DedicatedServerModInitializer;

public class EnchantWithMobServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		/*ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if(entity instanceof IEnchantCap) {
				S2CSyncMobEnchants.createPacket(entity, ((IEnchantCap) entity).getEnchantCap().getMobEnchants());
			}
		});*/
	}
}
