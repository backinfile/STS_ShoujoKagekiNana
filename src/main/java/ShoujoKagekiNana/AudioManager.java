package ShoujoKagekiNana;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AudioManager {
    public static final String PassionateRevue = "PassionateRevue.WAV";

    public static void init() {
        for (Field field : AudioManager.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                if (field.getType() == String.class) {
                    BaseMod.addAudio(ModPath.makeID(PassionateRevue), ModPath.makeAudioPath(PassionateRevue));
                }
            }
        }
    }

    public static void play(String ID) {
        CardCrawlGame.sound.play(ModPath.makeID(ID));
    }

    public static void stop(String ID) {
        CardCrawlGame.sound.stop(ModPath.makeID(ID));
    }
}
