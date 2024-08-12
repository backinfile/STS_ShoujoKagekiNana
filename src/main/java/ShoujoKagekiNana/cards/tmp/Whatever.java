package ShoujoKagekiNana.cards.tmp;

import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.modifiers.InstantModifier;
import basemod.AutoAdd;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

@AutoAdd.Ignore
public class Whatever extends BaseCard {
    public static final String ID = makeID(Whatever.class.getSimpleName());

    public Whatever() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        isMultiDamage = true;
        this.baseDamage = this.damage = 12;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
            upgradeDamage(4);
            initializeDescription();
        }
    }
}