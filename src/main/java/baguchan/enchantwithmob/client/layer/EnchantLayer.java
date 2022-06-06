package baguchan.enchantwithmob.client.layer;

import baguchan.enchantwithmob.api.IEnchantCap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class EnchantLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	public EnchantLayer(FeatureRendererContext<T, M> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity instanceof IEnchantCap && ((IEnchantCap) entity).getEnchantCap().hasEnchant()) {
			float f = (float) entity.age + tickDelta;
			EntityModel<T> entityModel = this.getContextModel();
			entityModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
			this.getContextModel().copyStateTo(entityModel);
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityGlint());
			entityModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0.5F, 0.5F, 0.5F, 1.0F);
		}
	}
}
