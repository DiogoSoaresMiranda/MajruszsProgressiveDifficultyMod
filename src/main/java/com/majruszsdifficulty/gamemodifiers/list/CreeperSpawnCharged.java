package com.majruszsdifficulty.gamemodifiers.list;

import com.majruszsdifficulty.GameStage;
import com.majruszsdifficulty.gamemodifiers.CustomConditions;
import com.mlib.gamemodifiers.GameModifier;import com.majruszsdifficulty.Registries;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnSpawned;
import com.mlib.gamemodifiers.contexts.OnSpawnedContext;
import com.mlib.gamemodifiers.data.OnSpawnedData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;

public class CreeperSpawnCharged extends GameModifier {
	public CreeperSpawnCharged() {
		super( Registries.Modifiers.DEFAULT, "CreeperSpawnCharged", "Creeper may spawn charged." );

		OnSpawned.Context onSpawned = new OnSpawned.Context( this::chargeCreeper );
		onSpawned.addCondition( new CustomConditions.GameStage( GameStage.Stage.NORMAL ) )
			.addCondition( new CustomConditions.CRDChance( 0.125, true ) )
			.addCondition( new Condition.Excludable() )
			.addCondition( OnSpawned.IS_NOT_LOADED_FROM_DISK )
			.addCondition( data->data.level != null )
			.addCondition( data->data.target instanceof Creeper );

		this.addContext( onSpawned );
	}

	private void chargeCreeper( OnSpawned.Data data ) {
		assert data.level != null;
		Creeper creeper = ( Creeper )data.target;
		LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create( data.level );
		if( lightningBolt != null ) {
			creeper.thunderHit( data.level, lightningBolt );
			creeper.clearFire();
		}
	}
}
