package ommina.biomediversity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import ommina.biomediversity.IProxy;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.plug.energy.PlugEnergyScreen;
import ommina.biomediversity.blocks.receiver.ReceiverScreen;
import ommina.biomediversity.blocks.transmitter.TransmitterScreen;

public class ClientProxy implements IProxy {

//region Overrides

    @Override
    public void init() {

        ScreenManager.registerFactory( ModBlocks.RECEIVER_CONTAINER, ReceiverScreen::new );
        ScreenManager.registerFactory( ModBlocks.PLUG_ENERGY_CONTAINER, PlugEnergyScreen::new );
        ScreenManager.registerFactory( ModBlocks.TRANSMITTER_CONTAINER, TransmitterScreen::new );

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

