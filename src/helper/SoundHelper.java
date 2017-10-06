/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author user
 */
public class SoundHelper {

    public static File Error1Sound = new File("error1.WAV");
    public static File Error2Sound = new File("error2.WAV");
    public static File Ok1Sound = new File("ok1.WAV");

    /**
     *
     * @param sound
     * @param type 
     * 1 - OK Sound 1
     */
    public static void PlayOkSound(File sound, int type) {
        try {
            Clip clip = AudioSystem.getClip();

            if (sound == null) {

                switch (type) {                    
                    case 1:
                        clip.open(AudioSystem.getAudioInputStream(Ok1Sound));
                        clip.start();
                        Thread.sleep(clip.getMicrosecondLength() / 1000);
                        break;
                }
            } else {
                clip.open(AudioSystem.getAudioInputStream(sound));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }

        } catch (Exception e) {

        }
    }
    
    /**
     * 
     * @param sound
     * @param type 
     */
    public static void PlayNotificationSound(File sound, int type) {
        try {
            Clip clip = AudioSystem.getClip();

            if (sound == null) {

                switch (type) {
                    case 1:                        
                        clip.open(AudioSystem.getAudioInputStream(Error1Sound));
                        clip.start();
                        //Thread.sleep(clip.getMicrosecondLength() / 500);
                        break;
                    case 2:
                        clip.open(AudioSystem.getAudioInputStream(Error2Sound));
                        clip.start();
                        //Thread.sleep(clip.getMicrosecondLength() / 500);
                        break;
                }
            } else {
                clip.open(AudioSystem.getAudioInputStream(sound));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }

        } catch (Exception e) {

        }
    }

}
