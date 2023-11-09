package com.majruszsdifficulty.blocks;

import com.majruszsdifficulty.MajruszsDifficulty;
import com.mlib.level.BlockHelper;
import com.mlib.math.AnyPos;
import com.mlib.time.TimeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class FragileEndStone extends Block {
	public FragileEndStone() {
		super( Properties.of().mapColor( MapColor.SAND ).strength( 0.0f, 0.75f ).sound( SoundType.STONE ) );
	}

	@Override
	public void fallOn( Level level, BlockState blockState, BlockPos blockPos, Entity entity, float distance ) {
		if( distance > 1.0f && entity instanceof Player player ) {
			FragileEndStone.destroy( level, blockState, blockPos, player );
		}
	}

	@Override
	public void updateEntityAfterFallOn( BlockGetter block, Entity entity ) {}

	private static void destroy( Level level, BlockState blockState, BlockPos blockPos, Player player ) {
		Block block = blockState.getBlock();
		block.playerWillDestroy( level, blockPos, blockState, player );
		level.setBlockAndUpdate( blockPos, Blocks.AIR.defaultBlockState() );

		TimeHelper.nextTick( delay->{
			for( Direction direction : Direction.values() ) {
				BlockPos offset = AnyPos.from( blockPos ).add( direction ).block();
				BlockState neighbor = BlockHelper.getState( level, offset );
				if( neighbor.is( block ) ) {
					FragileEndStone.destroy( level, blockState, offset, player );
				}
			}
		} );
	}

	public static class Item extends BlockItem {
		public Item() {
			super( MajruszsDifficulty.FRAGILE_END_STONE.get(), new Properties().stacksTo( 64 ) );
		}
	}
}
