package test;

import org.junit.jupiter.api.Test;
import ragmad.sound_engine.MusicClips;
import ragmad.sound_engine.Sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

public class SoundEngine {

    private final MusicClips MAINMENU = new MusicClips ("res/sounds/got.wav");
//    private final MusicClips BUTTON = new MusicClips ("res/sounds/button_sound.wav");
    
    @Test
    void soundUpdateMethodWorks() throws LineUnavailableException {
        Sound sound = new Sound();
        sound.openAudio(MAINMENU.toString(),1000);
        assertTrue(sound.isClipRunning());
    }

    @Test
    void soundPauseMethodWorks(){
        Sound sound = new Sound();
        sound.openAudio(MAINMENU.toString(),1000);
        sound.pauseSound();
        assertFalse(sound.isClipRunning());
    }
}