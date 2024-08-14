package ShoujoKagekiNana.patches;

import ShoujoKagekiNana.AudioManager;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.cards.BaseCard;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.google.gson.annotations.SerializedName;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


public class AudioPatch {

    @SpirePatch(
            clz = CardStrings.class,
            method = "<class>"
    )
    public static class CardStringsFlavorField {
        @SerializedName("ShoujoKageki_FLAVOR")
        public static SpireField<String> flavor = new SpireField<>(() -> null);
    }

    @SpirePatch2(
            clz = AbstractCard.class,
            method = "<class>"
    )
    public static class AbstractCardFlavorFields {
        public static SpireField<String> flavor = new SpireField<>(() -> null);
    }

    @SpirePatch2(
            clz = AbstractCard.class,
            method = "<ctor>",
            paramtypez = {String.class, String.class, String.class, int.class, String.class, AbstractCard.CardType.class, AbstractCard.CardColor.class, AbstractCard.CardRarity.class, AbstractCard.CardTarget.class, DamageInfo.DamageType.class}
    )
    public static class FlavorIntoCardStrings {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            CardStrings cardStrings = (CardStrings) ((Map<?, ?>) ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "cards")).get(__instance.cardID);

            if (cardStrings != null) {
                String flavorText = CardStringsFlavorField.flavor.get(cardStrings);
                if (flavorText != null) {
                    AbstractCardFlavorFields.flavor.set(__instance, flavorText);
                }
            }
        }
    }


    private static UIStrings uiStrings = null;

    @SpirePatch2(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderPatch {
        public static void Postfix(SingleCardViewPopup __instance, AbstractCard ___card, SpriteBatch sb) {
            if (!(___card instanceof BaseCard)) return;

            if (uiStrings == null)
                uiStrings = CardCrawlGame.languagePack.getUIString(ModPath.makeID(AudioPatch.class.getSimpleName()));

            String flavorText = AbstractCardFlavorFields.flavor.get(___card);
            if (StringUtils.isEmpty(flavorText)) return;
            String singleCardAudioKey = ((BaseCard) ___card).singleCardAudioKey;

            String description;
            if (StringUtils.isEmpty(singleCardAudioKey)) {
                description = flavorText;
            } else {
                description = flavorText + uiStrings.TEXT[0];
            }

            renderTipBox(Settings.WIDTH / 2.0F - 340.0F * Settings.scale - 320.0F * Settings.scale, 420.0F * Settings.scale, sb, description);

//            ReflectionHacks.RStaticMethod renderPowerTips = ReflectionHacks.privateStaticMethod(TipHelper.class, "renderPowerTips", float.class, float.class, SpriteBatch.class, ArrayList.class);
//            renderPowerTips.invoke(null, new Object[]{(});
        }
    }

    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float TEXT_OFFSET_X = 22.0F * Settings.scale;
    private static final float BODY_TEXT_WIDTH = 280.0F * Settings.scale;
    private static final float BODY_OFFSET_Y = 0; // = -20.0F * Settings.scale;
    private static final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;
    private static final float HEADER_OFFSET_Y = 12.0F * Settings.scale;
    private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);

    private static void renderTipBox(float x, float y, SpriteBatch sb, String description) {
        float h = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 40.0F * Settings.scale;
        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);
//        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, title, x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, description, x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);
    }

    @SpirePatch2(
            clz = SingleCardViewPopup.class,
            method = "updateInput"
    )
    public static class UpdateInputPatch {
        @SpirePostfixPatch
        public static void __updateInput(SingleCardViewPopup __instance, AbstractCard ___card) {
            if (!(___card instanceof BaseCard)) return;
            String singleCardAudioKey = ((BaseCard) ___card).singleCardAudioKey;
            if (singleCardAudioKey == null) return;
            if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                AudioManager.stop(singleCardAudioKey);
                AudioManager.play(singleCardAudioKey);
            }
        }
    }
}
