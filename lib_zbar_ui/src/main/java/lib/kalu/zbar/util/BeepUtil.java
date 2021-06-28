package lib.kalu.zbar.util;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import androidx.annotation.RawRes;

import lib.kalu.zbar.R;
import lib.kalu.zbar.contentprovider.ContextProviderZxing;

/**
 * @description:
 * @date: 2021-05-07 14:55
 */
public final class BeepUtil {

    // 默认无声
    public static boolean beep = false;
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    public static final void beep() {
        beep(R.raw.moudle_zbar_raw_beep);
    }

    public static final void beep(@RawRes int raw) {
        try {

            if (null == mediaPlayer) {
                mediaPlayer = new MediaPlayer();
            }

            AssetFileDescriptor file = ContextProviderZxing.mContext.getResources().openRawResourceFd(raw);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
//             mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
        } catch (Exception e) {
            LogUtil.log(e.getMessage(), e);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static final void release() {
        try {
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            LogUtil.log(e.getMessage(), e);
        }
    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        return false;
//    }
//
//    @Override
//    public void close() throws IOException {
//        try {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        } catch (Exception e) {
//            LogUtil.log(e.getMessage(), e);
//        }
//    }
}