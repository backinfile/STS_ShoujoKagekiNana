package ShoujoKagekiNana.relics;

import ShoujoKagekiNana.ModPath;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FrogWaterCupRelic extends BaseRelic {
    public static final String RAW_ID = FrogWaterCupRelic.class.getSimpleName();
    public static final String ID = ModPath.makeID(RAW_ID);

    public FrogWaterCupRelic() {
        super(ID, RAW_ID, RelicTier.COMMON, LandingSound.FLAT);
    }


    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }
}
