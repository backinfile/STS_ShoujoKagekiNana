package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiCore.token.TokenCardField;
import ShoujoKagekiNana.actions.InstantAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Attack04 extends BaseCard {
    public static final String ID = makeID(Attack04.class.getSimpleName());

    public Attack04() {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 27;
        DisposableVariable.setBaseValue(this, 9);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (TokenCardField.isToken.get(this)) {
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (TokenCardField.isToken.get(this)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(9);
            initializeDescription();
        }
    }
}