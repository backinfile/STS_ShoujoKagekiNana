package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Prepare2 extends BaseCard {
    public static final String ID = ModPath.makeID(Prepare2.class.getSimpleName());

    public Prepare2() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.baseBlock = this.block = 6;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
        DisposableVariable.setBaseValue(this, 3);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new StageCardPowerUpAction(c -> {
            CardModifierManager.addModifier(c, new AddDamageModifier());
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBlock(3);
            initializeDescription();
        }
    }
}
