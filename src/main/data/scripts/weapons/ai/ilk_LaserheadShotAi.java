package data.scripts.weapons.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.plugins.beamRenderer.ilk_BeamRendererPlugin;
import data.scripts.plugins.beamRenderer.ilk_BeamSpec;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

// A dummy AI that immediately replaces the projectile.
public class ilk_LaserheadShotAi implements MissileAIPlugin {

  private static final float BEAM_RANGE = 700.0f;
  private static final float BEAM_DURATION = 0.5f;
  private static final float BEAM_CHARGEUP = 0.1f;
  private static final float BEAM_CHARGEDOWN = 0.2f;
  private static final String SPRITE_KEY = "beams";
  private static final String SPRITE_NAME = "ilk_fakeBeamFX";
  private static final float BEAM_WIDTH = 27.0f;
  private static final Color BEAM_COLOR = new Color(224, 184, 225, 175);
  private static final String SOUND_ID = "ilk_graser_fire";

  public ilk_LaserheadShotAi(MissileAPI missile, ShipAPI launchingShip) {
    Vector2f fireLoc = missile.getLocation();
    CombatEngineAPI engine = Global.getCombatEngine();
    ilk_BeamRendererPlugin.addBeam(
        new ilk_BeamSpec(
            engine,
            launchingShip,
            missile.getLocation(),
            BEAM_RANGE,
            missile.getFacing(),
            missile.getDamageAmount(),
            missile.getDamageType(),
            missile.getEmpAmount(),
            BEAM_DURATION,
            BEAM_CHARGEUP,
            BEAM_CHARGEDOWN,
            SPRITE_KEY,
            SPRITE_NAME,
            BEAM_WIDTH,
            BEAM_COLOR));
    Global.getSoundPlayer().playSound(SOUND_ID, 1f, 1f, fireLoc, missile.getVelocity());
    engine.removeEntity(missile);
  }

  @Override
  public void advance(float amount) {}
}
