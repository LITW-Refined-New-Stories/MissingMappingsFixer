package de.pilz.missingmappingfixer;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

@Config(modid = MissingMappingsFixer.MODID)
public class GeneralConfig {

    @Config.Comment("Defines block mappings to remap. Each line defines the old mapping and then the new mapping.\nExample: item|tconstruct:tourch|minecraft:tourch")
    @Config.DefaultStringList({})
    @Config.RequiresMcRestart()
    public static String[] mappings;

    public static void init() {
        try {
            ConfigurationManager.registerConfig(GeneralConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }
}
