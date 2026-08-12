package net.awyvrix.genetics.content.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static net.awyvrix.genetics.Genetics.MOD_ID;

public class EarsModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ears"), "main");
	private final ModelPart bone;

	public EarsModel(ModelPart root) {
		this.bone = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.75F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 5).addBox(-1.0F, -5.75F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-2.0F, -4.75F, 0.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 9).addBox(2.0F, -2.75F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(2, 9).addBox(-3.0F, -2.75F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.75F, 0.25F, 0.5F, -0.0394F, -0.2729F, -0.1806F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 9).addBox(-3.0F, -2.75F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 9).addBox(2.0F, -2.75F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-2.0F, -4.75F, 0.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 5).addBox(-1.0F, -5.75F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.0F, -3.75F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.75F, 0.25F, 0.5F, -0.0394F, 0.2729F, 0.1806F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}