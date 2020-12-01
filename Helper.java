package dt.soap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;

public class Helper {
	
	private static HttpURLConnection connection;
	private static final String API_KEY = "";														// ***your own api key***
	
	public static String sendToApi(String Url)
	{
		
		BufferedReader reader;
		String line;																				//string line that we are reading from
		StringBuffer riot_API_response_content = new StringBuffer();								// response from riot server
		
		try 
		{	
			URL url = new URL(Url);																	//creates URL object from arguments from method 	
			connection = (HttpURLConnection) url.openConnection();									//type casting to specify that we will be using Http version of URL
			
			int status = connection.getResponseCode();																//testing status for failed and successful connections
			
			if (status > 299)	{																					//if your connection has problems 
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));					//getting error from exception 
				while((line = reader.readLine()) != null)															//while we still have stuff to read append the content to line
				{
					riot_API_response_content.append(line);
				}
				reader.close();
			}	
			else {
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));				//successful connection
					while((line = reader.readLine()) != null)	
					{
						riot_API_response_content.append(line);
					}
					reader.close();
			}//else
			
		}//try
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			connection.disconnect();
		}
		
		String string_response = riot_API_response_content.toString();						// turning response content into string to return
		return string_response;
	}//method sedntoApi
	
	public static String[] getPlayerStats (String player_username)
	{
		//get player account info
		String[] player_stats = new String[3];
		
		String riot_api_url = "https://NA1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + player_username + "?" + API_KEY;
		String apiResponse = Helper.sendToApi(riot_api_url);													//sends url to sendToApi methods and stores response in string
												
		String id = StringUtils.substringBetween(apiResponse, "\"id\":\"", "\",\"");					//finding substring by searching for start and end parameters ex: start= \"id\   end= \",\"
		String accountId = StringUtils.substringBetween(apiResponse, "\"accountId\":\"", "\",\"");		//storing accountId for later use
		 
		//get player rank
		String newUrl2 ="https://NA1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + id + "?" + API_KEY;
		String apiResponseRanked = Helper.sendToApi(newUrl2);
		
		
		String rank = StringUtils.substringBetween(apiResponseRanked, "\"tier\":\"", "\",\"");
		
		if(apiResponseRanked.equals("[]"))																//if unranked, api will respond with []
		{																								//player rank is first value stored in player_stats String array
			player_stats[0] = "Rank: Unranked";
		}
		else
		{
			player_stats[0] = "Rank:" + " " + rank.toLowerCase();
		}
		
		//get players past 25 games of match history	
		//determine their most played roll in the game, top,mid,jungle,bottom
		String match_history ="https://na1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accountId + "?queue=400&queue=410&queue=420&queue=430&queue=440&season=13" + "&" + API_KEY;
		String api_response_matchHistory = Helper.sendToApi(match_history);
		
		int top = 0;
		int mid = 0;
		int jungle = 0;				
		int bottom = 0;
		String mostPlayed = "";
		
		StringTokenizer string_tokenizer = new StringTokenizer(api_response_matchHistory, "}");				//every match history ends with a "}" character so I use this character as the delimiter
																											//to separate between matches
		int totalGames = string_tokenizer.countTokens(); 													// total tokens aka total games played
		while(string_tokenizer.hasMoreTokens())
		{
			
			String tmp = string_tokenizer.nextToken().toString();
			if(tmp.contains("MID"))
					{
						mid++;
					}
			else if(tmp.contains("TOP"))
			{
				top++;
			}																								//populate mid,top,boottom and jungle int variable to determine most played role
			else if(tmp.contains("BOTTOM"))
			{
				bottom++;
			}
			else if(tmp.contains("JUNGLE"))
			{
				jungle++;
			}
		}//while
		
		//calculate most played role
		int r1 = Math.max(mid, top);
		int r2 = Math.max(bottom, jungle);
		int rf = Math.max(r1, r2);
		
		if(rf == mid)
		{
			mostPlayed = "Mid";
		}
		if(rf == top)
		{
			mostPlayed = "Top";
		}
		if(rf == bottom)
		{
			mostPlayed = "Bottom";
		}
		if(rf == jungle)
		{
			mostPlayed = "Jungle";
		}
		player_stats[1] = "Total summoner's rift games played this season: " + totalGames + " " + "(*max of 101 games recorded*)";					// 2nd element in player_stats String array
		player_stats[2] = "Most played role:" + " " + mostPlayed;																					// 3nd element in player_stats String array
		
		return player_stats;
	}//method		
}//class
