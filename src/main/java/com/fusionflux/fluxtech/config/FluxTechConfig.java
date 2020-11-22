package com.fusionflux.fluxtech.config;

import com.google.common.collect.ImmutableList;
import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.List;

public final class FluxTechConfig extends Config {


	public static class VALUES extends ConfigItemGroup {

		public static final ConfigItem<Double> HPD_LAUNCH_POWER = new ConfigItem<>("hpd_launch_power", 2D,
				"config.fluxtech.values.hpd_launch_power");
		public static final ConfigItem<Double> AEROARMOR_FLIGHT_BOOST = new ConfigItem<>("aeroarmor_flight_boost", 1.5D,
				"config.fluxtech.values.aeroarmor_flight_boost");
		public static final ConfigItem<Double> CRUSH_BOUNCE_MULTIPLIER = new ConfigItem<>("crush_bounce_multiplier", 1.5D,
				"config.fluxtech.values.crush_bounce_multiplier");

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(AEROARMOR_FLIGHT_BOOST,HPD_LAUNCH_POWER,CRUSH_BOUNCE_MULTIPLIER);

		public VALUES() {
			super(OPTIONS, "values");
		}

	}

	public static class ENABLED extends ConfigItemGroup {

		public static final ConfigItem<Boolean> ENABLED_HPD = new ConfigItem<>("handheld_propulsion_device_enabler", true,
				"config.fluxtech.enabled.handheld_propulsion_device_enabler");

		public static final ConfigItem<Boolean> ENABLED_HPD_LAUNCH_PREVENTER = new ConfigItem<>("handheld_propulsion_device_preventer_enabler", true,
				"config.fluxtech.enabled.handheld_propulsion_device_preventer_enabler");

		public static final ConfigItem<Boolean> ENABLED_GELS = new ConfigItem<>("gel_enabler", true,
				"config.fluxtech.enabled.gel_enabler");

		public static final ConfigItem<Boolean> ENABLED_GRAVITRONS = new ConfigItem<>("gravitrons_enabler", true,
				"config.fluxtech.enabled.gravitrons_enabler");

		public static final ConfigItem<Boolean> ENABLED_AEROARMOR = new ConfigItem<>("aeroarmor_enabler", true,
				"config.fluxtech.enabled.aeroarmor_enabler");

		public static final ConfigItem<Boolean> ENABLED_PORTAL_BLOCKS = new ConfigItem<>("portal_blocks_enabler", true,
				"config.fluxtech.enabled.portal_blocks_enabler");

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(ENABLED_AEROARMOR, ENABLED_GRAVITRONS, ENABLED_GELS, ENABLED_HPD,ENABLED_PORTAL_BLOCKS,ENABLED_HPD_LAUNCH_PREVENTER);

		public ENABLED() {
			super(OPTIONS, "enabled");
		}
	}

	public static class INTEGER_VALUES extends ConfigItemGroup {


		public static final ConfigItem<Integer> GRAVITRON_CRUSH_DAMAGE = new ConfigItem<>("gravitrion_crush_damage", 30,
				"config.fluxtech.integer_values.gravitrion_crush_damage");
		public static final ConfigItem<Integer> HPD_COOLDOWN = new ConfigItem<>("hpd_cooldown", 5,
				"config.fluxtech.integer_values.hpd_cooldown");
		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(GRAVITRON_CRUSH_DAMAGE, HPD_COOLDOWN);

		public INTEGER_VALUES() {
			super(OPTIONS, "integer_values");
		}

	}

	private static final String CONFIG_FILE_NAME = "fluxtech.json";

	private static final List<ConfigItemGroup> CONFIGS = ImmutableList.of(new ENABLED(), new VALUES(), new INTEGER_VALUES());


	public FluxTechConfig() {
		super(CONFIGS, new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME), "fluxtech");
	}





}
