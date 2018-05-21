package mc.cyberplex.CaptureTheFlag;

import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.cyberplex.CaptureTheFlag.arena.Arena;
import mc.cyberplex.CaptureTheFlag.arena.ArenaState;
import mc.cyberplex.CaptureTheFlag.arena.FlagData;
import mc.cyberplex.CaptureTheFlag.arena.PlayerState;

public class Commands implements CommandExecutor{

	Main main = Main.getMain();
	Arena data = new Arena();
	ArenaState state = new ArenaState();
	PlayerState playerState = new PlayerState();
	FlagData flag = new FlagData();

	//create error messages for commands
	String noPermission = ChatColor.RED + "Sorry, you don't have permisson for that command",
			systemAdmin = ChatColor.RED + "Please contact your server administrator if you believe this is wrong",
			invalidCmd = ChatColor.RED + "Sorry the command was entered wrong",
			helpMsg = ChatColor.RED + "Do /ctf help for a list of commands",
			invalidArena = ChatColor.RED + "Sorry, that arena doesn't exist",
			invalidTeam = ChatColor.RED + "Sorry, that team color is invalid please choose red or blue",
			arenaExist = ChatColor.RED + "Sorry, an arena with that name already exists",
			missingName = ChatColor.RED + "Sorry, the arena name was missing",
			missingComponent = ChatColor.RED + "Sorry, a component of the command is missing";
	String team = null;

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args){

		FileConfiguration config = main.getConfig();

		Player player = (Player) sender;
		UUID playerID = player.getUniqueId();

		//check to see if a player is running the command versus the console
		if(sender instanceof Player){

			if(cmd.getName().equalsIgnoreCase("ctf")){

				switch(args[0].toLowerCase()){

				case "join":

					//check if player has permission to join arena
					if(player.hasPermission("ctf.join")){

						boolean inGame = false;
						
						//check if the player is currently in a game
						for(String arena: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

							int arenaNum = data.getArenaNum(arena);
							for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++){

								if(data.getArena(arenaNum).getPlayer(count).equals(playerID)) {
									inGame = true;
								}

							}

						}

						//check if the argument length is 2 for the command
						if(args.length == 2){

							String arenaName = args[1].toLowerCase();
							
							//check if the arena exist in the config
							if(main.getConfig().contains("Arenas." + arenaName)) {
								
								if(inGame == true) {
									
									player.sendMessage(ChatColor.RED + "Sorry you are currently in a game");
									player.sendMessage(ChatColor.RED + "Please do " + ChatColor.DARK_RED + "/ctf leave " + ChatColor.RED + "first before joining another arena");
																		
								} else {									
									playerState.joinGame(arenaName, player);									
								}
								
							} else {
								
								player.sendMessage(invalidArena);
								
								//teleport player to the hub
								player.teleport(data.getHub());
								player.sendMessage(ChatColor.YELLOW + "Sending you to the Capture the Flags hub.");
								
							}

						} else if(inGame == false){
							
							//teleport player to the hub
							player.teleport(data.getHub());
							player.sendMessage(ChatColor.YELLOW + "Sending you to the Capture the Flags hub.");
							
						} else {
							
							player.sendMessage(ChatColor.RED + "Sorry you are currently in a game");
							player.sendMessage(ChatColor.RED + "Please do " + ChatColor.DARK_RED + "/ctf leave " + ChatColor.RED + "first before joining another arena");
							
						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "leave":

					//check if player has permission to leave arena
					if(player.hasPermission("ctf.leave")){

						boolean inArena = false;

						for(String arena: main.getConfig().getConfigurationSection("Arenas").getKeys(false)){

							int arenaNum = data.getArenaNum(arena);
							for(int count = 0; count < data.getArena(arenaNum).getGameCount(); count++){

								if(data.getArena(arenaNum).getPlayer(count).equals(playerID)){
									playerState.leaveGame(arena, player);
									inArena = true;
								}

							}

						}

						if(inArena == false){
							player.sendMessage(ChatColor.RED + "Sorry you aren't in an arena");
						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "start":

					//check if player has permission to start arena
					if(player.hasPermission("ctf.start")){

						if(args.length == 2){

							//check if arena name exist in config
							if(config.contains("Arenas." + args[1].toLowerCase())){

								if(main.getConfig().getString("Arenas." + args[1].toLowerCase() + ".state").equals("running")) {

									player.sendMessage(ChatColor.RED + "CTF arena " + args[1].toLowerCase() + " is currently running");

								} else {

									state.start(args[1].toLowerCase());
									player.sendMessage(ChatColor.GREEN + "CTF arena " + args[1].toLowerCase() + " has been started");

								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "stop":

					//check if player has permission to stop arena
					if(player.hasPermission("ctf.stop")){

						if(args.length == 2){

							//check if arena name exist in config
							if(config.contains("Arenas." + args[1].toLowerCase())){
								
								if(main.getConfig().getString("Arenas." + args[1].toLowerCase() + ".state").equals("running")) {

								state.stop(args[1].toLowerCase());
								player.sendMessage(ChatColor.GREEN + "CTF arena " + args[1].toLowerCase() + " has been stopped");
								
								} else {
									
									player.sendMessage(ChatColor.RED + "CTF arena " + args[1].toLowerCase() + " is not running currently");
									
								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "create":

					//check if player has permission to create arena
					if(player.hasPermission("ctf.create")){

						if(args.length == 2){

							//check if arena name exist in config
							if(config.contains("Arenas." + args[1].toLowerCase())){

								//display error message to user saying the arena already exist
								player.sendMessage(arenaExist);

							} else {

								//display message to user saying that arena has been created
								player.sendMessage(ChatColor.GREEN + "Creating CTF arena " + args[1].toLowerCase());

								//save arena to config
								data.addArena(args[1].toLowerCase());

							}

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "delete":

					//check if player has permission to delete arena
					if(player.hasPermission("ctf.delete")){

						if(args.length == 2){

							//check if arena name exist in config
							if(config.contains("Arenas." + args[1].toLowerCase())){

								//remove arena from config
								data.removeArena(args[1].toLowerCase());

								//send message to user saying the arena was removed
								player.sendMessage(ChatColor.YELLOW + "Removing CTF arena " + args[1].toLowerCase());
								player.sendMessage(ChatColor.YELLOW + "Arena removed");


							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "minplayers":

					//check if player has permission to set min players
					if(player.hasPermission("ctf.minplayers")){

						if(args.length == 3){

							//check if arena name exist in config
							if(config.contains("Arenas." + args[1].toLowerCase())){

								int minNum = Integer.parseInt(args[2]);

								//save min players to config
								data.setMinPlayers(args[1].toLowerCase(), minNum);

								//send message to player saying minimum players was set
								player.sendMessage(ChatColor.GREEN + "Minimum players set to " + args[2] + " for arena " + args[1].toLowerCase());

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "maxplayers":

					//check if player has permission to set max players
					if(player.hasPermission("ctf.maxplayers")){

						if(args.length == 3){

							//check if arena name exist in config
							if(config.contains("Arenas." + args[1].toLowerCase())){

								int maxNum = Integer.parseInt(args[2]);

								//save max players to config
								data.setMaxPlayers(args[1].toLowerCase(), maxNum);

								//send message to player saying maximum players was set
								player.sendMessage(ChatColor.GREEN + "Maximum players set to " + args[2] + " for arena " + args[1].toLowerCase());

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "reload":

					//check if player has permission to reload plugin config
					if(player.hasPermission("ctf.reload")){

						if(args.length == 1){
							//reload config
							main.reloadConfig();
							main.saveConfig();

							//display message to player saying config was reloaded
							player.sendMessage(ChatColor.GREEN + "Capture the Flag config reloaded");

						} else {

							//display error message to user
							player.sendMessage(missingComponent);
							player.sendMessage(helpMsg);

						}

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "list":

					//check if player has permission to see arena list
					if(player.hasPermission("ctf.list")){

						//get arenas from config and save them to arena list
						Set<String> arenas = config.getConfigurationSection("Arenas").getKeys(false);
						String[] arenaList = new String[arenas.size()];
						arenas.toArray(arenaList);

						//display arena list to user
						String arenaMsg = new String();
						for(String arena: arenaList){
							arenaMsg += arena + "\n";							
						}
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_AQUA +  "Capture the Flags arenas");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.YELLOW + arenaMsg.substring(0,1).toUpperCase() + arenaMsg.substring(1));

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}					

					break;

				case "help":

					//check if player has permission for help command
					if(player.hasPermission("ctf.help")){

						//Display help messages to user
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.DARK_AQUA +  "Capture the Flag Commands");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
						player.sendMessage(ChatColor.YELLOW + "/ctf help: " + ChatColor.GRAY + "Display all commands for Capture the Flag plugin");
						player.sendMessage(ChatColor.YELLOW + "/ctf join [Arena]: " + ChatColor.GRAY + "Join an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf leave: " + ChatColor.GRAY + "Leave an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf start [Arena]: " + ChatColor.GRAY + "Start an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf stop [Arena]: " + ChatColor.GRAY + "Stop an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf create [Arena]: " + ChatColor.GRAY + "Create an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf delete [Arena]: " + ChatColor.GRAY + "Delete an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf minplayers [Arena] [number]: " + ChatColor.GRAY + "Set the minimum number of players required to start a game");
						player.sendMessage(ChatColor.YELLOW + "/ctf maxplayers [Arena] [number]: " + ChatColor.GRAY + "Set the maximum number of players for an arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf reload: " + ChatColor.GRAY + "Reload CTF config");
						player.sendMessage(ChatColor.YELLOW + "/ctf list: " + ChatColor.GRAY + "List all arenas");
						player.sendMessage(ChatColor.YELLOW + "/ctf add spawn [Arena] [Team]: " + ChatColor.GRAY + "Add the team spawn for the arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf add flag [Arena] [Team]: " + ChatColor.GRAY + "Add the team flag for the arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf add lobby [Arena]: " + ChatColor.GRAY + "Add the lobby spawn for the arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf add hub: " + ChatColor.GRAY + "Add hub for ctf arenas");
						player.sendMessage(ChatColor.YELLOW + "/ctf remove hub: " + ChatColor.GRAY + "Remove hub for ctf arenas.");
						player.sendMessage(ChatColor.YELLOW + "/ctf remove spawn [Arena] [Team]: " + ChatColor.GRAY + "Remove the team spawn for the arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf remove flag [Arena] [Team]: " + ChatColor.GRAY + "Remove the team flag for the arena");
						player.sendMessage(ChatColor.YELLOW + "/ctf remove lobby [Arena]: " + ChatColor.GRAY + "Remove the lobby for the arena");
						player.sendMessage(ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

					} else {

						//display error message to user
						player.sendMessage(noPermission);
						player.sendMessage(systemAdmin);

					}

					break;

				case "add":

					if(args[1].equalsIgnoreCase("spawn")){

						//check if player has permission to add a spawn
						if(player.hasPermission("ctf.add.spawn")){

							//check if arena name exist in the config
							if(config.contains("Arenas." + args[2].toLowerCase())){

								if(args.length == 4){

									int arenaNum = data.getArenaNum(args[2].toLowerCase());

									if(args[3].equalsIgnoreCase("red")){ //check if player is setting spawn for team red

										data.getCTFData(arenaNum).setRedSpawn(args[2].toLowerCase(), player);
										player.sendMessage(ChatColor.GREEN + "Spawn point created for red team");

									} else if(args[3].equalsIgnoreCase("blue")){ //check if player is setting spawn for team blue

										data.getCTFData(arenaNum).setBlueSpawn(args[2].toLowerCase(), player);
										player.sendMessage(ChatColor.GREEN + "Spawn point created for blue team");

									} else {

										//send error message to player
										player.sendMessage(invalidTeam);

										break;

									}

								} else {

									//display error message to user
									player.sendMessage(missingComponent);
								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else if (args[1].equalsIgnoreCase("flag")){

						//check if player has permission to add a flag
						if(player.hasPermission("ctf.add.flag")){

							//check if the arena name exist in the config
							if(config.contains("Arenas." + args[2].toLowerCase())){

								if(args.length == 4){

									//Check to see what team the user selected
									if(args[3].equalsIgnoreCase("red")){

										flag.setRedFlag(args[2].toLowerCase(), player);
										player.sendMessage(ChatColor.GREEN + "Flag created for red team");

									} else if (args[3].equalsIgnoreCase("blue")){

										flag.setBlueFlag(args[2].toLowerCase(), player);
										player.sendMessage(ChatColor.GREEN + "Flag created for blue team");

									} else {

										//display error message to user
										player.sendMessage(invalidTeam);

										break;

									}											

								} else {

									//display error message to user
									player.sendMessage(missingComponent);
								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else if (args[1].equalsIgnoreCase("lobby")){

						//check if player has permission to add a lobby
						if(player.hasPermission("ctf.add.lobby")){

							if(args.length == 3){

								//check if arena name exist in config
								if(config.contains("Arenas." + args[2].toLowerCase())){

									data.setLobby(args[2].toLowerCase(), player);
									player.sendMessage(ChatColor.GREEN + "Lobby created for " + args[2].toLowerCase() + " arena");

								} else {

									//display error message to user
									player.sendMessage(missingName);

								}

							} else {

								//display error message to user
								player.sendMessage(missingComponent);
								player.sendMessage(helpMsg);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else if (args[1].equalsIgnoreCase("hub")){

						//check if player has permission to add a hub
						if(player.hasPermission("ctf.add.hub")){

							if(args.length == 2){

								data.setHub(player);
								player.sendMessage(ChatColor.GREEN + "Hub created");

							} else {

								//display error message to user
								player.sendMessage(missingComponent);
								player.sendMessage(helpMsg);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else {

						//display error message to user
						player.sendMessage(missingComponent);
						player.sendMessage(helpMsg);

					}

					break;

				case "remove":

					if(args[1].equalsIgnoreCase("spawn")){

						//check if player has permission to remove spawn
						if(player.hasPermission("ctf.remove.spawn")){

							//check if config contains arena name
							if(config.contains("Arenas." + args[2].toLowerCase())){

								if(args.length == 4){

									//Check to see what team the user selected
									if(args[3].equalsIgnoreCase("red")){

										//Set team to red
										team = "Red";

									} else if (args[3].equalsIgnoreCase("blue")){

										//set team to blue
										team = "Blue";

									} else {

										//display error message to user
										player.sendMessage(invalidTeam);

										break;

									}

									//Display message to user
									player.sendMessage(ChatColor.GREEN + "Spawn removed for " + team + " team");

									//remove spawn from config
									config.set("Arenas." + args[2].toLowerCase() + "." + team + ".spawn", null);
									main.saveConfig();

								} else {

									//display error message to user
									player.sendMessage(missingComponent);
								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else if (args[1].equalsIgnoreCase("flag")){

						//check if player has permission to remove the flag
						if(player.hasPermission("ctf.remove.flag")){

							//check if the arean exist in the config
							if(config.contains("Arenas." + args[2].toLowerCase())){

								if(args.length == 4){

									//Check to see what team the user selected
									if(args[3].equalsIgnoreCase("red")){

										//remove team red flag
										team = "Red";

									} else if (args[3].equalsIgnoreCase("blue")){

										//remove team blue flag
										team = "Blue";

									} else {

										//display error message to user
										player.sendMessage(invalidTeam);

										break;

									}

									//Display message to user
									player.sendMessage(ChatColor.GREEN + "Flag removed for " + team + " team");

									//remove flag from config
									config.set("Arenas." + args[2].toLowerCase() + "." + team + ".flag", null);
									main.saveConfig();

								} else {

									//display error message to user
									player.sendMessage(missingComponent);
								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else if (args[1].equalsIgnoreCase("lobby")){

						//check if player has permission to remove the lobby
						if(player.hasPermission("ctf.remove.lobby")){

							//check if the arena exist in the config
							if(config.contains("Arenas." + args[2].toLowerCase())){

								if(args.length == 3){

									//Display message to user
									player.sendMessage(ChatColor.GREEN + "Lobby removed from " + args[2].toLowerCase() + " arena");

									//Remove lobby from config
									data.removeLobby(args[2].toLowerCase());

								} else {

									//display error message to user
									player.sendMessage(missingComponent);
								}

							} else {

								//display error message to user
								player.sendMessage(missingName);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else if (args[1].equalsIgnoreCase("hub")){

						//check to see if player has permission to remove the hub
						if(player.hasPermission("ctf.remove.hub")){

							if(args.length == 2){

								//Save spawnpoint to config
								data.removeHub();
								player.sendMessage(ChatColor.RED + "Hub removed");

							} else {

								//display error message to user
								player.sendMessage(missingComponent);
								player.sendMessage(helpMsg);

							}

						} else {

							//display error message to user
							player.sendMessage(noPermission);
							player.sendMessage(systemAdmin);

						}

					} else {

						//display error message to user
						player.sendMessage(missingComponent);
						player.sendMessage(helpMsg);

					}

					break;

				default:

					//display error message and help cmd to player
					player.sendMessage(invalidCmd);
					player.sendMessage(helpMsg);
				}

			}

		} else {

			//display error message to console
			sender.sendMessage(ChatColor.RED + "Sorry, only players can execute capture the flag commands");

		}

		return false;

	}
}
