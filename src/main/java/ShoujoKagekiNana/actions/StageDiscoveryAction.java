package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.Res;
import ShoujoKagekiNana.effects.BorderLongLongFlashEffect;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.function.Consumer;

public class StageDiscoveryAction extends AbstractGameAction {
    public static final UIStrings uiString = CardCrawlGame.languagePack.getUIString(ModPath.makeID(StageDiscoveryAction.class.getSimpleName()));
    private final AbstractCard.CardType cardType;
    private boolean retrieveCard = false;
    private final Consumer<AbstractCard> consumer;

    public StageDiscoveryAction() {
        this(1, null, null);
    }

    public StageDiscoveryAction(Consumer<AbstractCard> consumer) {
        this(1, null, consumer);
    }

    public StageDiscoveryAction(int amount, AbstractCard.CardType cardType, Consumer<AbstractCard> consumer) {
        this.amount = amount;
        this.cardType = cardType;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.consumer = consumer;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.topLevelEffectsQueue.add(new BorderLongLongFlashEffect(Res.NanaRenderColor.cpy(), 2, false));
            ArrayList<AbstractCard> generatedCards = StagePoolManager.popCard(3, cardType);
            if (generatedCards.isEmpty()) {
                isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, uiString.TEXT[0], true);
            this.tickDuration();
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard copy = AbstractDungeon.cardRewardScreen.discoveryCard;
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                    int handSize = AbstractDungeon.player.hand.size();
                    for (int i = 0; i < amount; i++) {
                        AbstractCard card = copy.makeStatEquivalentCopy();
                        if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                            card.upgrade();
                        }
                        if (consumer != null) consumer.accept(card);
                        card.current_x = -1000.0F * Settings.xScale;
                        if (handSize + i + 1 <= BaseMod.MAX_HAND_SIZE) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                        }
                    }
                }

                this.retrieveCard = true;
            }

            this.tickDuration();
        }
    }

}