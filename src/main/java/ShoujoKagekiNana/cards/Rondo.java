package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import ShoujoKagekiNana.powers.RondoPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Rondo extends BaseCard {
    public static final String ID = ModPath.makeID(Rondo.class.getSimpleName());

    public Rondo() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = 1;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new RondoPower(m, magicNumber)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBaseCost(0);
        }
    }
}
