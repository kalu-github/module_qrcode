/*------------------------------------------------------------------------
 *  ImageScanner
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
 * Read barcodes from 2-D images.
 */
@Keep
public final class ImageScanner {

    static {
        System.loadLibrary("zbar");
        init();
    }

    /**
     * C pointer to a zbar_image_scanner_t.
     */
    private long peer;

    private ImageScanner() {
        peer = create();
        enableCache(false);
    }

    public static final class Holder {
        static final ImageScanner imageScanner = new ImageScanner();
    }

    public static final ImageScanner getInstance() {
        return Holder.imageScanner;
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
     * Set config for indicated symbology (0 for all) to specified value.
     */
    @Keep
    public native void setConfig(int symbology, int config, int value)
            throws IllegalArgumentException;

    /**
     * Parse configuration string and apply to image scanner.
     */
    @Keep
    public native void parseConfig(String config);

    /**
     * Enable or disable the inter-image result cache (default disabled).
     * Mostly useful for scanning video frames, the cache filters duplicate
     * results from consecutive images, while adding some consistency
     * checking and hysteresis to the results.  Invoking this method also
     * clears the cache.
     */
    @Keep
    public native void enableCache(boolean enable);

    /**
     * Retrieve decode results for last scanned image.
     *
     * @returns the SymbolSet result container
     */
    public SymbolSet getResults() {
        return (new SymbolSet(getResults(peer)));
    }

    @Keep
    private native long getResults(long peer);

    /**
     * Scan for symbols in provided Image.
     * The image format must currently be "Y800" or "GRAY".
     *
     * @returns the number of symbols successfully decoded from the image.
     */
    @Keep
    public native int scanImage(Image image);
}
