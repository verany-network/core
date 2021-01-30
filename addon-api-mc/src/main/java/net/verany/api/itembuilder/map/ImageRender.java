package net.verany.api.itembuilder.map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

public class ImageRender extends MapRenderer {

    private final SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public ImageRender(String url) throws IOException {
        cacheImage = new SoftReference<>(getImage(url));
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        if (this.hasRendered) {
            return;
        }

        if (cacheImage.get() != null) {
            mapCanvas.drawImage(0, 0, cacheImage.get());
        } else {
            player.sendMessage("Attempted to render the image, but the cached image was null!");
        }
        hasRendered = true;
    }

    private BufferedImage getImage(String url) throws IOException {
        boolean useCache = ImageIO.getUseCache();
        ImageIO.setUseCache(false);
        BufferedImage image = resize(new URL(url), new Dimension(128, 128));
        ImageIO.setUseCache(useCache);
        return image;
    }

    private BufferedImage resize(URL url, Dimension size) throws IOException {
        BufferedImage image = ImageIO.read(url);
        BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = resized.createGraphics();
        graphics.drawImage(image, 0, 0, size.width, size.height, null);
        graphics.dispose();
        return resized;
    }
}
