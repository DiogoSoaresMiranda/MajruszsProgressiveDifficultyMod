package com.majruszsdifficulty.gamemodifiers.list;

import com.majruszsdifficulty.GameStage;
import com.majruszsdifficulty.gamemodifiers.CustomConditions;
import com.majruszsdifficulty.gamemodifiers.DifficultyModifier;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnDamagedContext;
import com.mlib.gamemodifiers.data.OnDamagedData;
import net.minecraft.world.entity.monster.Creeper;

public class CreeperChainReaction extends DifficultyModifier {
	public CreeperChainReaction() {
		super( DifficultyModifier.DEFAULT, "CreeperChainReaction", "Makes a Creeper ignite once any other Creeper explode nearby." );

		OnDamagedContext onDamaged = new OnDamagedContext( this::igniteCreeper );
		onDamaged.addCondition( new CustomConditions.GameStage( GameStage.Stage.EXPERT ) )
			.addCondition( new Condition.Excludable() )
			.addCondition( data->data.target instanceof Creeper && data.attacker instanceof Creeper );

		this.addContext( onDamaged );
	}

	private void igniteCreeper( OnDamagedData data ) {
		Creeper creeper = ( Creeper )data.target;
		creeper.ignite();
	}
}
