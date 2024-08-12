package ShoujoKagekiNana.blossom;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = "<class>"
)
public class BlossomField {
    public static SpireField<Boolean> blossom = new SpireField<>(() -> false); // can blossom
    public static SpireField<Boolean> blossomed = new SpireField<>(() -> false);

    public static void set(AbstractCard card) {
        blossom.set(card, true);
    }

    public static boolean isBlossomed(AbstractCard card) {
        return blossomed.get(card);
    }
}
