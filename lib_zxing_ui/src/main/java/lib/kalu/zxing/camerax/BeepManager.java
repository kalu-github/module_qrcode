package lib.kalu.zxing.camerax;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import lib.kalu.zxing.R;
import lib.kalu.zxing.camerax.util.LogUtil;

import java.io.Closeable;

/**
 * @description:
 * @date:  2021-05-07 14:55
 */
public final class BeepManager implements MediaPlayer.OnErrorListener, Closeable {

    private static final long VIBRATE_DURATION = 200L;

    private final Context context;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean playBeep;
    private boolean vibrate;

    BeepManager(Context context) {
        this.context = context;
        this.mediaPlayer = null;
        updatePrefs();
    }

    public void setVibrate(boolean vibrate){
        this.vibrate = vibrate;
    }

    public void setPlayBeep(boolean playBeep){
        this.playBeep = playBeep;
    }

    private synchronized void updatePrefs() {
        if (mediaPlayer == null) {
            mediaPlayer = buildMediaPlayer(context);
        }
        if(vibrator == null){
            vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public synchronized void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private MediaPlayer buildMediaPlayer(Context context) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor file = context.getResources().openRawResourceFd(R.raw.moudle_zxing_raw_beep);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (Exception e) {
            LogUtil.w(e);
            mediaPlayer.release();
            return null;
        }
    }

    @Override
    public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        close();
        updatePrefs();
        return true;
    }

    @Override
    public synchronized void close() {
        try{
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }catch (Exception e){
            LogUtil.e(e);
        }
    }
}