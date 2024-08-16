package ShoujoKagekiNana.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BasePower extends ShoujoKagekiCore.base.BasePower {
    public BasePower(String ID, PowerType powerType, AbstractCreature owner, AbstractCreature source, int amount) {
        super(ID, powerType, owner, source, amount);
    }

    public void triggerOnBlossom(AbstractCard card) {
        
    }
}
