package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.auditionEnergy.AuditionEnergy;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class GainAuditionEnergyAction extends AbstractGameAction {
    public GainAuditionEnergyAction(int total) {
        this.actionType = ActionType.SPECIAL;
        this.amount = total;
    }

    public void update() {
        AuditionEnergy.add(amount);
        this.isDone = true;
    }
}