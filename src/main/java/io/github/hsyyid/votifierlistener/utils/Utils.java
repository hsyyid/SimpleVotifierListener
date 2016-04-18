package io.github.hsyyid.votifierlistener.utils;

import com.google.common.collect.Lists;
import io.github.hsyyid.votifierlistener.config.Config;
import io.github.hsyyid.votifierlistener.config.Configs;
import io.github.hsyyid.votifierlistener.config.Configurable;
import io.github.hsyyid.votifierlistener.config.RewardsConfig;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;

public class Utils
{
	private static Configurable config = Config.getConfig();
	private static Configurable rewardsConfig = RewardsConfig.getConfig();

	public static void addReward(String reward)
	{
		ConfigurationNode rewardNode = Configs.getConfig(rewardsConfig).getNode("rewards", "commands");
		String formattedDrop = (reward + ",");

		if (rewardNode.getValue() != null)
		{
			Configs.setValue(rewardsConfig, rewardNode.getPath(), rewardNode.getString() + formattedDrop);
		}
		else
		{
			Configs.setValue(rewardsConfig, rewardNode.getPath(), formattedDrop);
		}
	}

	public static void removeReward(String reward)
	{
		ConfigurationNode rewardNode = Configs.getConfig(rewardsConfig).getNode("rewards", "commands");
		String formattedDrop = (reward + ",");

		if (rewardNode.getValue() != null)
		{
			Configs.setValue(rewardsConfig, rewardNode.getPath(), rewardNode.getString().replace(formattedDrop, ""));
		}
	}

	public static ArrayList<String> getRewards()
	{
		ConfigurationNode valueNode = Configs.getConfig(rewardsConfig).getNode("rewards", "commands");

		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();

		ArrayList<String> rewardList = Lists.newArrayList();
		boolean finished = false;

		if (finished != true)
		{
			int endIndex = list.indexOf(",");

			if (endIndex != -1)
			{
				String substring = list.substring(0, endIndex);
				rewardList.add(substring);

				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = list.indexOf(",", startIndex + 1);
					if (endIndex != -1)
					{
						String substrings = list.substring(startIndex + 1, endIndex);
						rewardList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				rewardList.add(list);
				finished = true;
			}
		}

		return rewardList;
	}

	public static void addLink(String link)
	{
		ConfigurationNode linkNode = Configs.getConfig(config).getNode("vote", "links");
		String newLink = (link + ",");

		if (linkNode.getValue() != null)
		{
			Configs.setValue(config, linkNode.getPath(), linkNode.getString() + newLink);
		}
		else
		{
			Configs.setValue(config, linkNode.getPath(), newLink);
		}
	}

	public static boolean isEconomyEnabled()
	{
		ConfigurationNode valueNode = Configs.getConfig(config).getNode("economy", "toggle");

		if (valueNode.getValue() != null)
		{
			return valueNode.getBoolean();
		}
		else
		{
			Configs.setValue(config, valueNode.getPath(), true);
			return true;
		}
	}

	public static ArrayList<String> getLinks()
	{
		ConfigurationNode valueNode = Configs.getConfig(config).getNode("vote", "links");

		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();
		ArrayList<String> linkList = Lists.newArrayList();
		boolean finished = false;

		if (finished != true)
		{
			int endIndex = list.indexOf(",");
			if (endIndex != -1)
			{
				String substring = list.substring(0, endIndex);
				linkList.add(substring);

				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = list.indexOf(",", startIndex + 1);
					if (endIndex != -1)
					{
						String substrings = list.substring(startIndex + 1, endIndex);
						linkList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				linkList.add(list);
				finished = true;
			}
		}

		return linkList;
	}

	public static int getMaxMoneyReward()
	{
		ConfigurationNode valueNode = Configs.getConfig(rewardsConfig).getNode("rewards", "money", "maximum");
		return valueNode.getInt();
	}

	public static int getMinimumMoneyReward()
	{
		ConfigurationNode valueNode = Configs.getConfig(rewardsConfig).getNode("rewards", "money", "minimum");
		return valueNode.getInt();
	}

	public static int getAmtOfRewards()
	{
		ConfigurationNode valueNode = Configs.getConfig(rewardsConfig).getNode("rewards", "amount");

		if (valueNode.getValue() != null)
		{
			return valueNode.getInt();
		}
		else
		{
			Configs.setValue(rewardsConfig, valueNode.getPath(), 2);
			return 2;
		}
	}

	public static boolean shouldGiveAllRewards()
	{
		ConfigurationNode valueNode = Configs.getConfig(rewardsConfig).getNode("rewards", "mode");

		if (valueNode.getValue() != null) {
			if (valueNode.getValue().toString().equalsIgnoreCase("all"))
			{
				return true;
			}
			else if (valueNode.getValue().toString().equalsIgnoreCase("random"))
			{
				return false;
			}
			else
			{
				Configs.setValue(rewardsConfig, valueNode.getPath(), "random");
				return false;
			}
		}
		else
		{
			Configs.setValue(rewardsConfig, valueNode.getPath(), "random");
			return false;
		}
	}

	public static boolean shouldAnnounceVotes()
	{
		ConfigurationNode valueNode = Configs.getConfig(config).getNode("vote", "announce");

		if (valueNode.getValue() != null)
		{
			return valueNode.getBoolean();
		}
		else
		{
			Configs.setValue(config, valueNode.getPath(), true);
			return true;
		}
	}
}
