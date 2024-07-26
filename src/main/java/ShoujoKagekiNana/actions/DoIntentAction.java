package ShoujoKagekiNana.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DoIntentAction extends AbstractGameAction {

    private final AbstractCreature creature;

    public DoIntentAction(AbstractCreature creature) {
        this.creature = creature;
    }

    @Override
    public void update() {
        if (creature instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) creature;
            if (!m.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(new ShowMoveNameAction(m));
                AbstractDungeon.actionManager.addToBottom(new IntentFlashAction(m));
                AbstractDungeon.actionManager.addToBottom(new InstantAction(() -> {
                    m.takeTurn();
                    m.applyTurnPowers();
                    m.createIntent();
                }));
            }
        }
        isDone = true;
    }
}
