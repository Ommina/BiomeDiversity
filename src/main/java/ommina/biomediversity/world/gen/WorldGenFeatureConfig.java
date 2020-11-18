package ommina.biomediversity.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;

import java.util.Random;

public class WorldGenFeatureConfig implements IFeatureConfig, IRegionalGemsConfig {

    /*

    public static final Codec<WorldGenFeatureConfig> CODEC = RecordCodecBuilder.create( instance ->
         instance.group(
              Gems.Set.CODEC.fieldOf( "gem_set" ).forGetter( config -> config.gemSet ),
              Codec.INT.fieldOf( "size" ).forGetter( config -> config.size ),
              Codec.INT.fieldOf( "region_size" ).forGetter( config -> config.regionSize ),
              RuleTest.field_237127_c_.fieldOf( "target" ).forGetter( config -> config.target )
         ).apply( instance, (WorldGenFeatureConfig::new) ) );

    public final int size;
    public final int regionSize;
    public final RuleTest target;
    protected final Gems.Set gemSet;

    public WorldGenFeatureConfig( Gems.Set gemSet, int size, int regionSize ) {
        this( gemSet, size, regionSize, new TagMatchRuleTest( GemsWorldFeatures.getOreGenTargetBlock( gemSet ) ) );
    }

    public WorldGenFeatureConfig( Gems.Set gemSet, int size, int regionSize, RuleTest target ) {
        this.gemSet = gemSet;
        this.size = size;
        this.regionSize = regionSize;
        this.target = target;
    }

    public Gems selectGem( ISeedReader world, BlockPos pos, Random random ) {
        return selectGem( world, pos, random, gemSet, regionSize );
    }

    */

}
