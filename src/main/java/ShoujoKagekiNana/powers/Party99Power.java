package ShoujoKagekiNana.powers;


import ShoujoKagekiNana.ModPath;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Party99Power extends BasePower {
    public static final String POWER_ID = ModPath.makeID(Party99Power.class.getSimpleName());

    public Party99Power(int amount) {
        super(POWER_ID, PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, amount);
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnBlossom(AbstractCard card) {
        flash();
        addToBot(new GainBlockAction(AbstractDungeon.player, amount));
    }
}
