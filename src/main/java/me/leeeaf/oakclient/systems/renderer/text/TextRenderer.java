package me.leeeaf.oakclient.systems.renderer.text;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Matrix4f;

import static me.leeeaf.oakclient.OakClient.mc;

public class TextRenderer {

    public  static  void  drawLabelOnEntity(String label, Formatting color, float height, Entity entity, MatrixStack matrices,
                                            int light, VertexConsumerProvider vertexConsumers, EntityRenderDispatcher dispatcher){
        Text text = Text.literal(label).formatted(color);
        boolean bl = !entity.isSneaky();
        float f = entity.getHeight() + height;
        matrices.push();
        matrices.translate(0.0, (double)f, 0.0);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float h = (float)(-mc.textRenderer.getWidth(text) / 2);
        float g = mc.options.getTextBackgroundOpacity(0.25F);
        int j = (int)(g * 255.0F) << 24;
        mc.textRenderer.draw(text, h, 0, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
        mc.textRenderer.draw(text, h, 0, -1, false, matrix4f, vertexConsumers, false, 0, light);
        matrices.pop();
    }

}
