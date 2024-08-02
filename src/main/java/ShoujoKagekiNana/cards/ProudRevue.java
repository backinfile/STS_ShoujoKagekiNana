package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.actions.DrawAttackCardAction;
import ShoujoKagekiNana.actions.StartRevueAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

public class ProudRevue extends BaseCard {
    public static final String ID = makeID(ProudRevue.class.getSimpleName());

    public ProudRevue() {
        super(ID, 1, CardType.SKILL, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);
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