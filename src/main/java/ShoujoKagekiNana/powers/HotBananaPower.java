package ShoujoKagekiNana.powers;


import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.util.Util;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HotBananaPower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(HotBananaPower.class.getSimpleName());

    private boolean canTrigger = true;

    public HotBananaPower(int amount) {
        super(POWER_ID, PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, amount);
//        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        checkCanTrigger();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        checkCanTrigger();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkCanTrigger();
        updateDescription();
    }

    private void checkCanTrigger() {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(DisposableVariable::isDisposableCard).count() >= amount) {
            canTrigger = false;
            stopPulse();
        } else {
            canTrigger = true;
            startLongPulse();
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (canTrigger && DisposableVariable.isDisposableCard(card)) {
            flash();
            addToBot(new GainEnergyAction(1));
            addToBot(new DrawCardAction(1));
            checkCanTrigger();
        }
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}
