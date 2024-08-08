package ShoujoKagekiNana.effects;

import ShoujoKagekiNana.stagePool.TopPanelDisposedPileBtn;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;

public class UpgradeStageShineEffect extends AbstractGameEffect {
    private final float x;
    private final float y;
    private boolean clang1 = false;
    private boolean clang2 = false;

    private final float scale = 0.5f;

    public UpgradeStageShineEffect() {
        this.x = TopPanelDisposedPileBtn.DISPOSED_PILE_BTN_X;
        this.y = TopPanelDisposedPileBtn.DISPOSED_PILE_BTN_Y;
        this.duration = 0.8F;
    }

    public void update() {
        if (this.duration < 0.6F && !this.clang1) {
            CardCrawlGame.sound.play("CARD_UPGRADE");
            this.clang1 = true;
            this.clank(this.x - 80.0F * Settings.scale * scale, this.y + 0.0F * Settings.scale * scale);
//            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

        if (this.duration < 0.2F && !this.clang2) {
            this.clang2 = true;
            this.clank(this.x + 90.0F * Settings.scale * scale, this.y - 110.0F * Settings.scale * scale);
//            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.clank(this.x + 30.0F * Settings.scale * scale, this.y + 120.0F * Settings.scale * scale);
            this.isDone = true;
//            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

    }

    private void clank(float x, float y) {
        AbstractDungeon.topLevelEffectsQueue.add(new UpgradeHammerImprintEffect(x, y));
        if (!Settings.DISABLE_EFFECTS) {
            for (int i = 0; i < 30; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(
                        new UpgradeShineParticleEffect(
                                x + MathUtils.random(-10.0F, 10.0F) * Settings.scale * scale,
                                y + MathUtils.random(-10.0F, 10.0F) * Settings.scale * scale
                        )
                );
            }

        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
