package ilk.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin.CustomRepImpact;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin.RepActionEnvelope;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin.RepActions;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.AICores;
import com.fs.starfarer.api.util.Misc.Token;
import java.util.List;
import java.util.Map;

public class ilk_TurnInAiCores extends BaseCommandPlugin {

  private final float PERSONAL_REP_MULT = 0.25f;

  private TextPanelAPI text;
  private PersonAPI person;

  @Override
  public boolean execute(
      String ruleId,
      InteractionDialogAPI dialog,
      List<Token> params,
      Map<String, MemoryAPI> memoryMap) {
    String command = params.get(0).getString(memoryMap);
    if (command == null) return false;

    text = dialog.getTextPanel();
    person = dialog.getInteractionTarget().getActivePerson();

    switch (command) {
      case "personCanAcceptCores":
        return personCanAcceptCores();
      default:
        return true;
    }
  }

  protected void selectCores() {
    final CargoAPI playerCargo = Global.getSector().getPlayerFleet().getCargo();
    float bounty = 0.0f;
    float reputation = 0.0f;
    for (CargoStackAPI stack : playerCargo.getStacksCopy()) {
      CommoditySpecAPI spec = stack.getResourceIfResource();
      if (spec != null && spec.getDemandClass().equals(Commodities.AI_CORES)) {
        reputation += AICores.getBaseRepValue(spec.getId()) * stack.getSize();
        bounty += spec.getBasePrice() * stack.getSize();

        AddRemoveCommodity.addCommodityLossText(
            stack.getCommodityId(), (int) stack.getSize(), text);
        playerCargo.removeStack(stack);
      }
    }

    final FactionAPI faction = person.getFaction();
    bounty *= faction.getCustomFloat("AICoreValueMult");
    reputation *= faction.getCustomFloat("AICoreRepMult");

    playerCargo.getCredits().add(bounty);
    AddRemoveCommodity.addCreditsGainText((int) bounty, text);

    CustomRepImpact impact = new CustomRepImpact();
    impact.delta = reputation * 0.01f;
    Global.getSector()
        .adjustPlayerReputation(
            new RepActionEnvelope(RepActions.CUSTOM, impact, null, text, true), faction.getId());

    impact.delta *= 0.25f;
    Global.getSector()
        .adjustPlayerReputation(
            new RepActionEnvelope(RepActions.CUSTOM, impact, null, text, true), person);
  }

  protected boolean personCanAcceptCores() {
    return Ranks.POST_BASE_COMMANDER.equals(person.getPostId())
        || Ranks.POST_STATION_COMMANDER.equals(person.getPostId())
        || Ranks.POST_ADMINISTRATOR.equals(person.getPostId())
        || Ranks.POST_OUTPOST_COMMANDER.equals(person.getPostId());
  }
}
