package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.List;
import java.util.Map;

public class IsFleetAlive extends BaseCommandPlugin {

    //param1 fleet mem key
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String memKey = params.get(0).getString(memoryMap);
        String fleetID = (String) Global.getSector().getMemory().get(memKey);
        
        return Global.getSector().getEntityById(fleetID).isAlive();
    }
    
}
