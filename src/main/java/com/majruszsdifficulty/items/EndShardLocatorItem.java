package com.majruszsdifficulty.items;

import com.majruszsdifficulty.Registries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/** Item for locating End Shards. */
public class EndShardLocatorItem extends Item {
	private static final String COUNTER_TAG = "EndShardLocatorCounter";
	private static final String POSITION_X_TAG = "EndShardLocatorX";
	private static final String POSITION_Y_TAG = "EndShardLocatorY";
	private static final String POSITION_Z_TAG = "EndShardLocatorZ";
	private static final int COORDINATE_FACTOR = 30;
	private static final float INVALID_DISTANCE = 9001.0f;

	public EndShardLocatorItem() {
		super( new Properties().tab( Registries.ITEM_GROUP ).rarity( Rarity.UNCOMMON ).stacksTo( 1 ) );
	}

	@OnlyIn( Dist.CLIENT )
	public static float calculateDistanceToEndShard( ItemStack itemStack, @Nullable ClientLevel clientWorld,
		@Nullable LivingEntity entity, int seed
	) {
		if( !( entity instanceof Player player ) || player.getInventory().findSlotMatchingItem( itemStack ) == -1 )
			return INVALID_DISTANCE;

		Level world = entity.level;
		Vec3 entityPosition = entity.position();

		CompoundTag data = itemStack.getOrCreateTag();
		int counter = data.getInt( COUNTER_TAG );
		BlockPos nearestEndShard = new BlockPos( data.getInt( POSITION_X_TAG ), data.getInt( POSITION_Y_TAG ), data.getInt( POSITION_Z_TAG ) );
		BlockState currentBlockState = world.getBlockState( nearestEndShard );
		boolean isValid = currentBlockState.getBlock() == Registries.ENDERIUM_SHARD_ORE.get();

		double closestDistance = entityPosition.distanceToSqr( Vec3.atCenterOf( nearestEndShard ) );
		BlockPos endShardOre = findNearestEndShard( world, entityPosition, counter - COORDINATE_FACTOR );
		if( endShardOre != null ) {
			if( !isValid ) {
				nearestEndShard = endShardOre;
			} else {
				double currentDistance = entityPosition.distanceToSqr( Vec3.atCenterOf( endShardOre ) );
				if( currentDistance < closestDistance )
					nearestEndShard = endShardOre;
			}
		}

		data.putInt( COUNTER_TAG, ( counter + 1 ) % ( 2 * COORDINATE_FACTOR ) );
		data.putInt( POSITION_X_TAG, nearestEndShard.getX() );
		data.putInt( POSITION_Y_TAG, nearestEndShard.getY() );
		data.putInt( POSITION_Z_TAG, nearestEndShard.getZ() );

		return isValid ? ( float )closestDistance : INVALID_DISTANCE;
	}

	/**
	 Finds the nearest End Shard Ore in certain distance. This function looks for End Shard Ore in this area:
	 x -> entity position +- factor
	 y -> function parameter
	 z -> entity position +- factor
	 It only checks one 'y' coordinate to reduce time complexity.
	 */
	@Nullable
	private static BlockPos findNearestEndShard( Level world, Vec3 entityPosition, int yFactor ) {
		int y = ( int )( entityPosition.y + yFactor );
		double closestDistance = INVALID_DISTANCE;
		BlockPos blockPosition = new BlockPos( 0, 0, 0 );

		for( int x = ( int )( entityPosition.x() - COORDINATE_FACTOR ); x < entityPosition.x() + COORDINATE_FACTOR; x++ )
			for( int z = ( int )( entityPosition.z() - COORDINATE_FACTOR ); z < entityPosition.z() + COORDINATE_FACTOR; z++ ) {
				BlockPos testPosition = new BlockPos( x, y, z );
				if( world.getBlockState( testPosition ).getBlock() == Registries.ENDERIUM_SHARD_ORE.get() ) {
					double distance = entityPosition.distanceToSqr( Vec3.atCenterOf( testPosition ) );
					if( distance < closestDistance ) {
						closestDistance = distance;
						blockPosition = testPosition;
					}
				}
			}

		return closestDistance == INVALID_DISTANCE ? null : blockPosition;
	}
}
