package com.quinstedt.islandRush;

public class Utils {
    public static void delay(int time){
        try{
            Thread.sleep(time);
        }catch(Exception exception){
            exception.getStackTrace();
        }
    }

    /**
     * @param unicode - Emoji Code
     * For more emojis: https://unicode.org/emoji/charts/full-emoji-list.html
     * Tips: change U+ with 0x
     */

    public static final int HAPPY =0x1F601;
    public static final int CHECKED =0x2705;
    public static final int CROSSED_FINGERS =0x1F91E;
    public static final int APPLAUSE = 0x1F44F;
    public static final int TIME_EMOJI = 0x23F3;

    public static String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }

}
