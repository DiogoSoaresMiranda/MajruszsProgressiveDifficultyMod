package com.majruszsdifficulty.gamemodifiers.list;

import com.majruszsdifficulty.GameStage;
import com.majruszsdifficulty.gamemodifiers.CustomConditions;
import com.mlib.gamemodifiers.GameModifier;import com.majruszsdifficulty.Registries;
import com.majruszsdifficulty.gamemodifiers.configs.ProgressiveEffectConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnDamagedContext;
import com.mlib.gamemodifiers.data.OnDamagedData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Slime;

public class SlimeSlownessAttack extends GameModifier {
	final ProgressiveEffectConfig slowness = new ProgressiveEffectConfig( "", ()->MobEffects.MOVEMENT_SLOWDOWN, 0, 6.0 );

	public SlimeSlownessAttack() {
		super( Registries.Modifiers.DEFAULT, "SlimeSlownessAttack", "Shulker attack may inflict stackable blindness effect." );

		OnDamagedContext onDamaged = new OnDamagedContext( this::applyEffect );
		onDamaged.addCondition( new CustomConditions.GameStage( GameStage.Stage.EXPERT ) )
			.addCondition( new CustomConditions.CRDChance( 0.5, true ) )
			.addCondition( new Condition.Excludable() )
			.addCondition( data->data.attacker instanceof Slime )
			.addCondition( data->data.source.getDirectEntity() == data.attacker )
			.addConfig( this.slowness );

		this.addContext( onDamaged );
	}

	private void applyEffect( OnDamagedData data ) {
		this.slowness.apply( data.target );
	}
}
