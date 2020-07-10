package ru.yiaaanni.dreamtime.dtwhitelist;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import ru.yiaaanni.dreamtime.dtwhitelist.Utils.DTWLServer;

import java.util.*;

public class DTWLCommand extends Command implements TabExecutor {

    public DTWLCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("dtwl.admin")) {
            sender.sendMessage(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&',DTWhitelist.cfg.getString("messages.noperm")
                            .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
            return;
        }

        if(args.length == 0 || args.length == 2 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            sendHelp(sender);
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("server")) {
                if (args[1].equalsIgnoreCase("list")) {

                    String serverId = args[2];
                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if (server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                                .replace("{prefix}", DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }
                    String out = "&aСписок игроков в белом списке реалма {realm}: &8({count}) &7{players}";

                    String players = String.join(", ", server.getList().toArray(new String[0]));
                    out = out
                            .replace("{realm}", server.getId())
                            .replace("{players}", players)
                            .replace("{count}", String.valueOf(server.getList().size()));

                    sender.sendMessage(TextComponent.fromLegacyText(
                            ChatColor.translateAlternateColorCodes('&', out)
                    ));

                }
                else if(args[1].equalsIgnoreCase("perm")) {
                    String serverId = args[2];
                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if(server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }

                    if(server.isPerm()) {
                        server.setPerm(false);
                    } else {
                        server.setPerm(true);
                    }
                    server.save();
                    sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "§7Охранник Балто §8>> §fТеперь сервер " +
                                    (server.isPerm() ? "§сзакрыт" : "§aоткрыт") + "§f для посещения по списку!")));

                } else if(args[1].equalsIgnoreCase("enabled")) {
                    String serverId = args[2];
                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if(server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                        .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }

                    if(server.isEnabled()) {
                        server.setEnabled(false);
                    } else {
                        server.setEnabled(true);
                    }

                    server.save();
                    sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "§7Охранник Балто §8>> §fТеперь сервер " +
                                    (server.isEnabled() ? "§сзакрыт" : "§aоткрыт") + "§f для всеобщего посещения!")));
                } else if(args[1].equalsIgnoreCase("test")) {
                    String serverId = args[2];
                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if(server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                        .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }

                    if(server.isTest()) {
                        server.setTest(false);
                    } else {
                        server.setTest(true);
                    }

                    server.save();
                    sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "§7Охранник Балто §8>> §fТеперь сервер " +
                                    (server.isTest() ? "§св тесте" : "§aбез тестов") + "§f!")));
                }
            } else {
                sendHelp(sender);
            }
        } else if(args.length >= 4) {
            if(args[0].equalsIgnoreCase("server")) {
                if(args[1].equalsIgnoreCase("reason")) {
                    String serverId = args[2];

                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if(server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                        .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }

                    StringBuilder res = new StringBuilder();
                    for(int i = 3; i < args.length; i++) {
                        res.append(args[i]).append(" ");
                    }
                    String reason = res.toString();
                    reason = reason.substring(0, reason.length()-1);

                    server.setReason(reason);

                    server.save();
                    sender.sendMessage(TextComponent.fromLegacyText(
                            ChatColor.translateAlternateColorCodes('&',("{prefix}&aНовая причина:&f "+reason)
                                    .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                }
            } else if(args[0].equalsIgnoreCase("player")) {
                if(args[1].equalsIgnoreCase("add")) {
                    String serverId = args[2];
                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if(server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                                .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }

                    String name = args[3].toLowerCase();

                    if(server.getList().contains(name)) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cИгрок уже есть в списке!"));
                    } else {
                        server.getList().add(name);
                        sender.sendMessage(TextComponent.fromLegacyText("§aИгрок добавлен в список!"));
                    }

                    server.save();
                } else if(args[1].equalsIgnoreCase("remove")) {
                    String serverId = args[2];
                    DTWLServer server = DTWLServer.getServerForName(serverId);

                    if(server == null) {
                        sender.sendMessage(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&',
                                        DTWhitelist.cfg.getString("messages.servernotfound")
                                                .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                        return;
                    }

                    String name = args[3].toLowerCase();

                    if(!server.getList().contains(name)) {
                        sender.sendMessage(TextComponent.fromLegacyText("§cИгрока нет в списке!"));
                    } else {
                        server.getList().remove(name);
                        sender.sendMessage(TextComponent.fromLegacyText("§aИгрок убран из списка!"));
                    }

                    server.save();
                } else {
                    sendHelp(sender);
                }
            } else {
                sendHelp(sender);
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("save")) {
                for (DTWLServer server : DTWhitelist.servers) {
                    server.save(false);
                }
                DTWhitelist.ins.saveCfg();
                sender.sendMessage(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&',
                                "&aКонфиг сохранен!")));
            } else {
                sendHelp(sender);
            }
        } else {
            sendHelp(sender);
        }
    }

    private void sendHelp(CommandSender sender) {

        for(String str : DTWhitelist.cfg.getStringList("messages.help")) {
            sender.sendMessage(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&',str.
                            replace("{prefix}","&7Охранник Балто &8>> &f"))));
        }
    }

    private static final HashSet<String> completeArg1 = new HashSet<>(
            Arrays.asList(
                    "help",
                    "player",
                    "server"
            ));
    private static final HashSet<String> completeArg2Player = new HashSet<>(
            Arrays.asList(
                    "add",
                    "remove"
            ));
    private static final HashSet<String> completeArg2Server = new HashSet<>(
            Arrays.asList(
                    "perm",
                    "enabled",
                    "test",
                    "list",
                    "reason"
            ));

    private Set<String> getCompleter(String str, Set<String> completers) {
        Set<String> result = new HashSet<>();
        for (String completer : completers) {
            if (completer.toLowerCase().startsWith(str.toLowerCase())){
                result.add(completer);
            }
        }
        return result;
    }
    private Set<String> getCompleterPlayer(String str, Collection<ProxiedPlayer> completers) {
        Set<String> result = new HashSet<>();
        for (ProxiedPlayer player : completers) {
            if (player.getName().toLowerCase().startsWith(str.toLowerCase())){
                result.add(player.getName());
            }
        }
        return result;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        if(!sender.hasPermission("dtwl.admin")) {
            return Collections.emptySet();
        }
        if (args.length >= 1) {
            if (args.length == 1) {
                return getCompleter(args[0], completeArg1);
            }
            if (args[0].equalsIgnoreCase("player")) {
                if (args.length == 2) {
                    return getCompleter(args[1], completeArg2Player);
                } else if (args.length == 3) {
                    Set<String> keySet = new HashSet<>(ProxyServer.getInstance().getServers().keySet());
                    keySet.add("_bungee_");
                    return getCompleter(args[2], keySet);
                } else if (args.length == 4) {
                    if (args[1].equalsIgnoreCase("remove")) {
                        DTWLServer server = DTWLServer.getServerForName(args[2]);

                        Set<String> set;
                        if (server != null) {
                            set = server.getList();
                        } else {
                            set = new HashSet<>();
                        }
                        return getCompleter(args[3], set);
                    }
                    return getCompleterPlayer(args[3], ProxyServer.getInstance().getPlayers());
                }
            } else if (args[0].equalsIgnoreCase("server")) {
                if (args.length == 2) {
                    return getCompleter(args[1], completeArg2Server);
                } else if (args.length == 3) {

                    Set<String> keySet = new HashSet<>(ProxyServer.getInstance().getServers().keySet());
                    keySet.add("_bungee_");
                    return getCompleter(args[2], keySet);
                }
            }
        }
        return Collections.emptySet();
    }
}
