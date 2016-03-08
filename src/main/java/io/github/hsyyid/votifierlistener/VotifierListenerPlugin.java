package io.github.hsyyid.votifierlistener;

import com.google.inject.Inject;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import io.github.hsyyid.votifierlistener.cmdexecutors.AddLinkExecutor;
import io.github.hsyyid.votifierlistener.cmdexecutors.AddRewardCommand;
import io.github.hsyyid.votifierlistener.cmdexecutors.RemoveRewardCommand;
import io.github.hsyyid.votifierlistener.cmdexecutors.VoteCommand;
import io.github.hsyyid.votifierlistener.cmdexecutors.VotifierListenerCommand;
import io.github.hsyyid.votifierlistener.config.Config;
import io.github.hsyyid.votifierlistener.config.RewardsConfig;
import io.github.hsyyid.votifierlistener.utils.Utils;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Plugin(id = "VotifierListener", name = "VotifierListener", version = "0.4", description = "This plugin enables server admins to give players rewards for voting for their server.", dependencies = @Dependency(id = "nuvotifier", version = "1.0", optional = false) )
public class VotifierListenerPlugin
{
	protected VotifierListenerPlugin()
	{
		;
	}

	public static Game game;
	public static EconomyService economyService;

	private static VotifierListenerPlugin votifierListenerPlugin;

	public static VotifierListenerPlugin getVotifierListenerPlugin()
	{
		return votifierListenerPlugin;
	}

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	@Listener
	public void onServerStart(GameInitializationEvent event)
	{
		getLogger().info("VotifierListener loading...");
		votifierListenerPlugin = this;
		game = Sponge.getGame();

		// Config File
		if (!Files.exists(configDir))
		{
			try
			{
				Files.createDirectories(configDir);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Create data Directory
		if (!Files.exists(configDir.resolve("data")))
		{
			try
			{
				Files.createDirectories(configDir.resolve("data"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		Config.getConfig().setup();
		RewardsConfig.getConfig().setup();

		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("addreward"), CommandSpec.builder()
			.description(Text.of("Adds Rewards for Votes"))
			.permission("votifierlistener.command.addreward")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("command"))))
			.executor(new AddRewardCommand())
			.build());

		subcommands.put(Arrays.asList("removereward"), CommandSpec.builder()
			.description(Text.of("Removes Rewards for Votes"))
			.permission("votifierlistener.command.removereward")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("command"))))
			.executor(new RemoveRewardCommand())
			.build());

		subcommands.put(Arrays.asList("addlink", "addvotelink"), CommandSpec.builder()
			.description(Text.of("Adds Links for Votes"))
			.permission("votifierlistener.command.addlink")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("link"))))
			.executor(new AddLinkExecutor())
			.build());

		CommandSpec votifierListenerCommandSpec = CommandSpec.builder()
			.description(Text.of("VotifierListener Command"))
			.permission("votifierlistener.command.use")
			.children(subcommands)
			.executor(new VotifierListenerCommand())
			.build();

		game.getCommandManager().register(this, votifierListenerCommandSpec, "votifierlistener");

		CommandSpec voteCommandSpec = CommandSpec.builder()
			.description(Text.of("Vote Command"))
			.permission("votifierlistener.command.vote")
			.arguments(GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.integer(Text.of("page number")))))
			.executor(new VoteCommand())
			.build();

		game.getCommandManager().register(this, voteCommandSpec, "vote");

		getLogger().info("-----------------------------");
		getLogger().info("VotifierListener was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("VotifierListener loaded!");
	}

	@Listener
	public void onPostInit(GamePostInitializationEvent event)
	{
		Optional<EconomyService> econService = Sponge.getServiceManager().provide(EconomyService.class);

		if (econService.isPresent())
		{
			economyService = econService.get();
		}
		else
		{
			getLogger().error("Error! This plugin requires an Economy plugin.");
		}
	}

	@Listener
	public void onVote(VotifierEvent event)
	{
		Vote vote = event.getVote();

		if (Utils.shouldAnnounceVotes())
			MessageChannel.TO_ALL.send(Text.of(TextColors.GREEN, "[VotifierListener]: ", TextColors.GOLD, event.getVote().getUsername() + " just voted and got a reward! You can too with ", TextColors.GRAY, "/vote"));

		for (Player player : Sponge.getServer().getOnlinePlayers())
		{
			if (player.getName().equals(vote.getUsername()))
			{
				player.sendMessage(Text.of(TextColors.GREEN, "Thanks for Voting! Here is a reward!"));
				UniqueAccount uniqueAccount = economyService.getOrCreateAccount(player.getUniqueId()).get();
				Random rand = new Random();
				BigDecimal decimal = new BigDecimal(rand.nextInt(Utils.getMaxMoneyReward() - Utils.getMinimumMoneyReward()) + Utils.getMinimumMoneyReward());
				uniqueAccount.deposit(economyService.getDefaultCurrency(), decimal, Cause.of(NamedCause.source(player)));

				for (int counter = 0; counter < 2; counter++)
				{
					String command = Utils.getRewards().get(rand.nextInt(Utils.getRewards().size()));
					command = command.replaceAll("@p", player.getName());
					Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
				}

				break;
			}
		}
	}

	public Path getConfigDir()
	{
		return configDir;
	}
}
