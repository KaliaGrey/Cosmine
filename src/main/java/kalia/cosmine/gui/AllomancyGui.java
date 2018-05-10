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
import kalia.cosmine.utils.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AllomancyGui extends InvestitureGui {
    private static final int INNER_RING_RADIUS = 50;
    private static final int OUTER_RING_RADIUS = 80;

    private static final String[] METALS = {
        "copper", "bronze", "electrum", "gold", "aluminum", "duralumin", "pewter", "tin",
        "zinc", "brass", "bendalloy", "cadmium", "chromium", "nicrosil", "steel", "iron"
    };

    private static ResourceLocation[] SYMBOLS;

    public AllomancyGui() {
        if (SYMBOLS == null) {
            SYMBOLS = new ResourceLocation[16];
            for (int i = 0; i < 16; i++) {
                SYMBOLS[i] = new ResourceLocation(String.format("%s:textures/gui/allomancy/symbol%s.png", Cosmine.MOD_ID, METALS[i]));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();

        int centreX = width / 2;
        int centreY = height / 2;

        float angle = mouseAngle(centreX, centreY, mouseX, mouseY);

        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();

        for (int i = 0; i < 16; i++) {
            float azimuth = (((i % 8) * 45f) + (i < 8 ? 0 : 22.5f)) * MathUtils.DEGTORAD;
            float radius = i < 8 ? INNER_RING_RADIUS : OUTER_RING_RADIUS;

            int pointX = (int)(centreX + Math.sin(azimuth) * radius);
            int pointY = (int)(centreY - Math.cos(azimuth) * radius);

            if (mouseInArea(pointX, pointY, 144 /*12^2*/, mouseX, mouseY)) {
                String metalName = I18n.format("investiture.allomancy." + METALS[i]);
                drawCenteredString(fontRenderer, metalName, (int)pointX, (int)pointY - 18, 0xFFFFFF);
            }

            Investiture investiture = Allomancy.getInvestiture(METALS[i]);
            boolean hasInvestiture = this.spiritweb.hasInvestitureSource(investiture);
            GlStateManager.color(1, 1, 1, hasInvestiture ? 1.0f : 0.25f);

            mc.renderEngine.bindTexture(SYMBOLS[i]);
            drawModalRectWithCustomSizedTexture((int)(pointX - 8), (int)(pointY - 8), 0, 0, 16, 16, 16, 16);

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
}
