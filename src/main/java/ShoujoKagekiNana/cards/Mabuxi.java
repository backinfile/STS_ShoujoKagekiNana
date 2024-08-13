package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.blossom.BlossomField;
import ShoujoKagekiNana.modifiers.InstantModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

public class Mabuxi extends BaseCard {
    public static final String ID = makeID(Mabuxi.class.getSimpleName());

    public Mabuxi() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 3;
        this.defaultBaseSecondMagicNumber = this.defaultSecondMagicNumber = 3;
        BlossomField.set(this, card -> {
            card.exhaust = true;
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (BlossomField.isBlossomed(this)) {
            addToBot(new GainEnergyAction(magicNumber));
        } else {
            addToBot(new DrawCardAction(3));
        }
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