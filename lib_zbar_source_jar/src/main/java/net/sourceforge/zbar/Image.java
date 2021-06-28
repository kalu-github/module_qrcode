/*------------------------------------------------------------------------
 *  Image
 *
 *  Copyright 2007-2010 (c) Jeff Brown <spadix@users.sourceforge.net>
 *
 *  This file is part of the ZBar Bar Code Reader.
 *
 *  The ZBar Bar Code Reader is free software; you can redistribute it
 *  and/or modify it under the terms of the GNU Lesser Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  The ZBar Bar Code Reader is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with the ZBar Bar Code Reader; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 *  Boston, MA  02110-1301  USA
 *
 *  http://sourceforge.net/projects/zbar
 *------------------------------------------------------------------------*/

package net.sourceforge.zbar;

import androidx.annotation.Keep;

/**
 * stores image data samples along with associated format and size
 * metadata.
 */
@Keep
public final class Image {

    static {
        System.loadLibrary("zbar");
        init();
    }

    /**
     * C pointer to a zbar_symbol_t.
     */
    private long peer;
    private Object data;

    public Image() {
        peer = create();
    }

    public Image(int width, int height) {
        this();
        setSize(width, height);
    }

    public Image(int width, int height, Format format) {
        this();
        setSize(width, height);
        setFormat(format.toString());
    }

    public Image(Format format) {
        this();
        setFormat(format.toString());
    }

    Image(long peer) {
        this.peer = peer;
    }

    @Keep
    private static native void init();

    /**
     * Create an associated peer instance.
     */
    @Keep
    private native long create();

    protected void finalize() {
        destroy();
    }

    /**
     * Clean up native data associated with an instance.
     */
    public synchronized void destroy() {
        if (peer != 0) {
            destroy(peer);
            peer = 0;
        }
    }

    /**
     * Destroy the associated peer instance.
     */
    @Keep
    private native void destroy(long peer);

    /**
     * Image format conversion.
     *
     * @returns a @em new image with the sample data from the original
     * image converted to the requested format fourcc.  the original
     * image is unaffected.
     */
    public Image convert(String format) {
        long newpeer = convert(peer, format);
        if (newpeer == 0)
            return (null);
        return (new Image(newpeer));
    }

    @Keep
    private native long convert(long peer, String format);

    /**
     * Retrieve the image format fourcc.
     */
    @Keep
    public native String getFormat();

    /**
     * Specify the fourcc image format code for image sample data.
     */
    @Keep
    public native void setFormat(String format);

    /**
     * Retrieve a "sequence" (page/frame) number associated with this
     * image.
     */
    @Keep
    public native int getSequence();

    /**
     * Associate a "sequence" (page/frame) number with this image.
     */
    @Keep
    public native void setSequence(int seq);

    /**
     * Retrieve the width of the image.
     */
    @Keep
    public native int getWidth();

    /**
     * Retrieve the height of the image.
     */
    @Keep
    public native int getHeight();

    /**
     * Retrieve the size of the image.
     */
    @Keep
    public native int[] getSize();

    /**
     * Specify the pixel size of the image.
     */
    @Keep
    public native void setSize(int[] size);

    /**
     * Specify the pixel size of the image.
     */
    @Keep
    public native void setSize(int width, int height);

    /**
     * Retrieve the crop region of the image.
     */
    @Keep
    public native int[] getCrop();

    /**
     * Specify the crop region of the image.
     */
    @Keep
    public native void setCrop(int[] crop);

    /**
     * Specify the crop region of the image.
     */
    @Keep
    public native void setCrop(int x, int y, int width, int height);

    /**
     * Retrieve the image sample data.
     */
    @Keep
    public native byte[] getData();

    /**
     * Specify image sample data.
     */
    @Keep
    public native void setData(byte[] data);

    /**
     * Specify image sample data.
     */
    @Keep
    public native void setData(int[] data);

    /**
     * Retrieve the decoded results associated with this image.
     */
    public SymbolSet getSymbols() {
        return (new SymbolSet(getSymbols(peer)));
    }

    @Keep
    private native long getSymbols(long peer);

}
