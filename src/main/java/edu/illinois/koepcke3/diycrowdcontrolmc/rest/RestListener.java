package edu.illinois.koepcke3.diycrowdcontrolmc.rest;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.get;
import static spark.Spark.halt;
import static edu.illinois.koepcke3.diycrowdcontrolmc.util.Communication.messagePlayer;
import static edu.illinois.koepcke3.diycrowdcontrolmc.util.Entity.getEntityByName;
import static edu.illinois.koepcke3.diycrowdcontrolmc.util.Entity.spawnEntity;
import static edu.illinois.koepcke3.diycrowdcontrolmc.util.Game.adjustHearts;

/**
 * Listens to a KnockOffCrowdControl Rest Resource. Must have logic for all commands in its enum
 */
public class RestListener {

    private static final String QUERY_MESSAGE = "message";
    private static final String ENTITY_ARGS = "entity";
    private static final String PASSENGER_ARGS = "passenger";
    private static final String ENTITY_NAME_ARGS = "entityname";
    private static final String PASSENGER_NAME_ARGS = "passengername";

    /**
     * The MC server this object modifies
     */
    private MinecraftServer server;

    protected RestListener(@Nullable MinecraftServer server){
        this.restInit();
    }

    public RestListener(){
        this(null);
    }

    /**
     * Sets up all REST resources
     */
    private void restInit(){

        get("/command/:command", (req, res) -> {
            String commandString = req.params("command");

            String message = req.queryParamOrDefault(QUERY_MESSAGE, "no message");

            Map<String, String> params = new HashMap<>();

            params.put(ENTITY_ARGS, req.queryParamOrDefault(ENTITY_ARGS, null));
            params.put(PASSENGER_ARGS, req.queryParamOrDefault(PASSENGER_ARGS, null));
            params.put(ENTITY_NAME_ARGS, req.queryParamOrDefault(ENTITY_NAME_ARGS, null));
            params.put(PASSENGER_NAME_ARGS, req.queryParamOrDefault(PASSENGER_NAME_ARGS, null));


            //Try to parse command
            try{
                Command command = Command.valueOf(commandString);
                command.action(getPlayer(this.server), message + commandString+ "with args" + params.toString(), params);
                res.status(200);
                return message + commandString;
            }
            catch(Error e){
                halt(404, "not a command bruvh");
                return null;
            }
        });
    }

    private static PlayerEntity getPlayer(MinecraftServer server){
        return server.getPlayerList().getPlayers().get(0);
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    /**
     * All possible commands to be used on player
     */
    private enum Command{
        JUMP {
            public void action(PlayerEntity player, String message, Map<String, String> args){
                player.jump();
                messagePlayer(player, message);
            }
        },
        KILL {
            public void action(PlayerEntity player, String message, Map<String, String> args){
                player.setHealth(0);
                messagePlayer(player, message);
            }
        },
        GIVE_HEART {
            public void action(PlayerEntity player, String message, Map<String, String> args){
                player.setHealth(adjustHearts(player.getHealth(), 1));
                messagePlayer(player, message);
            }
        },
        TAKE_HEART {
            public void action(PlayerEntity player, String message, Map<String, String> args){
                player.setHealth(adjustHearts(player.getHealth(), -1));
                messagePlayer(player, message);
            }
        },
        SPAWN {
            public void action(PlayerEntity player, String message, Map<String, String> args){
                World world = player.world;
                messagePlayer(player, player.world.getDimension().getType().toString());

                String entityName = args.get(ENTITY_NAME_ARGS);
                entityName = entityName == null || entityName.toLowerCase().equals("null") ? null : entityName;
                String passengerName = args.get(PASSENGER_NAME_ARGS);
                passengerName = passengerName == null || passengerName.toLowerCase().equals("null") ? null : passengerName;

                String entityArgs = args.get(ENTITY_ARGS);
                if(entityArgs != null) {
                    Optional<Map.Entry<ResourceLocation, EntityType<?>>> toSpawnOpt = getEntityByName(args.get(ENTITY_ARGS));
                    if(toSpawnOpt.isPresent()){
                        Entity entity = spawnEntity(toSpawnOpt.get().getValue(), entityName, world, new BlockPos(player.posX + 1, player.posY + 1, player.posZ + 1));
                        messagePlayer(player, message);

                        String passengerArgs = args.get(PASSENGER_ARGS);
                        if(passengerArgs != null) {
                            Optional<Map.Entry<ResourceLocation, EntityType<?>>> passengerOpt = getEntityByName(args.get(PASSENGER_ARGS));

                            if(passengerOpt.isPresent()){
                                Entity passenger = spawnEntity(passengerOpt.get().getValue(), passengerName, world, new BlockPos(player.posX + 1, player.posY + 1, player.posZ + 1));
                                passenger.startRiding(entity, true);
                            }
                        }

                    }
                    else {
                        messagePlayer(player, "couldn't find "+ args.get(ENTITY_ARGS));
                    }





                }







            }
        }
        ;

        /**
         * The action performed by this enum on the player
         * @param player the player to have an action done on
         * @param message the message to send the player when doing this action
         * @param args optional arguments
         */
        abstract void action(PlayerEntity player, String message, Map<String, String> args);




    }


}
