/*
 * This class is based on a version from legobmw99's Allomancy mod.
 * Their notice in their version of this file is given below:
 *
 * This class was modified from one created by <Vazkii>. The original is
 * distributed as part of the Psi Mod.
 * This code is used under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * The code was used as a template for the circular GUI,
 * and was heavily modified
 */
package kalia.cosmine.gui;

import kalia.cosmine.Cosmine;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.Allomancy;
import kalia.cosmine.investiture.allomancy.SpiritwebAllomancy;
import kalia.cosmine.utils.DrawUtils;
import kalia.cosmine.utils.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class AllomancyGui extends InvestitureGui {
    private static final int INNER_RING_RADIUS = 50;
    private static final int OUTER_RING_RADIUS = 80;

    private static final Investiture[] ALLOMANCIES = new Investiture[]{
        Allomancy.STEEL, Allomancy.IRON, Allomancy.PEWTER, Allomancy.TIN,
        Allomancy.ZINC, Allomancy.BRASS, Allomancy.COPPER, Allomancy.BRONZE,
        Allomancy.DURALUMIN, Allomancy.ALUMINUM, Allomancy.NICROSIL, Allomancy.CHROMIUM,
        Allomancy.GOLD, Allomancy.ELECTRUM, Allomancy.CADMIUM, Allomancy.BENDALLOY
    };

    private static final ResourceLocation SURROUND = new ResourceLocation(Cosmine.MOD_ID + ":textures/gui/allomancy/surround.png");
    private static final ResourceLocation GAUGE = new ResourceLocation(Cosmine.MOD_ID + ":textures/gui/allomancy/gauge.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();

        int centreX = width / 2;
        int centreY = height / 2;

        float mouseAngle = mouseAngle(centreX, centreY, mouseX, mouseY);

        ArrayList<SpiritwebAllomancy> allomancies = new ArrayList<SpiritwebAllomancy>();
        for (Investiture allomancy : ALLOMANCIES) {
            SpiritwebAllomancy spiritwebAllomancy = (SpiritwebAllomancy)this.spiritweb.getSpiritwebInvestiture(allomancy);
            if (spiritwebAllomancy != null) {
                allomancies.add(spiritwebAllomancy);
            }
        }

        int powerCount = allomancies.size();
        int[][] positions = new int[powerCount][];
        for (int i = 0; i < powerCount; i++) {
            float azimuth = 0;
            float radius = 0;

            if (powerCount >= 8) {
                int innerCount = powerCount / 2;
                int outerCount = powerCount - innerCount;
                if (i < innerCount) {
                    azimuth = i * (360f / innerCount) * MathUtils.DEGTORAD;
                }
                else {
                    azimuth = ((i - innerCount) + 0.5f) * (360f / outerCount) * MathUtils.DEGTORAD;
                }
                radius = i < innerCount ? INNER_RING_RADIUS : OUTER_RING_RADIUS;
            }
            else {
                azimuth = i * (360f / powerCount) * MathUtils.DEGTORAD;
                radius = INNER_RING_RADIUS;

                if (powerCount == 2) { //If two, put them side-by-side
                    azimuth += Math.PI / 2f;
                }
            }

            int pointX = (int)(centreX + Math.sin(azimuth) * radius);
            int pointY = (int)(centreY - Math.cos(azimuth) * radius);
            positions[i] = new int[] {pointX, pointY};
        }

        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();

        for (int i = 0; i < powerCount; i++) {
            int pointX = positions[i][0];
            int pointY = positions[i][1];

            drawSurround(pointX, pointY);
            drawSymbol(pointX, pointY, allomancies.get(i).getInvestiture());

            float test = i / 16f;
            drawGauge(pointX, pointY, test);
        }

        for (int i = 0; i < powerCount; i++) {
            int pointX = positions[i][0];
            int pointY = positions[i][1];

            if (mouseInArea(pointX, pointY, 144 /*12^2*/, mouseX, mouseY)) {
                drawLabel(pointX, pointY, allomancies.get(i).getInvestiture());
            }
        }

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();

        GlStateManager.popMatrix();
    }

    private void drawSurround(int x, int y) {
        GlStateManager.color(1, 1, 1, 1);
        mc.renderEngine.bindTexture(SURROUND);
        drawModalRectWithCustomSizedTexture(x - 20, y - 20, 0, 0, 40, 40, 40, 40);
    }

    private void drawLabel(int x, int y, Investiture investiture) {
        drawCenteredString(fontRenderer, investiture.localizedName, x, y - 22, 0xFFFFFF);
    }

    private void drawSymbol(int x, int y, Investiture investiture) {
        GlStateManager.color(1, 1, 1, 1);
        mc.renderEngine.bindTexture(investiture.symbol);
        drawModalRectWithCustomSizedTexture(x - 8, y - 8, 0, 0, 16, 16, 16, 16);
    }

    private void drawGauge(int x, int y, float fillValue) {
        float[] colour = DrawUtils.getGaugeColour(fillValue);
        GlStateManager.color(colour[0], colour[1], colour[2], 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        mc.renderEngine.bindTexture(GAUGE);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glTexCoord2f(0.5f, 0.5f);
        GL11.glVertex2f(x, y);

        //Top-left, fixed
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x - 20, y - 20);

        float angle = 45f + fillValue * 270f;
        float sin = (float)Math.sin(angle * -MathUtils.DEGTORAD);
        float cos = (float)Math.cos(angle * -MathUtils.DEGTORAD);

        if (fillValue < MathUtils.ONETHIRD) {
            GL11.glTexCoord2f(0.5f + sin / 2.0f, 0.5f - cos / 2.0f);
            GL11.glVertex2f(x + sin * 20, y - cos * 20);
        }
        else {
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(x - 20, y + 20);

            if (fillValue < MathUtils.TWOTHIRDS) {
                GL11.glTexCoord2f(0.5f + sin / 2.0f, 0.5f - cos / 2.0f);
                GL11.glVertex2f(x + sin * 20, y - cos * 20);
            }
            else {
                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(x + 20, y + 20);

                if (fillValue < 1) {
                    GL11.glTexCoord2f(0.5f + sin / 2.0f, 0.5f - cos / 2.0f);
                    GL11.glVertex2f(x + sin * 20, y - cos * 20);
                }
                else {
                    GL11.glTexCoord2f(1, 0);
                    GL11.glVertex2f(x + 20, y - 20);
                }
            }
        }
        GL11.glEnd();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }
}
