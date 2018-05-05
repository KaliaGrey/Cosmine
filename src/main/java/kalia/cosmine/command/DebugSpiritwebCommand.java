package kalia.cosmine.command;

import com.google.common.collect.Lists;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.Investiture;
import kalia.cosmine.investiture.allomancy.InherentAllomancySource;
import kalia.cosmine.registry.InvestitureRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class DebugSpiritwebCommand extends CommandBase {
    public static final ArrayList<String> ALIASES = Lists.newArrayList("debugspiritweb", "spiritweb");

    public DebugSpiritwebCommand() {

    }

    @Override
    public String getName() {
        return "debugspiritweb";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return ALIASES;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb((EntityPlayerMP)sender);
            spiritweb.printDebugInformation(sender);
        }
    }
}
