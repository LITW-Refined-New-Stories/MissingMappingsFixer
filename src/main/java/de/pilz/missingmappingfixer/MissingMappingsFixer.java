package de.pilz.missingmappingfixer;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
    modid = MissingMappingsFixer.MODID,
    version = Tags.VERSION,
    name = "Missing Mappings Fixer",
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*")
public class MissingMappingsFixer {

    public static final String MODID = "missingmappingfixer";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GeneralConfig.init();
        MissingMappingsFixer.LOG.info("I am Missing Mappings Fixer at version " + Tags.VERSION);
    }

    @Mod.EventHandler
    public void onFMLMissingMappingsEvent(FMLMissingMappingsEvent event) {
        List<MissingMapping> mappings = event.getAll();
        HashMap<String, Item> newItemMappings = new HashMap<String, Item>();
        HashMap<String, Block> newBlockMappings = new HashMap<String, Block>();

        // Get new item mappings
        for (String newMapping : GeneralConfig.mappings) {
            String[] names = newMapping.split("\\|");
            if (names.length >= 3) {
                String[] newName = names[2].split("\\:");
                switch (names[0]) {
                    case "item": {
                        Item newItem = GameRegistry.findItem(newName[0], newName[1]);
                        if (newItem != null) {
                            newItemMappings.put(names[1], newItem);
                        }
                        break;
                    }
                    case "block": {
                        Block newBlock = GameRegistry.findBlock(newName[0], newName[1]);
                        if (newBlock != null) {
                            newBlockMappings.put(names[1], newBlock);
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
