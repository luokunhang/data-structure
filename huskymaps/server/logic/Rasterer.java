package huskymaps.server.logic;

import huskymaps.params.RasterRequest;
import huskymaps.params.RasterResult;

import java.util.Objects;

import static huskymaps.utils.Constants.LAT_PER_TILE;
import static huskymaps.utils.Constants.LON_PER_TILE;
import static huskymaps.utils.Constants.MIN_X_TILE_AT_DEPTH;
import static huskymaps.utils.Constants.MIN_Y_TILE_AT_DEPTH;
import static huskymaps.utils.Constants.MIN_ZOOM_LEVEL;
import static huskymaps.utils.Constants.ROOT_LRLAT;
import static huskymaps.utils.Constants.ROOT_LRLON;
import static huskymaps.utils.Constants.ROOT_ULLAT;
import static huskymaps.utils.Constants.ROOT_ULLON;

/** Application logic for the RasterAPIHandler. */
public class Rasterer {

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param request RasterRequest
     * @return RasterResult
     */
    public static RasterResult rasterizeMap(RasterRequest request) {
        double ullat = request.ullat;
        double ullon = request.ullon;
        double lrlat = request.lrlat;
        double lrlon = request.lrlon;
        if (ullat > ROOT_ULLAT) {
            ullat = ROOT_ULLAT;
        }
        if (ullon < ROOT_ULLON) {
            ullon = ROOT_ULLON;
        }
        if (lrlat < ROOT_LRLAT) {
            lrlat = ROOT_LRLAT;
        }
        if (lrlon > ROOT_LRLON) {
            lrlon = ROOT_LRLON;
        }
        int uly = (int) ((ROOT_ULLAT - ullat) / LAT_PER_TILE[request.depth]);
        int ulx = (int) ((ullon - ROOT_ULLON) / LON_PER_TILE[request.depth]);
        int lry = (int) ((ROOT_ULLAT - lrlat) / LAT_PER_TILE[request.depth]);
        int lrx = (int) ((lrlon - ROOT_ULLON) / LON_PER_TILE[request.depth]);

        Tile[][] grid = new Tile[lry - uly + 1][lrx - ulx + 1];
        for (int i = ulx; i <= lrx; i++) {
            for (int j = uly; j <= lry; j++) {
                grid[j - uly][i - ulx] = new Tile(request.depth, i, j);
            }
        }
        return new RasterResult(grid);
    }

    public static class Tile {
        public final int depth;
        public final int x;
        public final int y;

        public Tile(int depth, int x, int y) {
            this.depth = depth;
            this.x = x;
            this.y = y;
        }

        public Tile offset() {
            return new Tile(depth, x + 1, y + 1);
        }

        /**
         * Return the latitude of the upper-left corner of the given slippy map tile.
         * @return latitude of the upper-left corner
         * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
         */
        public double lat() {
            double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
            int slippyY = MIN_Y_TILE_AT_DEPTH[depth] + y;
            double latRad = Math.atan(Math.sinh(Math.PI * (1 - 2 * slippyY / n)));
            return Math.toDegrees(latRad);
        }

        /**
         * Return the longitude of the upper-left corner of the given slippy map tile.
         * @return longitude of the upper-left corner
         * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
         */
        public double lon() {
            double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
            int slippyX = MIN_X_TILE_AT_DEPTH[depth] + x;
            return slippyX / n * 360.0 - 180.0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tile tile = (Tile) o;
            return depth == tile.depth &&
                    x == tile.x &&
                    y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(depth, x, y);
        }

        @Override
        public String toString() {
            return "d" + depth + "_x" + x + "_y" + y + ".jpg";
        }
    }
}
