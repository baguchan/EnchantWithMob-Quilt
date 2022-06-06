package baguchan.enchantwithmob.mixin.client;

import baguchan.enchantwithmob.client.layer.EnchantLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
	@Shadow
	@Final
	protected List<FeatureRenderer<T, M>> features;

	protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@Shadow
	@Final
	protected boolean addFeature(FeatureRenderer<T, M> feature) {
		return this.features.add(feature);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void onInit(EntityRendererFactory.Context context, M entityModel, float f, CallbackInfo ci) {
		addFeature(new EnchantLayer(this));
	}

}
