package executor;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

public class MandelbrotPart implements Callable<BufferedImage> {

    private BufferedImage I;
    private int width;
    private int height;
    private int fromY;
    private int toY;

    MandelbrotPart(BufferedImage i, int width, int height, int fromY, int toY) {
        this.I = i;
        this.width = width;
        this.height = height;
        this.fromY = fromY;
        this.toY = toY;
    }

    @Override
    public BufferedImage call() throws Exception {
        for (int y = this.fromY; y < this.toY; y++) {
            for (int x = 0; x <width; x++) {
                double zy;
                double zx = zy = 0;
                double ZOOM = width/4.0;
                double cX = (x - width/2.0) / ZOOM;
                double cY = (y - height/2.0) / ZOOM;
                int iter = 570;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                I.setRGB(x, y, iter | (iter << 8));
            }
        }
        return I;
    }
}
