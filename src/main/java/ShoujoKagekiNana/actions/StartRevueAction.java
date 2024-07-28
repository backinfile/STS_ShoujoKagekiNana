package ShoujoKagekiNana.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StartRevueAction extends AbstractGameAction {
    private final AbstractMonster monster;

    public StartRevueAction(AbstractMonster monster) {
        this.startDuration = this.duration = Settings.ACTION_DUR_FAST;
        this.monster = monster;
    }

    @Override
    public void update() {
        if (startDuration == duration) {
            addToTop(new DoIntentAction(monster));
            addToTop(new GainAuditionEnergyAction(2));
            isDone = true;
        }
    }
}
