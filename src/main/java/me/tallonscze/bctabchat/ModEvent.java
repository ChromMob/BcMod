package me.tallonscze.bctabchat;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvent {

    @Mod.EventBusSubscriber(modid = Bctabchat.MODID)
    public class ForgeEvents{

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onTabListNameFormat(PlayerEvent.TabListNameFormat event){
            String playerName = event.getEntity().getDisplayName().getString();
            updateTabName(playerName);
            Player player = event.getEntity();
            player.sendSystemMessage(Component.literal("DisplayName Sets"));
            player.refreshDisplayName();
            System.out.println("DisplayName Sets");
        }


        @SubscribeEvent
        public void onChatNameFormat(PlayerEvent.NameFormat event){
            try {
                Component playerName = event.getEntity().getName();
                String stringPlayerName = playerName.getString();
                User user = LuckPermsProvider.get().getUserManager().getUser(stringPlayerName);
                String lPrefix = " ";
                if (user != null){
                    lPrefix = user.getCachedData().getMetaData().getPrefix();
                }
                if (lPrefix != null && lPrefix.contains("&")){
                    lPrefix = lPrefix.replace("&", "§");
                }
                Component finish = Component.literal(lPrefix + " " + stringPlayerName);
                event.setDisplayname(finish);
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        @SubscribeEvent
        public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
            ServerPlayer sPlayer = (ServerPlayer) event.getEntity();
            String playerName = event.getEntity().getName().getString();
            updateTabName(playerName);
            Player player = event.getEntity();
            player.refreshDisplayName();
            System.out.println("Update playerTabName - " + playerName);

            Component header = Component.literal("BurningCube.EU");
            Component footer = Component.literal("Discord - ");
            sPlayer.setTabListHeaderFooter(header, footer);
        }


    }

    public void updateTabName(String name){
        PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(name);
        System.out.println("Update playerTabName - " + name);
        if (info == null){
            return;
        }
        User user = LuckPermsProvider.get().getUserManager().getUser(name);
        String lPrefix = "";
        if (user != null){
            lPrefix = user.getCachedData().getMetaData().getPrefix();
        }
        if (lPrefix != null && lPrefix.contains("&")){
            lPrefix = lPrefix.replace("&", "§");
        }


        System.out.println("Update playerTabName - " + name);
        Component finalName = Component.literal(lPrefix + " " + name);
        info.setTabListDisplayName(finalName);

    }
}
