package sigler.rebelempiremc.com;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Exp extends JavaPlugin
{
	//Vault Start
	public static Permission permission = null;
	public static Economy economy = null;
	public static Chat chat = null;

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
	//Vault End

	public void onEnable()
	{
		if (!setupEconomy() ) {
			return;
		}
		setupPermissions();
		setupChat();
		saveDefaultConfig();
		getServer().getLogger().info("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		getServer().getLogger().info("Exp2Money V1.2 has been enabled!");
		getServer().getLogger().info("Author: 97WaterPolo");
		getServer().getLogger().info("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public void onDisable()
	{
		getServer().getLogger().info("REMC EXP has been disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("ex") || cmd.getName().equalsIgnoreCase("xp"))
		{
			if (sender.hasPermission("remc.expmoney.exp"))
			{
				int rate = this.getConfig().getInt("TransferRate");
				int exp = player.getTotalExperience();
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
				player.sendMessage(ChatColor.GREEN + "Currently you have " + ChatColor.RED + exp + ChatColor.GREEN + "which can be transferred at a rate of " + ChatColor.RED + rate + ChatColor.GREEN + "per exp using /em.");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("expmoney") || cmd.getName().equalsIgnoreCase("em"))
		{
			if (sender.hasPermission("remc.expmoney.use"))
			{
				int rate = this.getConfig().getInt("TransferRate");
				int exp = player.getTotalExperience();
				int money = exp * rate;
				
				
				EconomyResponse r = economy.depositPlayer(player.getName(), money);
				if(r.transactionSuccess()) {
					player.sendMessage(ChatColor.AQUA + "[" + ChatColor.DARK_GREEN + "Server" + ChatColor.AQUA + "]" + ChatColor.YELLOW + "The amount of " + ChatColor.RED + money + ChatColor.YELLOW + " has been deposited into your account!" );
				} else {
					player.sendMessage(String.format("An error occured: %s", r.errorMessage));
				}
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
				player.setExp(0);
				player.setLevel(0);
				player.setTotalExperience(0);
				
			}
		}
		return false;
		
	}

}
