package ShoujoKagekiNana.cards.tmp;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.cards.BaseCard;
import ShoujoKagekiNana.modifiers.InstantBlock;
import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Ignore
public class Defend02 extends BaseCard {
    public static final String ID = ModPath.makeID(Defend02.class.getSimpleName());
//    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public Defend02() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        this.baseBlock = this.block = 10;
        BlockModifierManager.addModifier(this, new InstantBlock());
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainCustomBlockAction(this, p, this.block));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
        }
    }
}