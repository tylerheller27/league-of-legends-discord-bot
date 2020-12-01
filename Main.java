package dt.soap;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
	public static JDA jda;																		//reference variable for jda files imported
	public static String DISCORD_PREFIX_HELP = "help";											// prefix used to identify username you want the bot to lookup
	public static String DISCORD_PREFIX_SINGLE_LOOKUP = "+"; 
	public static String DISCORD_PREFIX_MULTIPLE_LOOKUP = "*";									//prefix to signal bulk search
	
	//main method
	public static void main(String[] args) throws LoginException  {
		//bot connects to the discord bot we created
		//token connects you to your bot
		
		final JDA jda = JDABuilder.createDefault("").build();									//***your own discord bot token***
		
		jda.addEventListener(new Commands());													 // "registers file as event"
	}//main
}//class
