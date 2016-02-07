package io.github.hsyyid.votifierlistener.cmdexecutors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class VotifierListenerCommand implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(TextColors.GREEN, "[VotifierListener]: ", TextColors.GOLD, "Version: ", TextColors.GRAY, Sponge.getPluginManager().getPlugin("VotifierListener").get().getVersion()));
		return CommandResult.success();
	}
}
