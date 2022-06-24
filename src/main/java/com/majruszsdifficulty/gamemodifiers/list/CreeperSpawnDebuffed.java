package com.majruszsdifficulty.gamemodifiers.list;

import com.majruszsdifficulty.GameStage;
import com.majruszsdifficulty.gamemodifiers.CustomConditions;
import com.mlib.Random;
import com.mlib.gamemodifiers.Config;
import com.majruszsdifficulty.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnSpawnedContext;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Creeper;

public class CreeperSpawnDebuffed extends GameModifier {
	static final Config.Effect[] EFFECTS;
	static final OnSpawnedContext ON_SPAWNED = new OnSpawnedContext();

	static {
		EFFECTS = new Config.Effect[]{
			new Config.Effect( "Weakness", ()->MobEffects.WEAKNESS, 0, 60.0 ),
			new Config.Effect( "Slowness", ()->MobEffects.MOVEMENT_SLOWDOWN, 0, 60.0 ),
			new Config.Effect( "MiningFatigue", ()->MobEffects.DIG_SLOWDOWN, 0, 60.0 ),
			new Config.Effect( "Saturation", ()->MobEffects.SATURATION, 0, 60.0 )
		};

		ON_SPAWNED.addCondition( new CustomConditions.GameStage( GameStage.Stage.NORMAL ) );
		ON_SPAWNED.addCondition( new CustomConditions.CRDChance( 0.375 ) );
		ON_SPAWNED.addCondition( new Condition.Excludable() );
		ON_SPAWNED.addCondition( new Condition.ContextOnSpawned( data->data.target instanceof Creeper ) );
		ON_SPAWNED.addConfigs( EFFECTS );
	}

	public CreeperSpawnDebuffed() {
		super( GameModifier.DEFAULT, "CreeperSpawnDebuffed", "Creeper may spawn with negative effects applied.", ON_SPAWNED );
	}

	@Override
	public void execute( Object data ) {
		if( data instanceof OnSpawnedContext.Data spawnedData ) {
			Random.nextRandom( EFFECTS ).apply( spawnedData.target );
		}
	}
}
