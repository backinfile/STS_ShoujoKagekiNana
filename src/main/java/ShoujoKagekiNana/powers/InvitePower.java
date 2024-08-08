package ShoujoKagekiNana.powers;


import ShoujoKagekiCore.base.BasePower;
import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.modifiers.InstantModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InvitePower extends BasePower {
    public static final String POWER_ID = ModPath.makeID(InvitePower.class.getSimpleName());

    public InvitePower(int amount) {
        super(POWER_ID, PowerType.BUFF, AbstractDungeon.player, AbstractDungeon.player, amount);
//        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        addToBot(new DrawCardAction(amount, new InstantAction(() -> {
            for (AbstractCard card : DrawCardAction.drawnCards) {
                CardModifierManager.addModifier(card, new InstantModifier());
            }
        })));
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
