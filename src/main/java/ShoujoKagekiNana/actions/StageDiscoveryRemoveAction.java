package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class StageDiscoveryRemoveAction extends AbstractGameAction {
    private boolean retrieveCard = false;

    public StageDiscoveryRemoveAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> generatedCards = StagePoolManager.popCardForRemove(3);
            if (generatedCards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, StageDiscoveryAction.uiString.TEXT[1], true);
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
//                    disCard.current_x = -1000.0F * Settings.xScale;

//                    StagePoolManager.removeCard(disCard);
//                    AbstractDungeon.effectList.add(new PurgeCardInBattleEffect(disCard)); // , disCard.current_x, disCard.current_y

                    addToTop(new StageRemoveAction(disCard));
                    isDone = true;
                }

                this.retrieveCard = true;
            }

            this.tickDuration();
        }
    }

}