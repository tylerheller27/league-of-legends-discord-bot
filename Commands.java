package dt.soap;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String[] rawDiscordInput = event.getMessage().getContentRaw().split("\\s+"); 									// split from whitespace

		if (rawDiscordInput[0].equalsIgnoreCase(Main.DISCORD_PREFIX_HELP)) 												// looks for help prefix to give prompt
		{
			event.getChannel().sendMessage("Type + followed by a summoners name to retrieve player info").queue();
			event.getChannel().sendMessage("Type * followed by champ select chat box to retrieve all teammates info").queue();
		}

		if (rawDiscordInput[0].equalsIgnoreCase(Main.DISCORD_PREFIX_SINGLE_LOOKUP)) 									// looking for + character for single search
		{
			String discordInput = rawDiscordInput[1];
			String[] playerStats = Helper.getPlayerStats(discordInput);
			event.getChannel().sendMessage(rawDiscordInput[1]).queue();
			for (int i = 0; i < playerStats.length; i++) {
				event.getChannel().sendMessage(playerStats[i]).queue();
			} // for
		} // if

		if (rawDiscordInput[0].equalsIgnoreCase(Main.DISCORD_PREFIX_MULTIPLE_LOOKUP)) 									// looking for * for multiple player search																	
		{
			int[] staticPlayerLocations = new int[] { 1, 5, 9, 13, 17 };												//known locations of player usernames within input *does not change*
			for (int i = 0; i < staticPlayerLocations.length; i++)
				{
				String player = rawDiscordInput[staticPlayerLocations[i]];												//same logic as single search but preformes the search 5 times
				String[] playerStats =Helper.getPlayerStats(player);
				event.getChannel().sendMessage(rawDiscordInput[staticPlayerLocations[i]]).queue();
				 	for (int j = 0; j < playerStats.length; j++)
				 		{
				 			event.getChannel().sendMessage(playerStats[j]).queue(); 
				 		}//for
				}//for		 
		} // if
	}// method
}// class