package ShoujoKagekiNana.actions;

import ShoujoKagekiCore.util.ActionUtils;
import ShoujoKagekiNana.blossom.BlossomFieldPatch;
import ShoujoKagekiNana.util.Util;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class BlossomAction extends AbstractGameAction {
    private final AbstractCard card;

    private static final float DURATION_WAIT = 0.3f;
    private static final float DURATION_SHOW = 2.5f;
    private static final float DIRTATION_TOTAL = DURATION_WAIT + DURATION_SHOW;

    private int stage = 0;

    public BlossomAction(AbstractCard card) {
        this.card = card;
        this.startDuration = this.duration = DIRTATION_TOTAL;
    }

    @Override
    public void update() {
        if (startDuration == duration) {
            if (!AbstractDungeon.player.hand.contains(card) || !BlossomFieldPatch.canTriggerBlossom(card)) {
                isDone = true;
                return;
            }
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(card.current_x, card.current_y));
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (startDuration - duration < DURATION_WAIT) {
            return;
        }

        // wait over

        BlossomFieldPatch.triggerBlossom(card);

        isDone = true;
        addToTop(new WaitAction(0.8F - DURATION_WAIT));
        addToBot(new HandCheckAction());
    }
}
