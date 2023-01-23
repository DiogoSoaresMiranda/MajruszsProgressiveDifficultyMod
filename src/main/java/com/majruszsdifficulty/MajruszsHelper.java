package com.majruszsdifficulty;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MajruszsHelper {
	public static void addTranslatableText( List< Component > tooltip, String translationKey ) {
		tooltip.add( new TranslatableComponent( translationKey ).withStyle( ChatFormatting.GRAY ) );
	}

	public static void addTranslatableTexts( List< Component > tooltip, String... translationKeys ) {
		for( String translationKey : translationKeys )
			addTranslatableText( tooltip, translationKey );
	}

	/** Adds tooltip to list if advanced tooltips are enabled. */
	public static void addAdvancedTranslatableText( List< Component > tooltip, TooltipFlag flag, String translationKey ) {
		if( flag.isAdvanced() )
			addTranslatableText( tooltip, translationKey );
	}

	/** Adds multiple tooltips to list if advanced tooltips are enabled. */
	public static void addAdvancedTranslatableTexts( List< Component > tooltip, TooltipFlag flag, String... translationKeys ) {
		if( flag.isAdvanced() )
			for( String translationKey : translationKeys )
				addAdvancedTranslatableText( tooltip, flag, translationKey );
	}

	/** Returns formatted text with information that item is disabled. */
	public static MutableComponent getDisabledItemComponent() {
		return new TranslatableComponent( "majruszsdifficulty.items.disabled_tooltip" ).withStyle( ChatFormatting.RED, ChatFormatting.BOLD );
	}
}
