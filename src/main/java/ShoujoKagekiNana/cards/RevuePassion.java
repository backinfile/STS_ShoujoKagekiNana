package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.actions.CreatureSwitchStanceAction;
import ShoujoKagekiNana.actions.DoIntentAction;
import ShoujoKagekiNana.stances.RevueStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;

public class RevuePassion extends BaseCard {
    public static final String ID = makeID(RevuePassion.class.getSimpleName());

    public RevuePassion() {
        super(ID, 1, AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);
        baseDamage = 15;

        this.tags.add(AbstractCard.CardTags.STRIKE);
        this.tags.add(AbstractCard.CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//        addToBot(new CreatureSwitchStanceAction(m, RevueStance.STANCE_ID));
        addToBot(new DoIntentAction(m));

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(5);
            initializeDescription();
        }
    }
}