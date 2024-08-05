package ShoujoKagekiNana.stagePool.patches;

import ShoujoKagekiNana.Log;
import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PreStartGameSubscriber;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static basemod.patches.com.megacrit.cardcrawl.core.CardCrawlGame.LoadPlayerSaves.getErrorMod;

@SpireInitializer
public class StagePoolPatch {

    private static Gson gson = null;

    public static ArrayList<AbstractCard> cache_stage_pool_cards;

    public static void initialize() {
        BaseMod.subscribe((OnStartBattleSubscriber) abstractRoom -> StagePoolManager.onBattleStart());

        BaseMod.addSaveField(ModPath.makeID("stage_pool_seed_count"), new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                if (StagePoolManager.rng != null) {
                    return StagePoolManager.rng.counter;
                }
                return 0;
            }

            @Override
            public void onLoad(Integer integer) {
                if (integer == null) integer = 0;
                StagePoolManager.rng = new Random(Settings.seed, integer);
                Log.logger.info("init rng with load");
            }
        });

        BaseMod.addSaveField(ModPath.makeID("stage_pool_remove_cnt"), new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return StagePoolManager.stage_remove_count;
            }

            @Override
            public void onLoad(Integer integer) {
                if (integer == null) integer = 0;
                StagePoolManager.stage_remove_count = integer;
            }
        });

        BaseMod.addSaveField(ModPath.makeID("stage_pool_cards"), new CustomSavable<ArrayListOfCardSaveWithModifier>() {
            @Override
            public ArrayListOfCardSaveWithModifier onSave() {
                initGson();
                ArrayListOfCardSaveWithModifier cards = new ArrayListOfCardSaveWithModifier();
                for (AbstractCard card : StagePoolManager.cardPool) {
                    CardSaveWithModifier cardSave = new CardSaveWithModifier();
                    cardSave.cardSave = new CardSave(card.cardID, card.timesUpgraded, card.misc);
                    cardSave.modifiers = cardModifiersToString(card);
                    cards.add(cardSave);
                }
                Log.logger.info("onSave stage_pool_cards = {}", cards.size());
                return cards;
            }

            @Override
            public void onLoad(ArrayListOfCardSaveWithModifier cardSaveWithModifiers) {
                if (cardSaveWithModifiers == null) {
                    cache_stage_pool_cards = null;
                    return;
                }
                initGson();
                cache_stage_pool_cards = new ArrayList<>();
                for (CardSaveWithModifier cardSave : cardSaveWithModifiers) {
                    AbstractCard card = CardLibrary.getCopy(cardSave.cardSave.id, cardSave.cardSave.upgrades, cardSave.cardSave.misc);
                    loadCardModifiers(card, cardSave.modifiers);
                    cache_stage_pool_cards.add(card);
                }
                Log.logger.info("onLoad stage_pool_cards = {}", cache_stage_pool_cards.size());
            }
        });

        BaseMod.subscribe((PreStartGameSubscriber) () -> {
            StagePoolManager.rng = null;
            StagePoolManager.stage_remove_count = 0;
            cache_stage_pool_cards = null;
            Log.logger.info("clear data on new game start");
        });


    }

    private static void initGson() {
        GsonBuilder builder = new GsonBuilder();
        if (CardModifierPatches.modifierAdapter == null) {
            CardModifierPatches.initializeAdapterFactory();
        }
        builder.registerTypeAdapterFactory(CardModifierPatches.modifierAdapter);
        gson = builder.create();
    }


//    @SpirePatch2(
//            clz = AbstractDungeon.class,
//            method = "initializeCardPools"
//    )
//    public static class _InitPatch {
//        public static void Postfix() {
//            StagePoolManager.initializeCardPools();
//        }
//    }
//
//
//    // clear data on new game start
//    @SpirePatch2(
//            clz = CardCrawlGame.class,
//            method = "update"
//    )
//    public static class _InitPatch {
//        @SpireInsertPatch(locator = Locator.class)
//        public static void Insert() {
//        }
//
//        public static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctBehavior) throws Exception {
//                Matcher m = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "loadingSave");
//                return LineFinder.findInOrder(ctBehavior, m);
//            }
//        }
//    }

    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, AbstractPlayer.class, ArrayList.class}
    )
    public static class _LoadPatch {
        public static void Postfix(AbstractDungeon __instance) {
            if (__instance instanceof Exordium) {
                StagePoolManager.loadCardPool(null);
            }
        }
    }

    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, AbstractPlayer.class, SaveFile.class}
    )
    public static class _LoadPatch2 {
        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            StagePoolManager.loadCardPool(saveFile);
        }
    }

//    @SpirePatch2(
//            clz = AbstractDungeon.class,
//            method = "generateSeeds"
//    )
//    public static class _SeedPatch1 {
//        public static void Prefix() {
//            StagePoolManager.rng = new Random(Settings.seed);
//            Log.logger.info("init rng with generateSeeds");
//        }
//    }

//    @SpirePatch2(
//            clz = AbstractDungeon.class,
//            method = "loadSeeds"
//    )
//    public static class _SeedPatch2 {
//        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
//            StagePoolManager.initSeed(Settings.seed, 0);
//        }
//    }


    // copy from baseMod:ConstructSaveFilePatch
    public static JsonElement cardModifiersToString(AbstractCard card) {
        JsonElement result = null;
        ArrayList<AbstractCardModifier> cardModifierList = CardModifierManager.modifiers(card);
        List<AbstractCardModifier> saveIgnores = cardModifierList.stream().filter(CardModifierPatches::unsavable).collect(Collectors.toList());
        if (!saveIgnores.isEmpty()) {
            BaseMod.logger.warn("attempted to save un-savable card modifier(s). Un-serializable modifiers and modifiers marked @SaveIgnore will not be saved on master deck.");
            BaseMod.logger.info("affected card: " + card.cardID);
            for (AbstractCardModifier mod : saveIgnores) {
                BaseMod.logger.info("   unsavable mod: " + mod.getClass().getName());
            }
            cardModifierList.removeAll(saveIgnores);
        }
        if (!cardModifierList.isEmpty()) {
            try {
                result = gson.toJsonTree(cardModifierList, (new TypeToken<ArrayList<AbstractCardModifier>>() {
                }).getType());
            } catch (Exception var12) {
                System.out.println("Could not save card modifier list for " + card + ".");
                System.out.println("Modifier list:");
                for (int i = 0; i < cardModifierList.size(); ++i) {
                    System.out.println("    " + i + ": " + cardModifierList.get(i));
                }
                var12.printStackTrace();
            }
        }
//        Log.logger.info("cardModifiersToString = {}", result);
        return result;
    }

    public static void loadCardModifiers(AbstractCard card, JsonElement loaded) {
        ArrayList<AbstractCardModifier> cardModifiers = new ArrayList<>();
        if (loaded != null && loaded.isJsonArray()) {
            JsonArray array = loaded.getAsJsonArray();
            for (JsonElement element : array) {
                AbstractCardModifier cardModifier = null;
                try {
                    cardModifier = gson.fromJson(element, (new TypeToken<AbstractCardModifier>() {
                    }).getType());
                } catch (Exception var16) {
                    System.out.println("Unable to load cardmod: " + element);
                    Log.logger.info("Unable to load cardmod: {} ", loaded);
                    cardModifiers.add(getErrorMod());
                }

                if (cardModifier != null) {
                    cardModifiers.add(cardModifier);
                }
            }
        }

        CardModifierManager.removeAllModifiers(card, true);
        for (AbstractCardModifier mod : cardModifiers)
            CardModifierManager.addModifier(card, mod.makeCopy());
    }

    public static class ArrayListOfCardSaveWithModifier extends ArrayList<CardSaveWithModifier> {
    }

    public static class CardSaveWithModifier {
        public CardSave cardSave;
        public JsonElement modifiers;
    }


//    @SpirePatch2(
//            clz = SaveFile.class,
//            method = "<ctor>",
//            paramtypez = {SaveFile.SaveType.class}
//    )
//    public static class Render {
//        public static void Postfix(SaveFile __instance) {
//            Log.logger.info("ConstructSaveFilePatch = {}", ModSaves.cardModifierSaves.get(__instance));
//        }
//    }
}
