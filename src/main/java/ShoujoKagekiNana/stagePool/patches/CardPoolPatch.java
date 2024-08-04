package ShoujoKagekiNana.stagePool.patches;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.stagePool.StagePoolManager;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnStartBattleSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.ArrayList;

@SpireInitializer
public class CardPoolPatch {


    public static void initialize() {
        BaseMod.subscribe(new OnStartBattleSubscriber() {
            @Override
            public void receiveOnBattleStart(AbstractRoom abstractRoom) {
                StagePoolManager.onBattleStart();
            }
        });

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
    }


    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "initializeCardPools"
    )
    public static class _InitPatch {
        public static void Postfix() {
            StagePoolManager.initializeCardPools();
        }
    }

    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, AbstractPlayer.class, ArrayList.class}
    )
    public static class _LoadPatch {
        public static void Postfix() {
            StagePoolManager.loadCardPool(null);
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

    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "generateSeeds"
    )
    public static class _SeedPatch1 {
        public static void Postfix() {
            StagePoolManager.rng = new Random(Settings.seed);
        }
    }

//    @SpirePatch2(
//            clz = AbstractDungeon.class,
//            method = "loadSeeds"
//    )
//    public static class _SeedPatch2 {
//        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
//            StagePoolManager.initSeed(Settings.seed, 0);
//        }
//    }
}
