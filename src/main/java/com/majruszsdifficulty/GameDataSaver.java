package com.majruszsdifficulty;

import com.majruszsdifficulty.gamemodifiers.list.IncreaseGameStage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

/** Stores information about current game stage in level. */
public class GameDataSaver extends SavedData {
	public static final String DATA_NAME = MajruszsDifficulty.MOD_ID;
	private static final String COMPOUND_STATE_TAG = "MajruszsDifficultyCompound";
	private static final String DIFFICULTY_STATE_TAG = "DifficultyState";
	private CompoundTag DATA = new CompoundTag();

	public GameDataSaver( boolean loadDefaultStateFromConfig ) {
		if( loadDefaultStateFromConfig ) {
			GameStage.changeStage( IncreaseGameStage.getDefaultGameStage(), null );
		}
	}

	public GameDataSaver() {
		this( true );
	}

	@Override
	public CompoundTag save( CompoundTag nbt ) {
		this.DATA.putInt( DIFFICULTY_STATE_TAG, GameStage.getCurrentStage().ordinal() );

		nbt.put( COMPOUND_STATE_TAG, this.DATA );
		return nbt;
	}

	public static GameDataSaver load( CompoundTag nbt ) {
		GameDataSaver gameData = new GameDataSaver( false );
		gameData.DATA = nbt.getCompound( COMPOUND_STATE_TAG );
		gameData.updateGameStage();

		return gameData;
	}

	public void updateGameStage() {
		GameStage.changeStage( GameStage.convertIntegerToStage( this.DATA.getInt( DIFFICULTY_STATE_TAG ) ), null );
	}
}
