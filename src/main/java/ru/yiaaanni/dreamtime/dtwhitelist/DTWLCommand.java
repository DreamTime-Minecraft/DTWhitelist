package ru.yiaaanni.dreamtime.dtwhitelist;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import ru.yiaaanni.dreamtime.dtwhitelist.Utils.DTWLServer;

public class DTWLCommand extends Command {

    public DTWLCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("dtwl.admin")) {
            sender.sendMessage(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&',DTWhitelist.cfg.getString("messages.noperm")
                            .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
            return;
        }

        boolean help = false;

        if(args.length == 0 || args.length == 1 || args.length == 2) {
            help = true;
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("server")) {
                if(args[1].equalsIgnoreCase("perm")) {
                    String serverId = args[2];
                    DTWLServer server = null;

                    for(DTWLServer se : DTWhitelist.servers) {
                        if(se.getId().equalsIgnoreCase(serverId)) {
                            server = se;
                            continue;
                        }
                    }

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

                    sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "§7Охранник Балто §8>> §fТеперь сервер " +
                                    (server.isPerm() ? "§сзакрыт" : "§aоткрыт") + "§f для посещения по списку!")));

                } else if(args[1].equalsIgnoreCase("enabled")) {
                    String serverId = args[2];
                    DTWLServer server = null;

                    for(DTWLServer se : DTWhitelist.servers) {
                        if(se.getId().equalsIgnoreCase(serverId)) {
                            server = se;
                            continue;
                        }
                    }

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

                    sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "§7Охранник Балто §8>> §fТеперь сервер " +
                                    (server.isPerm() ? "§сзакрыт" : "§aоткрыт") + "§f для всеобщего посещения!")));
                } else if(args[1].equalsIgnoreCase("test")) {
                    String serverId = args[2];
                    DTWLServer server = null;

                    for(DTWLServer se : DTWhitelist.servers) {
                        if(se.getId().equalsIgnoreCase(serverId)) {
                            server = se;
                            continue;
                        }
                    }

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

                    sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "§7Охранник Балто §8>> §fТеперь сервер " +
                                    (server.isPerm() ? "§св тесте" : "§aбез тестов") + "§f!")));
                }
            } else {
                help = true;
            }
        } else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("server")) {
                if(args[1].equalsIgnoreCase("reason")) {
                    String serverId = args[2];

                    DTWLServer server = null;

                    for(DTWLServer se : DTWhitelist.servers) {
                        if(se.getId().equalsIgnoreCase(serverId)) {
                            server = se;
                            continue;
                        }
                    }

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
                    sender.sendMessage(TextComponent.fromLegacyText(
                            ChatColor.translateAlternateColorCodes('&',("{prefix}&aНовая причина:&f "+reason)
                                    .replace("{prefix}",DTWhitelist.cfg.getString("messages.prefix")))));
                }
            } else if(args[0].equalsIgnoreCase("player")) {
                if(args[1].equalsIgnoreCase("add")) {
                    String serverId = args[2];

                    DTWLServer server = null;

                    for(DTWLServer se : DTWhitelist.servers) {
                        if(se.getId().equalsIgnoreCase(serverId)) {
                            server = se;
                            continue;
                        }
                    }

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
                } else if(args[1].equalsIgnoreCase("remove")) {
                    String serverId = args[2];

                    DTWLServer server = null;

                    for(DTWLServer se : DTWhitelist.servers) {
                        if(se.getId().equalsIgnoreCase(serverId)) {
                            server = se;
                            continue;
                        }
                    }

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
                } else {
                    help = true;
                }
            } else {
                help = true;
            }
        }

        if(help) {
            for(String str : DTWhitelist.cfg.getStringList("messages.help")) {
                sender.sendMessage(TextComponent.fromLegacyText(str));
            }
        }
    }
}
