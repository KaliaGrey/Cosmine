package kalia.cosmine.gui;

import kalia.cosmine.capability.PlayerSpiritweb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.vector.Vector2f;

public abstract class InvestitureGui extends GuiScreen {
    protected PlayerSpiritweb spiritweb;

    public InvestitureGui() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        this.spiritweb = PlayerSpiritweb.getPlayerSpiritWeb(player);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int centreX = width / 2;
        int centreY = height / 2;

        String identityCode = getIdentityCode(this.spiritweb.getIdentity());
        drawCenteredString(Minecraft.getMinecraft().standardGalacticFontRenderer, identityCode, centreX, centreY - 16, 0xFFFFFF);

        if (mouseInArea(centreX, centreY, 256 /*16^2*/, mouseX, mouseY)) {
            String identityIntensity = (int)(this.spiritweb.getIdentityIntensity() *  100) + "%";
            drawCenteredString(fontRenderer, identityIntensity, centreX, centreY + 12, 0xFFFFFF);
        }
    }

    protected static boolean mouseInArea(int x, int y, float sqrRadius, int mouseX, int mouseY) {
        return (Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2)) < sqrRadius;
    }

    protected static float mouseAngle(int x, int y, int mouseX, int mouseY) {
        Vector2f baseVec = new Vector2f(1F, 0F);
        Vector2f mouseVec = new Vector2f(mouseX - x, mouseY - y);

        float ang = (float) (Math.acos(Vector2f.dot(baseVec, mouseVec) / (baseVec.length() * mouseVec.length())) * (180F / Math.PI));
        return mouseY < y ? 360F - ang : ang;
    }

    protected String getIdentityCode(String identity) {
        int hash = identity.hashCode();
        char[] chars = new char[4];

        chars[0] = (char)(((hash >> 24) % 26) + 97);
        chars[1] = (char)(((hash >> 16) % 26) + 97);
        chars[2] = (char)(((hash >> 8) % 26) + 97);
        chars[3] = (char)((hash % 26) + 97);

        return new String(chars);
    }
}
