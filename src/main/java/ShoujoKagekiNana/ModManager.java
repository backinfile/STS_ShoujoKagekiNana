package ShoujoKagekiNana;

import ShoujoKagekiCore.base.SharedRelic;
import ShoujoKagekiNana.charactor.NanaCharacter;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpireSideload;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@SpireInitializer
@SpireSideload(modIDs = "ShoujoKagekiCore")
public class ModManager implements ISubscriber, EditStringsSubscriber, PostInitializeSubscriber,
        EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, AddAudioSubscriber {
    public static final Logger logger = LogManager.getLogger(ModPath.ModName);

    private static String modID;

    public ModManager() {
        BaseMod.subscribe(this);
        setModID(ModPath.ModName);

        Log.logger.info("Creating the color " + NanaCharacter.Enums.CardColor_Nana.toString());
        BaseMod.addColor(NanaCharacter.Enums.CardColor_Nana,
                Res.NanaRenderColor.cpy(),
                Res.NanaRenderColor.cpy(),
                Res.NanaRenderColor.cpy(),
                Res.NanaRenderColor.cpy(),
                Res.NanaRenderColor.cpy(),
                Res.NanaRenderColor.cpy(),
                Res.NanaRenderColor.cpy(),
                Res.ATTACK_DEFAULT_GRAY, Res.SKILL_DEFAULT_GRAY, Res.POWER_DEFAULT_GRAY,
                Res.ENERGY_ORB_DEFAULT_GRAY, Res.ATTACK_DEFAULT_GRAY_PORTRAIT, Res.SKILL_DEFAULT_GRAY_PORTRAIT,
                Res.POWER_DEFAULT_GRAY_PORTRAIT, Res.ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, Res.CARD_ENERGY_ORB);

        SettingsPanel.initProperties();

        Log.logger.info("Done creating the color");
    }

    public static void initialize() {
        Log.logger.info("========================= Initializing ShoujoKageki Mod. =========================");
        new ModManager();
        Log.logger.info("========================= /ShoujoKageki Initialized. Hello World./ =========================");
    }


    @Override
    public void receiveEditStrings() {
        Log.logger.info("Beginning to edit strings for mod with ID: " + ModPath.getModId());
        String lang = getLang();

        BaseMod.loadCustomStringsFile(CardStrings.class,
                ModPath.getResPath("/localization/" + lang + "/Card-Strings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                ModPath.getResPath("/localization/" + lang + "/Relic-Strings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                ModPath.getResPath("/localization/" + lang + "/UI-Strings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                ModPath.getResPath("/localization/" + lang + "/Character-Strings.json"));
        BaseMod.loadCustomStringsFile(StanceStrings.class,
                ModPath.getResPath("/localization/" + lang + "/Stance-Strings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                ModPath.getResPath("/localization/" + lang + "/Power-Strings.json"));


        Log.logger.info("Done edittting strings");
    }

    public static String getLang() {
        Settings.GameLanguage lang = Settings.language;
        if (lang == Settings.GameLanguage.ZHS || lang == Settings.GameLanguage.ZHT) {
            return "zhs";
        } else {
            return "eng";
        }
    }

    @Override
    public void receiveEditCharacters() {
        Log.logger.info("Beginning to edit characters. " + "Add " + NanaCharacter.Enums.ShoujoKageki_Nana.toString());
        BaseMod.addCharacter(new NanaCharacter("ShoujoKageki_Nana", NanaCharacter.Enums.ShoujoKageki_Nana), Res.THE_DEFAULT_BUTTON,
                Res.THE_DEFAULT_PORTRAIT, NanaCharacter.Enums.ShoujoKageki_Nana);
        Log.logger.info("Added " + NanaCharacter.Enums.ShoujoKageki_Nana.toString());
    }

    @Override
    public void receivePostInitialize() {
        SettingsPanel.initPanel();
        logger.info("Done loading badge Image and mod options");
    }

    @Override
    public void receiveEditCards() {
        Log.logger.info("Adding variables");
        pathCheck();
        // Add the Custom Dynamic Variables
        Log.logger.info("Add variables");
        // Add the Custom Dynamic variables
//        BaseMod.addDynamicVariable(new DefaultCustomVariable());
//        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
//        BaseMod.addDynamicVariable(new DisposableVariable());

        Log.logger.info("Adding cards");
        String cardsClassPath = this.getClass().getPackage().getName() + ".cards";
        new AutoAdd(ModPath.getModId()).packageFilter(cardsClassPath).setDefaultSeen(true).any(AbstractCard.class, (info, card) -> {
            BaseMod.addCard(card);
            if (info.seen) {
                UnlockTracker.unlockCard(card.cardID);
            }
//            if (card.color == CardColor_Karen) {
//                allModCards.add(card);
//            }
        });
        Log.logger.info("Done adding cards!");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files
                .internal(ModPath.getResPath("/localization/" + getLang() + "/Keyword-Strings.json"))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(ModPath.ModName.toLowerCase(), keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
                Log.logger.info("-----------------add keyword: " + keyword.NAMES[0]);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        String relicClassPath = this.getClass().getPackage().getName() + ".relics";
        Log.logger.info("===============Adding relics: search in " + relicClassPath);
        for (com.evacipated.cardcrawl.modthespire.ModInfo info : Loader.MODINFOS) {
            Log.logger.info(info.ID);
        }
        new AutoAdd(ModPath.getModId()).packageFilter(relicClassPath).any(CustomRelic.class, (info, relic) -> {
            if (relic.getClass().isAnnotationPresent(SharedRelic.class)) {
                BaseMod.addRelic(relic, RelicType.SHARED);
            } else {
                BaseMod.addRelicToCustomPool(relic, NanaCharacter.Enums.CardColor_Nana);
            }
//			if (info.seen || relic.tier == RelicTier.STARTER)
            UnlockTracker.markRelicAsSeen(relic.relicId);
            Log.logger.info("Adding relics: " + relic.relicId);
        });
        Log.logger.info("Done adding relics!");
    }


    @Override
    public void receiveAddAudio() {
        AudioManager.init();
    }


    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO
    // ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        // String IDjson =
        // Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8));
        // // i hate u Gdx.files
        InputStream in = ModManager.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T
        // EDIT
        // THIS
        // ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8),
                IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP
            // JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        // String IDjson =
        // Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8));
        // // i still hate u btw Gdx.files
        InputStream in = ModManager.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T
        // EDIT
        // THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8),
                IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = ModManager.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE,
        // THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT
                // THIS
            } // NO
        } // NO
    }// NO


    // ====== YOU CAN EDIT AGAIN ======
}
