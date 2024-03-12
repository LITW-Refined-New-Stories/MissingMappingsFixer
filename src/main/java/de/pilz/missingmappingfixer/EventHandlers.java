package de.pilz.missingmappingfixer;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class EventHandlers {

    @SubscribeEvent
    public void onFMLMissingMappingsEvent(FMLMissingMappingsEvent event) {
        List<MissingMapping> mappings = event.getAll();
        HashMap<String, Item> newItemMappings = new HashMap<String, Item>();
        HashMap<String, Block> newBlockMappings = new HashMap<String, Block>();

        // Get new item mappings
        for (String newMapping : GeneralConfig.mappings) {
            String[] names = newMapping.split("\|");
            if (names.length >= 3) {
                switch (names[0]) {
                    case "item": {
                        Item newItem = GameRegistry.findItem(names[1], names[2]);
                        if (newItem != null) {
                            newItemMappings.put(names[0], newItem);
                        }
                        break;
                    }
                    case "block": {
                        Block newBlock = GameRegistry.findBlock(names[1], names[2]);
                        if (newBlock != null) {
                            newBlockMappings.put(names[0], newBlock);
                        }
                        break;
                    }
                }
            }
        }

        // Try remap missing mappings
        for (MissingMapping mapping : mappings) {
            if (mapping.type == GameRegistry.Type.ITEM) {
                if (newItemMappings.containsKey(mapping.name)) {
                    mapping.remap(newItemMappings.get(mapping.name));
                }
            } else if (mapping.type == GameRegistry.Type.BLOCK) {
                if (newBlockMappings.containsKey(mapping.name)) {
                    mapping.remap(newBlockMappings.get(mapping.name));
                }
            }
        }
    }
}
