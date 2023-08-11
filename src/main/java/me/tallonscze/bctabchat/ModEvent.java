package me.tallonscze.bctabchat;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvent {

        @SubscribeEvent()
        public void onTabListNameFormat(PlayerEvent.TabListNameFormat event){
            try{
                System.out.println("Events Awoke onTabListNameFormat");
                String playerName = event.getEntity().getDisplayName().getString();
                updateTabName(playerName);
                Player player = event.getEntity();
                player.sendSystemMessage(Component.literal("DisplayName Sets"));
                player.refreshDisplayName();
                System.out.println("DisplayName Sets");
            } catch(Exception e){
                e.printStackTrace();

            }

        }


        @SubscribeEvent
        public void onChatNameFormat(PlayerEvent.NameFormat event){
            System.out.println("Events Awoke NameFormat");
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
            try{
                System.out.println("Events Awoke onPlayerJoin");
                ServerPlayer sPlayer = (ServerPlayer) event.getEntity();
                String playerName = event.getEntity().getName().getString();
                updateTabName(playerName);
                Player player = event.getEntity();
                player.refreshDisplayName();
                System.out.println("Update playerTabName - " + playerName);

                Component header = Component.literal("BurningCube.EU");
                Component footer = Component.literal("Discord - ");
                sPlayer.setTabListHeaderFooter(header, footer);
            }catch(Exception e)
            {
                e.printStackTrace();
            }

        }


    }

    public void updateTabName(String name){
        try{
            System.out.println("Awoke updateTabName");
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
        }catch (Exception e){
            e.printStackTrace();
        }
}
