package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jeff on 25/06/15.
 */
public class ilk_KerajjGen implements SectorGeneratorPlugin {

    @Override
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Kerajj");
        system.getLocation().set(-11500, -7300);

        system.setBackgroundTextureFilename("graphics/ilk/backgrounds/ilk_background2.jpg");

        //initialize the star
        PlanetAPI star = system.initStar("Kerajj", "star_white", 350f);
        star.setCustomDescriptionId("planet_Kerajj");
        system.setLightColor(new Color(240, 238, 191)); // light color in entire system, affects all entities

        PlanetAPI ker1 = system.addPlanet("Opel", star, "Opel", "desert", 40f, 100f, 3300f, 100f);
        ker1.setCustomDescriptionId("planet_Opel");
        ker1.setInteractionImage("illustrations", "marine");
        ilk_RashtGen.addMarketplace(Factions.INDEPENDENT,
                ker1,
                null,
                "Opel",
                2,
                new ArrayList<>(Arrays.asList(Conditions.FRONTIER, Conditions.FREE_PORT, Conditions.LARGE_REFUGEE_POPULATION,
                        Conditions.COTTAGE_INDUSTRY, Conditions.OUTPOST, Conditions.DESERT, Conditions.POPULATION_4)),
                new ArrayList<>(Arrays.asList(Submarkets.GENERIC_MILITARY, Submarkets.SUBMARKET_BLACK, Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_STORAGE)),
                0.3f);

        PlanetAPI ker2 = system.addPlanet("Ashaak", star, "Ashaak", "radiated", 160f, 140f, 1500f, 150f);
        ker2.setCustomDescriptionId("planet_Ashaak");

        PlanetAPI ker3 = system.addPlanet("Voronoi", star, "Voronoi", "gas_giant", 247f, 225f, 3900f, 170f);
        ker3.setCustomDescriptionId("planet_Voronoi");
        SectorEntityToken ilk_station = system.addOrbitalStation("ilk_listeningPost", ker3, 45f, 200f, 50f, "Mahyar Listening Post", "mayorate");

        PlanetAPI ker4 = system.addPlanet("Orinocco", star, "Orinocco", "lava_minor", 200f, 100f, 5700f, 320f);
        ker4.setCustomDescriptionId("planet_Orinocco");

        system.autogenerateHyperspaceJumpPoints(true, true);

    }
}
