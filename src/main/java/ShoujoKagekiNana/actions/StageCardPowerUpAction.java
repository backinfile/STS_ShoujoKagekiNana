package ShoujoKagekiNana.actions;

import ShoujoKagekiCore.util.Utils2;
import ShoujoKagekiNana.cards.Defend03;
import ShoujoKagekiNana.effects.UpgradeStageShineEffect;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class StageCardPowerUpAction extends AbstractGameAction {
    private final Consumer<AbstractCard> consumer;

    public StageCardPowerUpAction(Consumer<AbstractCard> consumer) {
        this.consumer = consumer;
    }

    public void doInstance() {
        update();
    }

    @Override
    public void update() {
        for (AbstractCard card : StagePoolManager.cardPool) {
            consumer.accept(card);
        }

        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof Defend03) {
                consumer.accept(card);
            }
        }
        for (AbstractCard card : Utils2.getAllCardsInCombat(AbstractDungeon.player)) {
            if (card instanceof Defend03) {
                consumer.accept(card);
            }
        }
        AbstractDungeon.effectsQueue.add(new UpgradeStageShineEffect());
        isDone = true;
    }
}
