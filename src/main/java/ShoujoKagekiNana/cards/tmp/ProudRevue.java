package ShoujoKagekiNana.cards.tmp;

import ShoujoKagekiNana.actions.DrawAttackCardAction;
import ShoujoKagekiNana.actions.StartRevueAction;
import ShoujoKagekiNana.cards.BaseCard;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

@AutoAdd.Ignore
public class ProudRevue extends BaseCard {
    public static final String ID = makeID(ProudRevue.class.getSimpleName());

    public ProudRevue() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StartRevueAction(m));
        addToBot(new DrawAttackCardAction(magicNumber, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}