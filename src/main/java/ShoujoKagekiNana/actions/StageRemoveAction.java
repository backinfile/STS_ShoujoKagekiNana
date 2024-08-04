package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class StageRemoveAction extends AbstractGameAction {
    private final AbstractCard card;

    public StageRemoveAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        DoInstant(card);
        addToBot(new WaitAction(PurgeCardInBattleEffect.DURATION));
        isDone = true;
    }

    public static void DoInstant(AbstractCard card) {
        StagePoolManager.removeCard(card);
        AbstractDungeon.effectList.add(new PurgeCardInBattleEffect(card));
    }
}
