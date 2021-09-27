package com.majruszs_difficulty;

import com.majruszs_difficulty.entities.TankEntity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

/** Handling connection between server and client. */
public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static SimpleChannel CHANNEL;

	public static void registerPacket( final FMLCommonSetupEvent event ) {
		CHANNEL = NetworkRegistry.newSimpleChannel( MajruszsDifficulty.getLocation( "main" ), ()->PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		);
		CHANNEL.registerMessage( 0, TankEntity.SpecialAttackMessage.class, TankEntity.SpecialAttackMessage::encode,
			TankEntity.SpecialAttackMessage::new, TankEntity.SpecialAttackMessage::handle
		);
		// CHANNEL.registerMessage( 1, VelocityMessage.class, VelocityMessage::encode, VelocityMessage::new, VelocityMessage::handle );
	}
}