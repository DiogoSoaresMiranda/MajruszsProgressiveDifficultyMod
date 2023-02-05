package com.majruszsdifficulty.undeadarmy.data;


import com.mlib.data.SerializableStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

public class MobDef extends SerializableStructure {
	public EntityType< ? > type;
	public int count = 1;
	public ResourceLocation equipment = LootTable.EMPTY.getLootTableId();

	public MobDef() {
		this.define( "type", ()->this.type, x->this.type = x );
		this.define( "count", ()->this.count, x->this.count = x );
		this.define( "equipment", ()->this.equipment, x->this.equipment = x );
	}
}
