package de.maxiindiestyle.api.ui.xml;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.HashMap;
import java.util.Map;

// TODO-API JavaDoc, cleanup
@SuppressWarnings("unused")
public class XmlStage extends Stage {

    private final HashMap<String, Object> actors;
    private final HashMap<String, Object> reloadableActors;
    private final Skin defaultSkin;
    private final UiXmlLoader loader;

    private Drawable background;
    private boolean isReloading;

    public XmlStage() {
        this(null);
    }

    public XmlStage(Skin defaultSkin) {
        this(defaultSkin, null);
    }

    public XmlStage(Skin defaultSkin, UiXmlLoader loader) {
        super();
        this.defaultSkin = defaultSkin;
        this.loader = loader;
        actors = new HashMap<>();
        reloadableActors = new HashMap<>();
    }

    @Override
    public void draw() {
        if (background != null && super.getRoot().isVisible()) {
            Camera camera = super.getViewport().getCamera();
            camera.update();

            Batch batch = super.getBatch();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            background.draw(batch, 0, 0, super.getWidth(), super.getHeight());
            batch.end();
        }
        super.draw();
    }

    public void reloadAll() {
        isReloading = true;
        System.out.println("Size: " + reloadableActors.size());
        for(Map.Entry<String, Object> actor : reloadableActors.entrySet()) {
            loader.reloadElement(actor.getValue(), actor.getKey());
            System.out.println("Reload: " + actor.getKey());
        }

        System.out.println("Size: " + reloadableActors.size());
        isReloading = false;
    }

    public void reload(String name) {
        isReloading = true;
        Object obj = actors.get(name);
        loader.reloadElement(obj, name);
        isReloading = false;
    }

    public void resize(int width, int height) {
        super.getViewport().setScreenSize(width, height);
    }

    public void putActor(String name, Object obj) {
        if (isReloading) {
            actors.remove(name);
        }
        if(actors.containsKey(name)) {
            System.err.println(name + " already exists in Stage!");
        }
        actors.put(name, obj);
    }

    public void putReloadableActor(String name, Object obj) {
        reloadableActors.put(name, obj);
    }

    public HashMap<String, Object> getElementsMap() {
        return actors;
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public UiXmlLoader getLoader() {
        return loader;
    }
}
