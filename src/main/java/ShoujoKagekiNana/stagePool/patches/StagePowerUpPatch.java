package ShoujoKagekiNana.stagePool.patches;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.modifiers.AddBlockModifier;
import ShoujoKagekiNana.modifiers.AddDamageModifier;
import ShoujoKagekiNana.stances.patches.StancePatches;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

import java.util.ArrayList;

public class StagePowerUpPatch {

    private static final float offsetX_ori = 0;
    private static final float offsetY_ori = -90; //-68;
    private static final float textOffsetX = -132.0F;
    private static final float textOffsetY = 192.0F;

    private static final Texture STAGE_ICON;

    static {
        int width = 512;
        int height = 512;
        Pixmap texture = new Pixmap(Gdx.files.internal(ModPath.makeUIPath("stage_icon.png")));
        Pixmap icon = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        float scale = 1.2f;
        int offsetX = (int) (width / 2f + textOffsetX + offsetX_ori - texture.getWidth() / 2 * scale);
        int offsetY = (int) (height / 2f - textOffsetY - offsetY_ori - texture.getHeight() / 2 * scale);

        icon.drawPixmap(texture, 0, 0, texture.getWidth(), texture.getHeight(),
                offsetX, offsetY, (int) (texture.getWidth() * scale), (int) (texture.getHeight() * scale)
        );

        Texture stage_icon = new Texture(icon);
        stage_icon.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        STAGE_ICON = stage_icon;
    }


    // 初始化 初始的舞台
    @SpirePatch2(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    public static class RenderStagePosition {
        public static void Postfix(AbstractCard __instance, SpriteBatch sb, float ___drawScale, float ___angle, float ___transparency) {
            ArrayList<AbstractCardModifier> modifiers = CardModifierManager.modifiers(__instance);
            long addDamage = modifiers.stream().filter(m -> m instanceof AddDamageModifier).count();
            long addBlock = modifiers.stream().filter(m -> m instanceof AddBlockModifier).count();

            if (addDamage == 0 && addBlock == 0) {
                return;
            }
            String text = addDamage + "/" + addBlock;

            float img_scale = 1.2f;
            float text_scale = 0.8f;

            float offsetX = offsetX_ori * Settings.scale * ___drawScale;
            float offsetY = offsetY_ori * Settings.scale * ___drawScale;


            float drawX = __instance.current_x;
            float drawY = __instance.current_y;
            Texture img = STAGE_ICON;
            sb.setColor(Color.WHITE);
            sb.draw(img,
                    drawX - img.getWidth() / 2f,
                    drawY - img.getHeight() / 2f,
                    img.getWidth() / 2f, img.getHeight() / 2f,
                    img.getWidth(), img.getHeight(),
                    ___drawScale * Settings.scale,
                    ___drawScale * Settings.scale,
                    ___angle,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    false, false);

            Color costColor = Color.WHITE.cpy();
            costColor.a = ___transparency;
            FontHelper.cardEnergyFont_L.getData().setScale(___drawScale * text_scale);
            BitmapFont font = FontHelper.cardEnergyFont_L;
            FontHelper.renderRotatedText(sb, font, text, drawX, drawY, textOffsetX * ___drawScale * Settings.scale + offsetX, textOffsetY * ___drawScale * Settings.scale + offsetY, ___angle, false, costColor);
        }
    }
}
