package kalia.cosmine.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerSpiritwebProvider implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(ISpiritweb.class)
    public static Capability<ISpiritweb> SPIRITWEB = null;

    private ISpiritweb instance = SPIRITWEB.getDefaultInstance();

    public PlayerSpiritwebProvider() {
        this.instance = new PlayerSpiritweb();
    }

    public PlayerSpiritwebProvider(EntityPlayer player) {
        this.instance = new PlayerSpiritweb(player);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return SPIRITWEB != null && capability == SPIRITWEB;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (this.hasCapability(capability, facing)) {
            return (T)this.instance;
        }
        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return SPIRITWEB.getStorage().writeNBT(SPIRITWEB, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        SPIRITWEB.getStorage().readNBT(SPIRITWEB, this.instance, null, nbt);
    }
}
