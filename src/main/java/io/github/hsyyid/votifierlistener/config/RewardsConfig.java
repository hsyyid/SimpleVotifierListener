package io.github.hsyyid.votifierlistener.config;

import io.github.hsyyid.votifierlistener.VotifierListenerPlugin;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the rewards.conf file
 */
public class RewardsConfig implements Configurable
{
	private static RewardsConfig config = new RewardsConfig();

	private RewardsConfig()
	{
		;
	}

	public static RewardsConfig getConfig()
	{
		return config;
	}

	private Path configFile = Paths.get(VotifierListenerPlugin.getVotifierListenerPlugin().getConfigDir().resolve("data") + "/rewards.conf");
	private ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
	private CommentedConfigurationNode configNode;

	@Override
	public void setup()
	{
		if (!Files.exists(configFile))
		{
			try
			{
				Files.createFile(configFile);
				load();
				populate();
				save();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			load();
		}
	}

	@Override
	public void load()
	{
		try
		{
			configNode = configLoader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save()
	{
		try
		{
			configLoader.save(configNode);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void populate()
	{
		get().getNode("rewards", "amount").setValue(2).setComment("Number of rewards a player will receive.");
		get().getNode("rewards", "commands").setValue("minecraft:give @p minecraft:diamond_sword 1,").setComment("Possible reward commands for voting.");
		get().getNode("rewards", "money", "maximum").setValue(500).setComment("Maximum amount of money a player can get for voting.");
		get().getNode("rewards", "money", "minimum").setValue(100).setComment("Minimum amount of money a player can get for voting.");
	}

	@Override
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
