package ommina.biomediversity.config;

public class Constants {

    public static final int CLUSTER_FLUID_CONSUMPTION = 20; // Combined with delay, it is effectively 1mb / tick
    public static final int CLUSTER_TICK_DELAY = 20;        // One second per cycle
    public static final int CLUSTER_SEARCH_ON_LOOP = 5;
    public static final int CLUSTER_MAX_SEARCH_COUNT = 36000 / (Constants.CLUSTER_TICK_DELAY * CLUSTER_SEARCH_ON_LOOP); // ~30min

    public static final float FLUID_DIVERSITY = 0;     // TODO: Probably would be interesting enabling this some day, but for now, we'll leave the fluid count neutral.  Receiver fluid penalities achieve much the same thing,
    public static final float FLUID_ADJUSTMENT = 0;    //       although as a negative, instead of a positive

    public static final float COLLECTOR_EQUILIBRIUM_THRESHOLD = 0.25f;
    public static final float COLLECTOR_POWER_TO_FLUID_MULTIPLIER = 0.0001f;  // 1mb of Collector Fluid produced per rf/cycle * this value.  Maybe move it to Config also.
    public static final int COLLECTOR_MINIMUM_ENERGY_TO_PRODUCE_FLUID = 500;
    public static final int COLLECTOR_EQUILIBRIUM_BONUS_BUCKET = 3;
    public static final int COLLECTOR_OUTER_TANK_CAPACITY = 64000;
    public static final int COLLECTOR_INNER_TANK_CAPACITY = 32000;

    public static final int RF_RENDER_STEP_COUNT = 60; // Used to specifiy how frequently the client is updated with new stored RF values.  maxStored/Step_Count becomes the BroadcastHelper minimumDelta

    public static final float DEFAULT_TILE_ENTITY_HARDNESS = 3.2f;

}
