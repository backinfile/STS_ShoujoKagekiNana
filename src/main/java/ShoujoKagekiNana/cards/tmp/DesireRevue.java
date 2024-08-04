package ShoujoKagekiNana.cards.tmp;

import ShoujoKagekiNana.actions.StartRevueAction;
import ShoujoKagekiNana.cards.BaseCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static ShoujoKagekiNana.ModPath.makeID;

public class DesireRevue extends BaseCard {
    public static final String ID = makeID(DesireRevue.class.getSimpleName());

    public DesireRevue() {
        super(ID, 1, CardType.SKILL, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        addToBot(new StartRevueAction(m));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}