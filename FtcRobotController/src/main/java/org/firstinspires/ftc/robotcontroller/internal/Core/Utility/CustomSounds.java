package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CustomSounds {

    public static final String ROGER_ROGER = "ss_roger_roger";
    // List of available sound resources
    public static final String SKYSTONE_SOUNDS[] =  {"ss_alarm", "ss_bb8_down", "ss_bb8_up", "ss_darth_vader", "ss_fly_by",
            "ss_mf_fail", "ss_laser", "ss_laser_burst", "ss_light_saber", "ss_light_saber_long", "ss_light_saber_short",
            "ss_light_speed", "ss_mine", "ss_power_up", "ss_r2d2_up", "ss_roger_roger", "ss_siren", "ss_wookie" };
    private int soundID = -1;
    private boolean soundPlaying = false;

    private HardwareMap map;
    private Context context;
    private SoundPlayer soundPlayer;
    private SoundPlayer.PlaySoundParams parameters = new SoundPlayer.PlaySoundParams();

    public CustomSounds(HardwareMap hardwareMap){
        map = hardwareMap;
        context = map.appContext;
        parameters.loopControl = 0;
        parameters.waitForNonLoopingSoundsToFinish = false;
        soundPlayer = SoundPlayer.getInstance();
    }

    public void playSkystoneSound(int index){
        if (index > -1 && index < SKYSTONE_SOUNDS.length){
            playSound(SKYSTONE_SOUNDS[index]);
        }
    }

    public void playSound(String s){
        soundID = context.getResources().getIdentifier(s, "raw", context.getPackageName());
        if (soundID != 0){
            soundPlayer.stopPlayingAll();
            soundPlaying = true;
            soundPlayer.startPlaying(context, soundID, parameters, null, new Runnable(){
                public void run() {
                    soundPlaying = false;
                }
            });
        }
    }

    public boolean isPlayingSound(){
        return soundPlaying;
    }
}
