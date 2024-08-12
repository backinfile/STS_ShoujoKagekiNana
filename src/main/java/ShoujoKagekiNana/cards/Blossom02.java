package ShoujoKagekiNana.cards;

import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.blossom.BlossomField;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShoujoKagekiNana.ModPath.makeID;


public class Blossom02 extends BlossomCard {
    public static final String ID = makeID(Blossom02.class.getSimpleName());

    public Blossom02() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.baseBlock = 9;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void triggerBlossom() {
        super.triggerBlossom();
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
//            upgradeMagicNumber(1);
            upgradeBlock(3);
            initializeDescription();
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (BlossomField.isBlossomed(this)) {
            addToBot(new InstantAction(() -> {
                AbstractPlayer player = AbstractDungeon.player;
                if (player.hand.contains(this)) return;
                if (player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                    player.createHandIsFullDialog();
                    return;
                }
                if (player.drawPile.contains(this)) {
                    player.drawPile.removeCard(this);
                } else if (player.discardPile.contains(this)) {
                    player.discardPile.removeCard(this);
                } else {
                    return;
                }
                player.hand.addToTop(this);
                player.hand.refreshHandLayout();
                player.hand.applyPowers();
            }));
        }
    }
}