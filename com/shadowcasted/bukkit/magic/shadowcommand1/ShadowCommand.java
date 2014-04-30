import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public abstract class ShadowCommand{

	
	public abstract String CommandString();
	
	public abstract boolean ActUpon();

	
	
	private static boolean isListening = false;
	public static void startListener(Plugin p){
		if(p == null){throw new NullPointerException("The Plugin Instance To Start The Listener Can Not Be Null Can Not Be Null");}
		if(!isListening){
			plugin = p;
			Bukkit.getServer().getPluginManager().registerEvents(new Listenerz(), plugin);
			isListening = true;
		}else{throw new AlreadyListeningException("This Was Thrown Because You Should Be Paying Attention To Your Code. This Method Should Only Be Called Once.");}
	}

	private static HashMap<String, ShadowCommand> shadowmap = new HashMap<String, ShadowCommand>();
	public final static HashMap<String, ShadowCommand> getCommandMap(){return shadowmap;}

	public final static void addCommand(ShadowCommand shadowcommand){
		if(shadowcommand == null){throw new NullPointerException("The ShadowCommand Object Cant Be Null");}
		if(shadowcommand.CommandString() == null){throw new NullPointerException("The Command Can't Be Null");}
		if(!isListening){throw new NullListenerException("The Listener Wasn't Setup Or Setup Properly.");}
		if(getCommandMap().containsKey(shadowcommand.CommandString().toLowerCase())){
			removeCommand(shadowcommand.CommandString());
		}
		addCommand(shadowcommand.CommandString(),shadowcommand);
		
	}
	public final static void addCommand(String command, ShadowCommand shadowcommand){
		if(command == null){throw new NullPointerException("The Command Can't Be Null");}
		if(shadowcommand == null){throw new NullPointerException("The ShadowCommand Object Cant Be Null");}
		if(!isListening){throw new NullListenerException("The Listener Wasn't Setup Or Setup Properly.");}
		else{
			if(getCommandMap().containsKey(command.toLowerCase())){throw new ExistingCommandException("The Command Already Existed");}
			else{getCommandMap().put(command.toLowerCase(), shadowcommand);}
		}
	}

	public final static void overrideCommand(String command, ShadowCommand shadowcommand){
		if(command == null){throw new NullPointerException("The Command Can't Be Null");}
		if(shadowcommand == null){throw new NullPointerException("The ShadowCommand Object Cant Be Null");}
		if(!isListening){throw new NullListenerException("The Listener Wasn't Setup Or Setup Properly.");}
		if(!getCommandMap().containsKey(command.toLowerCase())){throw new NullPointerException("The Command Doesn't Exist");}
		else{getCommandMap().put(command.toLowerCase(), shadowcommand);}
	}
	
	public final static void removeCommand(String command){
		if(command== null){throw new NullPointerException("The Command Can't Be Null");}
		if(getCommandMap().containsKey(command.toLowerCase())){getCommandMap().remove(command.toLowerCase());}
		else{throw new NullPointerException("Unknown Command. Error Caused By removeCommand(String command) With The String Not Being A Valid Command");}
	}

	public final static String[] getCommands(){return (String[])getCommandMap().keySet().toArray();}

	private final static class Listenerz implements Listener{
		@EventHandler
		private final void Commandz(PlayerCommandPreprocessEvent event){
			ShadowCommand.event = event;
			showInConsole = true;
			String cmd = getCommand().toLowerCase();
			if(getCommandMap().containsKey(cmd.toLowerCase())&&getCommandMap().get(cmd.toLowerCase())!= null){
				getEvent().setCancelled(getCommandMap().get(cmd.toLowerCase()).ActUpon());
				if(showInConsole){System.out.println(getPlayer().getName()+" issued server command: "+event.getMessage());}
			}
		}
	}
	
	public final static void doCommand(){Bukkit.getServer().dispatchCommand(getPlayer(), getEvent().getMessage().replaceFirst("/", ""));}
	
	private static boolean showInConsole = true;
	public final static void setVisibleInConsole(boolean b){showInConsole = b;}
	public final static boolean isVisibleInConsole(){return showInConsole;}
	
	private static Plugin plugin;
	public final static Plugin getPlugin(){return plugin;}

	private  static PlayerCommandPreprocessEvent event;
	public final static PlayerCommandPreprocessEvent getEvent(){return event;}

	public final static boolean isCanceled(){return getEvent().isCancelled();}
	public final static void setCanceled(boolean b){getEvent().setCancelled(b);}

	public final static String getCommand(){
		if(getEvent().getMessage().contains(" ")){return getEvent().getMessage().split(" ")[0].replaceFirst("/","");}
		else{return getEvent().getMessage().replaceFirst("/","");}
	}
	
	public final static void setCommand(String command){getEvent().setMessage(getEvent().getMessage().replaceFirst(getCommand(), command.toLowerCase()));}

	public final static Player getPlayer(){return getEvent().getPlayer();}
	public final static void setPlayer(Player p){getEvent().setPlayer(p);}

	public final static String[] getArguments(){
		if(getEvent().getMessage().contains(" ")){ //if no args
			String temp = getEvent().getMessage().replaceFirst(getEvent().getMessage().split(" ")[0], "");
			if(!temp.contains(" ")){return new String[]{temp};}
			else{return temp.split(" ");}
		}
		else{return null;}
	}
	public final static void setArguments(String[] args){
		String temp = "";
		for(String s: args){
			temp += s;
		}
		temp = temp.trim();
		getEvent().setMessage(getEvent().getMessage().split(" ")[0]+" " + temp);
	}

	
	
	public final static class AlreadyListeningException extends RuntimeException {
		private final static long serialVersionUID = 8910702955372715884L;
		public AlreadyListeningException() { super(); }
		public AlreadyListeningException(String message) { super(message); }
		public AlreadyListeningException(String message, Throwable cause) { super(message, cause); }
		public AlreadyListeningException(Throwable cause) { super(cause); }
	}
	
	public final static class NullListenerException extends RuntimeException {
		private static final long serialVersionUID = 8221265252078526485L;
		public NullListenerException() { super(); }
		public NullListenerException(String message) { super(message); }
		public NullListenerException(String message, Throwable cause) { super(message, cause); }
		public NullListenerException(Throwable cause) { super(cause); }
	}
	
	public final static class ExistingCommandException extends RuntimeException {
		private static final long serialVersionUID = -4322252716992506142L;
		public ExistingCommandException() { super(); }
		public ExistingCommandException(String message) { super(message); }
		public ExistingCommandException(String message, Throwable cause) { super(message, cause); }
		public ExistingCommandException(Throwable cause) { super(cause); }
	}

}
