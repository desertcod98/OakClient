package me.leeeaf.oakclient.systems.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import me.leeeaf.oakclient.systems.renderer.color.LineColor;
import me.leeeaf.oakclient.systems.renderer.color.QuadColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;

public class Renderer {

    public static void drawBoxBoth(BlockPos blockPos, QuadColor color, float lineWidth, Direction... excludeDirs) {
        drawBoxBoth(new Box(blockPos), color, lineWidth, excludeDirs);
    }

    public static void drawBoxBoth(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
        QuadColor outlineColor = color.clone();
        outlineColor.overwriteAlpha(255);

        drawBoxBoth(box, color, outlineColor, lineWidth, excludeDirs);
    }

    public static void drawBoxBoth(BlockPos blockPos, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
        drawBoxBoth(new Box(blockPos), fillColor, outlineColor, lineWidth, excludeDirs);
    }

    public static void drawBoxBoth(Box box, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
        drawBoxFill(box, fillColor, excludeDirs);
        drawBoxOutline(box, outlineColor, lineWidth, excludeDirs);
    }

    // -------------------- Fill Boxes --------------------

    public static void drawBoxFill(BlockPos blockPos, QuadColor color, Direction... excludeDirs) {
        drawBoxFill(new Box(blockPos), color, excludeDirs);
    }

    public static void drawBoxFill(Box box, QuadColor color, Direction... excludeDirs) {

        setup();

        MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Fill
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Vertexer.vertexBoxQuads(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
        tessellator.draw();

        cleanup();
    }

    // -------------------- Outline Boxes --------------------

    public static void drawBoxOutline(BlockPos blockPos, QuadColor color, float lineWidth, Direction... excludeDirs) {
        drawBoxOutline(new Box(blockPos), color, lineWidth, excludeDirs);
    }

    public static void drawBoxOutline(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {

        setup();

        MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Outline
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
        RenderSystem.lineWidth(lineWidth);

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Vertexer.vertexBoxLines(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
        tessellator.draw();

        RenderSystem.enableCull();

        cleanup();
    }

    // -------------------- Quads --------------------

    public static void drawQuadFill(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, int cullMode, QuadColor color) {

        setup();

        MatrixStack matrices = matrixFrom(x1, y1, z1);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Fill
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Vertexer.vertexQuad(matrices, buffer,
                0f, 0f, 0f,
                (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1),
                (float) (x3 - x1), (float) (y3 - y1), (float) (z3 - z1),
                (float) (x4 - x1), (float) (y4 - y1), (float) (z4 - z1),
                cullMode, color);
        tessellator.draw();

        cleanup();
    }

    public static void drawQuadOutline(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, float lineWidth, QuadColor color) {

        setup();

        MatrixStack matrices = matrixFrom(x1, y1, z1);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        int[] colors = color.getAllColors();

        // Outline
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
        RenderSystem.lineWidth(lineWidth);

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Vertexer.vertexLine(matrices, buffer, 0f, 0f, 0f, (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1), LineColor.gradient(colors[0], colors[1]));
        Vertexer.vertexLine(matrices, buffer, (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1), (float) (x3 - x1), (float) (y3 - y1), (float) (z3 - z1), LineColor.gradient(colors[1], colors[2]));
        Vertexer.vertexLine(matrices, buffer, (float) (x3 - x1), (float) (y3 - y1), (float) (z3 - z1), (float) (x4 - x1), (float) (y4 - y1), (float) (z4 - z1), LineColor.gradient(colors[2], colors[3]));
        Vertexer.vertexLine(matrices, buffer, (float) (x4 - x1), (float) (y4 - y1), (float) (z4 - z1), 0f, 0f, 0f, LineColor.gradient(colors[3], colors[0]));
        tessellator.draw();

        RenderSystem.enableCull();
        cleanup();
    }

    // -------------------- Lines --------------------

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, LineColor color, float width) {

        setup();

        MatrixStack matrices = matrixFrom(x1, y1, z1);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Line
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
        RenderSystem.lineWidth(width);

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Vertexer.vertexLine(matrices, buffer, 0f, 0f, 0f, (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1), color);
        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        cleanup();
    }

    // -------------------- Utils --------------------

    public static MatrixStack matrixFrom(double x, double y, double z) {
        MatrixStack matrices = new MatrixStack();

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));

        matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

        return matrices;
    }

    public static Vec3d getInterpolationOffset(Entity e) {
        if (MinecraftClient.getInstance().isPaused()) {
            return Vec3d.ZERO;
        }

        double tickDelta = MinecraftClient.getInstance().getTickDelta();
        return new Vec3d(
                e.getX() - MathHelper.lerp(tickDelta, e.lastRenderX, e.getX()),
                e.getY() - MathHelper.lerp(tickDelta, e.lastRenderY, e.getY()),
                e.getZ() - MathHelper.lerp(tickDelta, e.lastRenderZ, e.getZ()));
    }

    public static void setup() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
    }

    public static void cleanup() {
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
}