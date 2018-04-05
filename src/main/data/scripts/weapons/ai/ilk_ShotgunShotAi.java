package data.scripts.weapons.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

// A dummy AI that immediately replaces the projectile.
public class ilk_ShotgunShotAi implements MissileAIPlugin {

  private static final int SPAWNED_SHOT_COUNT = 18;
  private static final float MAX_VELOCITY_DEVIATION = 120.0f;

  public ilk_ShotgunShotAi(MissileAPI missile, ShipAPI launchingShip) {
    CombatEngineAPI engine = Global.getCombatEngine();
    final Vector2f baseVelocity = missile.getVelocity();
    final String spawnProjectileId = missile.getProjectileSpecId() + "_clone";

    for (int i = 0; i < SPAWNED_SHOT_COUNT; i++) {
      Vector2f randomVel =
          MathUtils.getRandomPointOnCircumference(
              null, MathUtils.getRandomNumberInRange(0f, MAX_VELOCITY_DEVIATION));
      randomVel.x += baseVelocity.x;
      randomVel.y += baseVelocity.y;
      engine.spawnProjectile(
          launchingShip,
          missile.getWeapon(),
          spawnProjectileId,
          missile.getLocation(),
          missile.getFacing(),
          randomVel);
    }

    engine.removeEntity(missile);
  }

  @Override
  public void advance(float amount) {}
}
