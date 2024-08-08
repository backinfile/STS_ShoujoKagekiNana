package ShoujoKagekiNana.cards;

import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.cards.starter.Strike;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ShoujoKagekiNana.ModPath.makeID;


public class Stay extends BaseCard {
    public static final String ID = makeID(Stay.class.getSimpleName());

    public Stay() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseBlock = 8;
//        DisposableVariable.setBaseValue(this, 9);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void onObtainThis() {
        super.onObtainThis();
        List<AbstractCard> strikes = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c instanceof Strike).sorted((card, t1) -> {
            int c1 = Boolean.compare(card.upgraded, t1.upgraded);
            if (c1 != 0) return c1;
            return card.hashCode() - t1.hashCode();
        }).collect(Collectors.toList());
        if (!strikes.isEmpty()) {
            AbstractCard card = strikes.get(0);
            CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            AbstractDungeon.player.masterDeck.removeCard(card);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
//            upgradeDamage(2);
            upgradeBlock(3);
            initializeDescription();
        }
    }
}