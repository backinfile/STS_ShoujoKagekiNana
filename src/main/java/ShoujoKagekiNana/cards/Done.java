package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageCardPowerUpAction;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.modifiers.AddBlockModifier;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Done extends BaseCard {
    public static final String ID = ModPath.makeID(Done.class.getSimpleName());

    public Done() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.baseBlock = this.block = 12;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
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
            upgradeBlock(4);
            initializeDescription();
        }
    }
}
