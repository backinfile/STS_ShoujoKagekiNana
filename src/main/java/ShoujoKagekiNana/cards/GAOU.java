package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class GAOU extends BaseCard {
    public static final String ID = ModPath.makeID(GAOU.class.getSimpleName());

    public GAOU() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 2;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
        this.addToBot(new RemoveSpecificPowerAction(p, p, WeakPower.POWER_ID));
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, magicNumber, false)));
                this.addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, magicNumber, false)));
            }
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
