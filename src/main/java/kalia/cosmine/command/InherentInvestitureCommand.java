package kalia.cosmine.command;

import com.google.common.collect.Lists;
import kalia.cosmine.capability.PlayerSpiritweb;
import kalia.cosmine.investiture.Investiture;
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

public class InherentInvestitureCommand extends CommandBase {
    public static final ArrayList<String> ALIASES = Lists.newArrayList("inherentinvestiture", "investiture");

    public InherentInvestitureCommand() {

    }

    @Override
    public String getName() {
        return "inherentinvestiture";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.inherentinvestiture.usage";
    }

    @Override
    public List<String> getAliases() {
        return ALIASES;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2 && (args[0].equals("add") || args[0].equals("remove"))) { //Todo: Check casing
            return;
        }

        boolean add = args[0].equals("add");
        Investiture investiture = InvestitureRegistry.getInvestiture(args[1]);

        if (investiture == null) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error parsing investiture!"));
            return;
        }

        float intensity = 0;
        if (add) {
            try {
                intensity = args.length == 3 ? Float.parseFloat(args[2]) : 1.0f;
            }
            catch (NumberFormatException e) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error parsing intensity!"));
                return;
            }
        }

        if (sender instanceof EntityPlayerMP) {
            PlayerSpiritweb spiritweb = PlayerSpiritweb.getPlayerSpiritWeb((EntityPlayerMP)sender);
            spiritweb.setInherentInvestiture(investiture, intensity);
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Inherent investiture updated."));
        }
    }
}
