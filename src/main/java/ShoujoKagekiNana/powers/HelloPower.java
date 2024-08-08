package ShoujoKagekiNana.powers;


import ShoujoKagekiCore.base.BasePower;
import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.ExhaustHandCardAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HelloPower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(HelloPower.class.getSimpleName());

    public HelloPower(int amount) {
        super(POWER_ID, PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, amount);
//        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        addToBot(new ExhaustHandCardAction(amount, c -> {
            c.applyPowers();
            if (c.damage > 0) {
                addToTop(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, c.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }));
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
