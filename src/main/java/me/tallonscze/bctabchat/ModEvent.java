package me.tallonscze.bctabchat;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvent {

    @Mod.EventBusSubscriber(modid = Bctabchat.MODID)
    public static class ForgeEvents{

        @SubscribeEvent
        public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event){
            Component playerName = event.getEntity().getDisplayName();
            String sPlayerName = playerName.getString();
            User user = LuckPermsProvider.get().getUserManager().getUser(sPlayerName);
            String lPrefix = "";
            if (user != null){
                lPrefix = user.getCachedData().getMetaData().getPrefix();
            }
            if (lPrefix != null && lPrefix.contains("&")){
                lPrefix = lPrefix.replace("&", "§");
            }
            Component prefix = Component.literal(lPrefix + " " + sPlayerName);
            event.setDisplayName(prefix);
            event.getEntity().refreshDisplayName();

        }


        @SubscribeEvent
        public static void onChatNameFormat(PlayerEvent.NameFormat event){
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

    }
}
