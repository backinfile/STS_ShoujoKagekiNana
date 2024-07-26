package ShoujoKagekiNana.stances;

import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.actions.DoIntentAction;
import ShoujoKagekiNana.effects.RevueParticleEffect;
import ShoujoKagekiNana.effects.StanceAuraRevueEffect;
import ShoujoKagekiNana.stances.patches.StancePatches;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;

public class RevueStance extends AbstractStance {

    public static final String STANCE_ID = "Revue";
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;

    public RevueStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }

    public void updateAnimation() {
        AbstractCreature owner = StancePatches.Field2.owner.get(this);
        if (owner == null) {
            Log.logger.error("RevueStance updateAnimation owner == null");
            return;
        }

        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.04F;
                AbstractDungeon.effectsQueue.add(new RevueParticleEffect(owner));
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new StanceAuraRevueEffect(owner));
        }

    }

    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
//        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
//        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
    }

    public void onExitStance() {
//        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(2));
//        this.stopIdleSfx();
        AbstractCreature owner = StancePatches.Field2.owner.get(RevueStance.this);
        if (owner != null) {
            AbstractDungeon.actionManager.addToBottom(new DoIntentAction(owner));
        }
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }
}
