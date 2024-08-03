package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class StageRemoveAction extends AbstractGameAction {
    private boolean retrieveCard = false;

    public StageRemoveAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> generatedCards = StagePoolManager.popCard(3);
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], true);
            this.tickDuration();
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        disCard.upgrade();
                    }
//                    disCard.setCostForTurn(0);

                    StagePoolManager.removeCard(disCard);
                    // TODO dispose effect

//                    disCard.current_x = -1000.0F * Settings.xScale;

                    AbstractDungeon.effectList.add(new PurgeCardInBattleEffect(disCard)); // , disCard.current_x, disCard.current_y
                    isDone = true;
                }

                this.retrieveCard = true;
            }

            this.tickDuration();
        }
    }

}