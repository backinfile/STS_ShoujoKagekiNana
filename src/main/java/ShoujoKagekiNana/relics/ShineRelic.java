package ShoujoKagekiNana.relics;

import ShoujoKagekiCore.base.BaseRelic;
import ShoujoKagekiCore.shine.DisposableVariable;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.actions.InstantAction;
import ShoujoKagekiNana.actions.StageDiscoveryAction;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ShineRelic extends BaseRelic {
    public static final String ID = ModPath.makeID(ShineRelic.class.getSimpleName());

    public ShineRelic() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw() {
//        addToBot(new WaitAction(Settings.ACTION_DUR_FAST));
//        addToBot(new StageDiscoveryAction());
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new InstantAction(() -> {
            ArrayList<AbstractCard> cards = StagePoolManager.popCard(1);
            if (!cards.isEmpty()) {
                addToTop(new MakeTempCardInHandAction(cards.get(0)));
            }
        }));
    }

    //    private boolean canTrigger = false;
//    @Override
//    public void atBattleStart() {
//        super.atBattleStart();
//        beginLongPulse();
//        canTrigger = true;
//    }
//
//    @Override
//    public void onVictory() {
//        super.onVictory();
//        stopPulse();
//        canTrigger = false;
//    }

//    @Override
//    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
//        super.onUseCard(targetCard, useCardAction);
//
//        if (DisposableVariable.isDisposableCard(targetCard) && canTrigger) {
//            canTrigger = false;
//            stopPulse();
//            addToBot(new GainEnergyAction(1));
//            addToBot(new DrawCardAction(1));
//        }
//    }
}
