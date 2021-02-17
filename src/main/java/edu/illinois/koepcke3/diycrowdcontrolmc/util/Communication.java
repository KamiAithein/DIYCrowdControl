package edu.illinois.koepcke3.diycrowdcontrolmc.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextComponentUtils;

/**
 * Utility class dealing with Communication with player and server
 */
public final class Communication {
    public static void messagePlayer(PlayerEntity player, String message){
        player.sendMessage(TextComponentUtils.toTextComponent(()->message));
    }
}
