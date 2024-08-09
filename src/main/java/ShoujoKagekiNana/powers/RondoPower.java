package ShoujoKagekiNana.powers;


import ShoujoKagekiCore.base.BasePower;
import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RondoPower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(RondoPower.class.getSimpleName());

    private boolean canTrigger = true;

    public RondoPower(AbstractMonster owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, owner, AbstractDungeon.player, amount);
//        updateDescription();
    }

    public void atEndOfRound() {
        this.addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        addToBot(new DrawCardAction(amount));
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
