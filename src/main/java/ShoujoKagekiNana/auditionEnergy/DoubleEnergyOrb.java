package ShoujoKagekiNana.auditionEnergy;

import ShoujoKagekiNana.ModPath;
import ShoujoKagekiNana.TextureLoader;
import ShoujoKagekiNana.charactor.NanaCharacter;
import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class DoubleEnergyOrb extends CustomEnergyOrb {
    public static final int SECOND_ORB_W = 128;
    public static final int PRIMARY_ORB_W = 128;
    public static final float SECOND_ORB_IMG_SCALE;
    public static final float PRIMARY_ORB_IMG_SCALE;
    public static final float X_OFFSET;
    public static final float Y_OFFSET;
    protected Texture secondBaseLayer;
    protected Texture[] secondEnergyLayers;
    protected Texture[] secondNoEnergyLayers;
    protected float[] secondLayerSpeeds;
    protected float[] secondAngles;
    private Texture mask;
    private FrameBuffer fbo;
    public static float secondVfxTimer;
    public static float secondEnergyVfxAngle;
    public static float secondEnergyVfxScale;
    public static Color secondEnergyVfxColor;
    private static final Hitbox hb;
    private static final UIStrings uiStrings;

    public DoubleEnergyOrb(String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds, String[] orbTexturePathsAlt, String orbVfxPathAlt) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        int numLayers = 5;
        this.secondEnergyLayers = new Texture[numLayers];
        this.secondNoEnergyLayers = new Texture[numLayers];

        assert orbTexturePathsAlt.length >= 3;

        assert orbTexturePathsAlt.length % 2 == 1;

        int middleIdx = orbTexturePathsAlt.length / 2;
        this.secondEnergyLayers = new Texture[middleIdx];
        this.secondNoEnergyLayers = new Texture[middleIdx];

        for (int i = 0; i < middleIdx; ++i) {
            this.secondEnergyLayers[i] = ImageMaster.loadImage(orbTexturePathsAlt[i]);
            this.secondNoEnergyLayers[i] = ImageMaster.loadImage(orbTexturePathsAlt[i + middleIdx + 1]);
        }

        this.secondBaseLayer = TextureLoader.getTexture(ModPath.ModName + "Resources/images/char/orb/layer6.png");
        this.orbVfx = ImageMaster.loadImage(orbVfxPath);
        if (layerSpeeds == null) {
            layerSpeeds = new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
        }

        this.secondLayerSpeeds = layerSpeeds;
        this.secondAngles = new float[this.secondLayerSpeeds.length];

        assert this.secondEnergyLayers.length == this.secondLayerSpeeds.length;

        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
//        this.mask = TextureLoader.getTexture("collectorResources/images/char/mainChar/orb/mask.png");
    }

    public Texture getEnergyImage() {
        return this.orbVfx;
    }

    public void updateOrb(int energyCount) {
        super.updateOrb(energyCount);
        int d = this.secondAngles.length;

        for (int i = 0; i < this.secondAngles.length; ++i) {
            float[] var10000;
            if (energyCount == 0) {
                var10000 = this.secondAngles;
                var10000[i] -= Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d - 1 - i] / 4.0F;
            } else {
                var10000 = this.secondAngles;
                var10000[i] -= Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d - 1 - i];
            }
        }

        if (secondVfxTimer != 0.0F) {
            secondEnergyVfxColor.a = Interpolation.exp10In.apply(0.5F, 0.0F, 1.0F - secondVfxTimer / 2.0F);
            secondEnergyVfxAngle += Gdx.graphics.getDeltaTime() * -30.0F;
            secondEnergyVfxScale = Settings.scale * Interpolation.exp10In.apply(1.0F, 0.1F, 1.0F - secondVfxTimer / 2.0F);
            secondVfxTimer -= Gdx.graphics.getDeltaTime();
            if (secondVfxTimer < 0.0F) {
                secondVfxTimer = 0.0F;
                secondEnergyVfxColor.a = 0.0F;
            }
        }

        hb.update();
    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        hb.move(current_x + X_OFFSET, current_y + Y_OFFSET);
        sb.setColor(Color.WHITE);
        int i;
        if (AbstractDungeon.player.chosenClass.equals(NanaCharacter.Enums.ShoujoKageki_Nana) || AuditionEnergy.count() > 0) {
            if (AuditionEnergy.count() > 0) {
                for (i = 0; i < this.secondEnergyLayers.length; ++i) {
                    sb.draw(this.secondEnergyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, 128, 128, false, false);
                }
            } else {
                for (i = 0; i < this.secondNoEnergyLayers.length; ++i) {
                    sb.draw(this.secondNoEnergyLayers[i], current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, 128, 128, false, false);
                }
            }

            sb.draw(this.secondBaseLayer, current_x + X_OFFSET - 64.0F, current_y + Y_OFFSET - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        }

        sb.setColor(Color.WHITE);
        sb.end();
        this.fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(16640);
        Gdx.gl.glColorMask(true, true, true, true);
        sb.begin();
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 771);
        if (enabled) {
            for (i = 0; i < this.energyLayers.length; ++i) {
                sb.draw(this.energyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } else {
            for (i = 0; i < this.noEnergyLayers.length; ++i) {
                sb.draw(this.noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }

        if (hb.hovered && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            TipHelper.renderGenericTip(50.0F * Settings.scale, 380.0F * Settings.scale, uiStrings.TEXT[0], uiStrings.TEXT[1]);
        }

        // TODO
//        sb.setBlendFunction(0, 770);
//        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
//        sb.draw(this.mask, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 1.0F, 1.0F, 0.0F, 0, 0, 128, 128, false, false);
//        sb.setBlendFunction(770, 771);
        sb.end();
        this.fbo.end();
        sb.begin();
        TextureRegion drawTex = new TextureRegion((Texture) this.fbo.getColorBufferTexture());
        drawTex.flip(false, true);
        sb.draw(drawTex, (float) (-Settings.VERT_LETTERBOX_AMT), (float) (-Settings.HORIZ_LETTERBOX_AMT));
        sb.draw(this.baseLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
        hb.render(sb);
    }

    static {
        SECOND_ORB_IMG_SCALE = 0.75F * Settings.scale;
        PRIMARY_ORB_IMG_SCALE = 1.15F * Settings.scale;
        X_OFFSET = 100.0F * Settings.scale;
        Y_OFFSET = 0.0F * Settings.scale;
        secondVfxTimer = 0.0F;
        secondEnergyVfxAngle = 0.0F;
        secondEnergyVfxScale = Settings.scale;
        secondEnergyVfxColor = Color.WHITE.cpy();
        hb = new Hitbox(80.0F * Settings.scale, 80.0F * Settings.scale);
        uiStrings = CardCrawlGame.languagePack.getUIString(AuditionEnergy.ID);
    }
}