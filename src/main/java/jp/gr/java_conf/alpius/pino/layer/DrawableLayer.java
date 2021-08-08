package jp.gr.java_conf.alpius.pino.layer;

import javafx.scene.image.WritableImage;
import jp.gr.java_conf.alpius.imagefx.Graphics;
import jp.gr.java_conf.alpius.pino.annotations.Name;
import jp.gr.java_conf.alpius.pino.internal.layer.DrawableHelper;
import jp.gr.java_conf.alpius.pino.internal.layer.DrawablePeer;
import jp.gr.java_conf.alpius.pino.internal.layer.LayerPeer;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;

@Name("レイヤー")
public class DrawableLayer extends LayerObject {
    static {
        DrawableHelper.setDrawableAccessor(new DrawableHelper.DrawableAccessor() {
            @Override
            public LayerPeer doCreatePeer(LayerObject object) {
                return ((DrawableLayer) object).doCreatePeer();
            }

            @Override
            public Graphics doGetGraphics(DrawableLayer layer) {
                return layer.getGraphics();
            }

            @Override
            public WritableImage getImage(DrawableLayer layer) {
                return layer.image;
            }
        });
    }

    {
        DrawableHelper.initHelper(this);
    }

    public DrawableLayer() {
        Project p = ProjectManager.getInstance().getProject();
        int w = (int) Math.floor(p.getWidth());
        int h = (int) Math.floor(p.getHeight());
        image = new WritableImage(w, h);
        g = new Graphics(image);
        setName("レイヤー");
    }

    private final WritableImage image;
    private final Graphics g;

    private LayerPeer doCreatePeer() {
        return new DrawablePeer(this, image);
    }

    private Graphics getGraphics() {
        return g;
    }
}
