package ShoujoKagekiNana.stances.patches;

import ShoujoKagekiNana.stances.RevueStance;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import javassist.CtBehavior;

import java.util.ArrayList;

public class StancePatches {
    @SpirePatch2(
            clz = AbstractStance.class,
            method = "getStanceFromName"
    )
    public static class _CreatePatch {
        public static SpireReturn<AbstractStance> Prefix(String name) {
            if (name.equals(RevueStance.STANCE_ID)) {
                return SpireReturn.Return(new RevueStance());
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(
            clz = AbstractCreature.class,
            method = SpirePatch.CLASS
    )
    public static class Field {
        public static SpireField<AbstractStance> stance = new SpireField<>(NeutralStance::new);
    }

    @SpirePatch2(
            clz = AbstractStance.class,
            method = SpirePatch.CLASS
    )
    public static class Field2 {
        public static SpireField<AbstractCreature> owner = new SpireField<>(() -> null);
    }

    @SpirePatch2(
            clz = AbstractCreature.class,
            method = "updateAnimations"
    )
    public static class _UpdatePatch {
        public static void Postfix(AbstractCreature __instance) {
            Field.stance.get(__instance).update();
        }
    }

    @SpirePatch2(
            clz = AbstractCreature.class,
            method = "renderHealth"
    )
    public static class _RenderPatch {
        public static void Postfix(AbstractCreature __instance, SpriteBatch sb) {
            Field.stance.get(__instance).render(sb);
        }
    }

    @SpirePatch2(
            clz = AbstractMonster.class,
            method = "renderTip"
    )
    public static class _RenderPatch2 {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractMonster __instance, SpriteBatch sb, ArrayList<PowerTip> ___tips) {
            AbstractStance stance = Field.stance.get(__instance);
            if (!stance.ID.equals("Neutral")) {
                ___tips.add(new PowerTip(stance.name, stance.description));
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "powers");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
