package ShoujoKagekiNana.cards.tmp;

import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.modifiers.InstantModifier;
import basemod.AutoAdd;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

@AutoAdd.Ignore
public class Mabuxi extends BaseCard {
    public static final String ID = makeID(Mabuxi.class.getSimpleName());

    public Mabuxi() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
//        baseDamage = 5;
//        baseBlock = 5;
        this.magicNumber = this.baseMagicNumber = 2;
//        DisposableVariable.setBaseValue(this, 9);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//        addToBot(new GainBlockAction(p, block));
        addToBot(new GainEnergyAction(magicNumber));
        addToBot(new InstantAction(() -> {
            for (AbstractCard card : p.hand.group) {
                CardModifierManager.addModifier(card, new InstantModifier());
            }
        }));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}