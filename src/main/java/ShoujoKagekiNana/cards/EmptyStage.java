package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class EmptyStage extends BaseCard {
    public static final String ID = makeID(EmptyStage.class.getSimpleName());

    public EmptyStage() {
        super(ID, -2, CardType.CURSE, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.color = CardColor.CURSE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
    }
}