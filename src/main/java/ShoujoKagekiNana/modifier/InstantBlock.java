package ShoujoKagekiNana.modifier;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.TextureLoader;
import ShoujoKagekiNana.actions.InstantAction;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;

public class InstantBlock extends AbstractBlockModifier {

    public static final String ID = ModPath.makeID("InstantBlock");
    public final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final Texture img = TextureLoader.getTexture(ModPath.makeUIPath("block.png"));

    public InstantBlock() {
    }

    @Override
    public int amountLostAtStartOfTurn() {
        return 0;
    }

    @Override
    public boolean shouldStack() {
        return false;
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        int currentAmount = getCurrentAmount();

        addToBot(new InstantAction(() -> {
            reduceThisBlockContainer(currentAmount);
        }));
    }

    @Override
    public String getName() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getDescription() {
        return uiStrings.TEXT[1];
    }

    @Override
    public Color blockImageColor() {
        return new Color(1f, 1f, 1f, 0.5f);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new InstantBlock();
    }
}