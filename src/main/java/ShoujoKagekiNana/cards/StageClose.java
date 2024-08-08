package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.actions.StageDiscoveryRemoveAction;
import ShoujoKagekiNana.actions.StageRemoveAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class StageClose extends BaseCard {
    public static final String ID = ModPath.makeID(StageClose.class.getSimpleName());

    public StageClose() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseBlock = block = 9;
        exhaust = true;
        DisposableVariable.setBaseValue(this, 6);
        this.tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        this.addToBot(new StageDiscoveryRemoveAction());
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
            initializeDescription();
        }
    }
}
