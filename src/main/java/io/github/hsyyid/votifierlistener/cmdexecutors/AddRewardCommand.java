package io.github.hsyyid.votifierlistener.cmdexecutors;

import io.github.hsyyid.votifierlistener.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AddRewardCommand implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String command = ctx.<String> getOne("command").get();
		Utils.addReward(command);
		src.sendMessage(Text.of(TextColors.GREEN, "[VotifierListener]: ", TextColors.GOLD, "Successfully added reward!"));
		return CommandResult.success();
	}
}
