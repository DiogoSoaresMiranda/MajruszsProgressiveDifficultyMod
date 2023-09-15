package com.majruszsdifficulty.features;

import com.majruszsdifficulty.gamestage.GameStage;
import com.majruszsdifficulty.Registries;
import com.majruszsdifficulty.contexts.base.CustomConditions;
import com.mlib.modhelper.AutoInstance;
import com.mlib.config.ConfigGroup;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.ModConfigs;
import com.mlib.contexts.OnDamaged;
import com.mlib.levels.LevelHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;

@AutoInstance
public class EndermanTeleportAttack {
	public EndermanTeleportAttack() {
		ConfigGroup group = ModConfigs.registerSubgroup( Registries.Groups.DEFAULT )
			.name( "EndermanTeleport" )
			.comment( "Enderman attack may teleport the player somewhere nearby." );

		OnDamaged.listen( this::teleportPlayerRandomly )
			.addCondition( CustomConditions.gameStageAtLeast( GameStage.MASTER ) )
			.addCondition( Condition.isServer() )
			.addCondition( Condition.chanceCRD( 0.5, true ) )
			.addCondition( Condition.excludable() )
			.addCondition( Condition.predicate( data->data.attacker instanceof EnderMan ) )
			.addCondition( Condition.predicate( data->data.source.getDirectEntity() == data.attacker ) )
			.insertTo( group );
	}

	private void teleportPlayerRandomly( OnDamaged.Data data ) {
		LivingEntity target = data.target;
		if( LevelHelper.teleportNearby( target, data.getServerLevel(), 6.0 ) && target instanceof ServerPlayer player ) {
			Registries.HELPER.triggerAchievement( player, "enderman_teleport_attack" );
		}
	}
}