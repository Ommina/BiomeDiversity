package ommina.biomediversity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import ommina.biomediversity.IProxy;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.receiver.ReceiverScreen;

public class ClientProxy implements IProxy {

//region Overrides

    @Override
    public void init() {

        ScreenManager.registerFactory( ModBlocks.RECEIVER_CONTAINER, ReceiverScreen::new );

    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

//endregion Overrides

}

