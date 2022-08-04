package com.majruszsdifficulty.commands;

import com.majruszsdifficulty.GameStage;
import com.mlib.commands.IRegistrableCommand;
import com.mlib.commands.PositionCommand;
import com.mlib.levels.LevelHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Command that displays clamped regional difficulty at given position. */
public class GetClampedRegionalDifficultyCommands extends PositionCommand implements IRegistrableCommand {
	/** Sends information about current clamped regional difficulty to the caller. */
	@Override
	protected int handleCommand( CommandContext< CommandSourceStack > context, CommandSourceStack source, Vec3 position ) {
		source.sendSuccess( getMessage( source.getLevel(), position ), true );
		return 0;
	}

	/** Registers this command. */
	@Override
	public void register( CommandDispatcher< CommandSourceStack > commandDispatcher ) {
		Data commandData1 = new Data( hasPermission( 0 ), literal( "clampedregionaldifficulty" ) );
		registerLocationCommand( commandDispatcher, commandData1 );

		Data commandData2 = new Data( hasPermission( 0 ), literal( "crd" ) );
		registerLocationCommand( commandDispatcher, commandData2 );
	}

	public MutableComponent getMessage( Level level, Vec3 position ) {
		String clampedRegionalDifficulty = String.format( "%.3f", LevelHelper.getClampedRegionalDifficulty( level, position ) );
		String stateModifier = String.format( "%.2f", GameStage.getStageModifier() );
		String finalDifficulty = String.format( "%.3f", GameStage.getRegionalDifficulty( level, position ) );
		MutableComponent formula = new TranslatableComponent( "commands.clampedregionaldifficulty.formula", stateModifier, finalDifficulty );
		String positionFormatted = CommandsHelper.getPositionFormatted( position );

		switch( GameStage.getCurrentStage() ) {
			case MASTER:
				formula.withStyle( GameStage.MASTER_MODE_COLOR, ChatFormatting.BOLD );

				return new TranslatableComponent( "commands.clampedregionaldifficulty", clampedRegionalDifficulty, formula, positionFormatted );
			case EXPERT:
				formula.withStyle( GameStage.EXPERT_MODE_COLOR, ChatFormatting.BOLD );

				return new TranslatableComponent( "commands.clampedregionaldifficulty", clampedRegionalDifficulty, formula, positionFormatted );
			default:
				return new TranslatableComponent( "commands.clampedregionaldifficulty", clampedRegionalDifficulty, "", positionFormatted );
		}
	}
}
