package io.github.hsyyid.votifierlistener.cmdexecutors;

import io.github.hsyyid.votifierlistener.utils.PaginatedList;
import io.github.hsyyid.votifierlistener.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class VoteCommand implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Optional<Integer> pageNo = ctx.<Integer> getOne("page number");
		int pgNo = pageNo.orElse(1);

		PaginatedList pList = new PaginatedList("/vote");

		for (String link : Utils.getLinks())
		{
			try
			{
				Text item = Text.builder(link)
					.onClick(TextActions.openUrl(new URL(link)))
					.onHover(TextActions.showText(Text.of(TextColors.WHITE, "Go to ", TextColors.GOLD, link)))
					.color(TextColors.DARK_AQUA)
					.style(TextStyles.UNDERLINE)
					.build();

				pList.add(item);
			}
			catch (MalformedURLException e)
			{
				;
			}
		}

		pList.setItemsPerPage(10);

		if (pgNo > pList.getTotalPages())
			pgNo = 1;

		Text.Builder header = Text.builder();
		header.append(Text.of(TextColors.GREEN, "------------"));
		header.append(Text.of(TextColors.GREEN, " Showing Vote Links page " + pgNo + " of " + pList.getTotalPages() + " "));
		header.append(Text.of(TextColors.GREEN, "------------"));

		pList.setHeader(header.build());
		src.sendMessage(pList.getPage(pgNo));
		return CommandResult.success();
	}
}
