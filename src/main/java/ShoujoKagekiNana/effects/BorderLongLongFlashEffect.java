package ShoujoKagekiNana.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BorderLongLongFlashEffect extends AbstractGameEffect {
    private final float totalDuration;
    private final float startAlpha;
    private final boolean additive;
    private final Color borderColor; // do not use super.color

    public BorderLongLongFlashEffect(Color borderColor, float totalDuration, boolean additive) {
        this.totalDuration = totalDuration;
        this.duration = totalDuration;
        this.startAlpha = borderColor.a;
        this.borderColor = borderColor.cpy();
        this.borderColor.a = 0F;
        this.additive = additive;
    }

    public void update() {
        if (totalDuration - this.duration < 0.2F) {
            this.borderColor.a = Interpolation.fade.apply(0.0F, this.startAlpha, (totalDuration - this.duration) * 10.0F);
        } else {
            this.borderColor.a = Interpolation.pow2Out.apply(0.0F, this.startAlpha, this.duration / totalDuration);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (this.additive) {
            sb.setBlendFunction(770, 1);
        }

        sb.setColor(this.borderColor);
        sb.draw(ImageMaster.BORDER_GLOW_2, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (this.additive) {
            sb.setBlendFunction(770, 771);
        }

    }

    public void dispose() {
    }
}
