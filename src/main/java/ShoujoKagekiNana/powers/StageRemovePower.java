package ShoujoKagekiNana.powers;


import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageRemoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StageRemovePower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(StageRemovePower.class.getSimpleName());

    private static int Index = 0;
    private final AbstractCard card;

    public StageRemovePower(AbstractCard card) {
        super(POWER_ID, AbstractPower.PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, -1);
        this.ID = POWER_ID + Index++;
        this.card = card;
        updateDescription();
    }

    @Override
    public void onVictory() {
        super.onVictory();

        if (card != null) {
            StageRemoveAction.DoInstant(card);
        }
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        if (card != null) {
            this.description = DESCRIPTIONS[0] + card.name + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        }
    }
}
