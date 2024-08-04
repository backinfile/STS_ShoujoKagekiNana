package ShoujoKagekiNana.actions;

import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class StageCardPowerUpAction extends AbstractGameAction {
    private final Consumer<AbstractCard> consumer;

    public StageCardPowerUpAction(Consumer<AbstractCard> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void update() {
        for (AbstractCard card : StagePoolManager.cardPool) {
            consumer.accept(card);
        }
        isDone = true;
    }
}
