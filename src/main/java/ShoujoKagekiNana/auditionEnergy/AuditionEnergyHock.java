package ShoujoKagekiNana.auditionEnergy;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerTurnStartSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpireInitializer
public class AuditionEnergyHock {
    public static void initialize() {
        BaseMod.subscribe(new StartGameSubscriber() {
            @Override
            public void receiveStartGame() {
                AuditionEnergy.reset();
            }
        });
        BaseMod.subscribe(new OnStartBattleSubscriber() {
            @Override
            public void receiveOnBattleStart(AbstractRoom abstractRoom) {
                AuditionEnergy.reset();
            }
        });
        BaseMod.subscribe(new PostBattleSubscriber() {
            @Override
            public void receivePostBattle(AbstractRoom abstractRoom) {
                AuditionEnergy.reset();
            }
        });
        BaseMod.subscribe(new OnPlayerTurnStartSubscriber() {
            @Override
            public void receiveOnPlayerTurnStart() {
                AuditionEnergy.reset();
            }
        });
    }
}
