package ShoujoKagekiNana.charactor;

import ShoujoKagekiCore.base.BasePlayer;
import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.Res;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.auditionEnergy.DoubleEnergyOrb;
import ShoujoKagekiNana.cards.starter.*;
import ShoujoKagekiNana.relics.ShineRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;
import java.util.List;

public class NanaCharacter extends BasePlayer {

    public static class Enums {
        @SpireEnum(name = "ShoujoKageki_Nana")
        public static PlayerClass ShoujoKageki_Nana;
        @SpireEnum(name = "ShoujoKageki_Nana") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor CardColor_Nana;
        @SpireEnum(name = "ShoujoKageki_Nana")
        public static CardLibrary.LibraryType LibraryType_Nana;
    }

    // =============== CHARACTER ENUMERATORS =================

    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================

    // =============== STRINGS =================

    private static final String ID = ModPath.makeID(Enums.ShoujoKageki_Nana.toString());
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    private static final String ModName = ModPath.ModName;

    // =============== /STRINGS/ =================

    // =============== TEXTURES OF BIG ENERGY ORB ===============


    public static final String[] orbTextures = {ModName + "Resources/images/char/orb/layer1.png",
            ModName + "Resources/images/char/orb/layer2.png",
            ModName + "Resources/images/char/orb/layer3.png",
            ModName + "Resources/images/char/orb/layer4.png",
            ModName + "Resources/images/char/orb/layer5.png",
            ModName + "Resources/images/char/orb/layer6.png",
            ModName + "Resources/images/char/orb/layer1d.png",
            ModName + "Resources/images/char/orb/layer2d.png",
            ModName + "Resources/images/char/orb/layer3d.png",
            ModName + "Resources/images/char/orb/layer4d.png",
            ModName + "Resources/images/char/orb/layer5d.png",};
    public static final String orbVfxPath = ModName + "Resources/images/char/orb/vfx.png";
    public static float[] layerSpeeds = new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public NanaCharacter(String name, PlayerClass setClass) {
//        super(name, setClass, orbTextures, orbVfxPath, null, null, null);
        super(name, setClass, new DoubleEnergyOrb(orbTextures, orbVfxPath, layerSpeeds, orbTextures, orbVfxPath), null, null);

        // =============== TEXTURES, ENERGY, LOADOUT =================

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are,
                // Ctrl+hover to see what they read.)
                Res.THE_DEFAULT_SHOULDER_2, // campfire pose
                Res.THE_DEFAULT_SHOULDER_1, // another campfire pose
                Res.THE_DEFAULT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================

        // =============== ANIMATIONS =================

        loadAnimation(ModPath.getResPath("/images/char/nana/Nana.atlas"),
                ModPath.getResPath("/images/char/nana/Nana.json"),
                1.0f);
//        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
//        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================

        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        maxOrbs = 0;
        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    @Override
    public String getPortraitImageName() {
        return Res.THE_DEFAULT_PORTRAIT;
    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0], STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        Log.logger.info("Begin loading starter Deck Strings");

//        for (AbstractCard card : ModManager.allModCards) {
//            if (card.rarity == AbstractCard.CardRarity.BASIC) {
//                if (card.hasTag(AbstractCard.CardTags.STARTER_DEFEND) || card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
//                    retVal.add(card.cardID);
//                    retVal.add(card.cardID);
//                    retVal.add(card.cardID);
//                    retVal.add(card.cardID);
//                } else {
//                    retVal.add(0, card.cardID);
//                }
//            }
//        }
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(ShineStrike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Attack07.ID);
        return retVal;
    }

    // Starting Relics
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(ShineRelic.ID);
        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
//        CardCrawlGame.sound.stop(AudioPatch.Sound_Karen_OnSelect);
//        CardCrawlGame.sound.play(AudioPatch.Sound_Karen_OnSelect); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false); // Screen
        // Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp
    // respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.CardColor_Nana;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return Res.NanaRenderColor.cpy();
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    // Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new ShineStrike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name
    // parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new NanaCharacter(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in
    // run history.
    @Override
    public Color getCardRenderColor() {
        return Res.NanaRenderColor.cpy();
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return Res.NanaRenderColor.cpy();
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires
    // event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }


    @Override
    public Texture getCutsceneBg() {
        return super.getCutsceneBg();
    }

    @Override
    public Texture getCustomModeCharacterButtonImage() {
        return super.getCustomModeCharacterButtonImage();
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel(ModPath.makeUIPath("CutScene_Karen1.png")));
        panels.add(new CutscenePanel(ModPath.makeUIPath("CutScene_Karen2.png")));
        panels.add(new CutscenePanel(ModPath.makeUIPath("CutScene_Karen3.png")));
        return panels;
    }
}
