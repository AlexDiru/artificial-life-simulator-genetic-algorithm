package alexdiru.lifesim.main;
import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import 

com.badlogic.gdx.graphics.glutils.ShapeRenderer

;
public class LibGDXApplicationListener implements ApplicationListener {

	
	private boolean render = true; //Whether the simulation is rendered
	private boolean followLifeForm = false;
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private Container holdingContainer;
	private Renderer renderer;
	private World world;
	private GUI gui;
	private OrthographicCamera camera;
	
	private int canvasWidth;
	private int canvasHeight;
	
	//For scrolling around the map with WASD
	private float scrollOffsetX = 0;
	private float scrollOffsetY = 0;
	
	public LibGDXApplicationListener(GUI gui,Container holdingContainer, World world,  int canvasWidth, int canvasHeight) {
		super();
		this.holdingContainer = holdingContainer;
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.world = world;
		this.gui = gui;
	}
	
	@Override
	public void create() {
		createNewCamera();
		camera.position.set(Gdx.graphics.getWidth() >> 1, Gdx.graphics.getHeight() >> 1,0);
		
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		renderer = new Renderer(this, spriteBatch, shapeRenderer);
		
		spriteBatch.enableBlending();
		Gdx.input.setInputProcessor(new LibGDXInputListener(this));
		
	}

	private void createNewCamera() {
		if (camera == null)
			camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

    @Override
	public void render () {
		//Update GUI
		gui.updateComponents(world);
		
		
		if (render) {
			try {
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			} catch (Exception ex) {
				return;
			}
			
			int sX = (int)( camera.position.x) * 2;
			int sY = (int)(camera.position.y) * 2;
			
			//Pan camera
			if (Gdx.input.isButtonPressed(Buttons.RIGHT)) 
				scrollPosition(Gdx.input.getDeltaX(),Gdx.input.getDeltaY());

			if (followLifeForm)
				centreMapOn(world.getCurrentLifeForm());

        camera.update();
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);

        spriteBatch.begin();
        renderer.renderWorld(world, world.getCurrentLifeForm(), (int)scrollOffsetX, (int)scrollOffsetY);
        spriteBatch.end();
    }

}

    private void scrollPosition(float x, float y) {
        createNewCamera();
		camera.position.set(new Vector3(camera.position.x - x,camera.position.y + y,camera.position.z));
		scrollOffsetX += x;
		scrollOffsetY -= y;
	}
	
	private void setPosition(int x, int y) {
		createNewCamera();
		scrollOffsetX = -x;
		scrollOffsetY = -y;
	}
	
	public GUI getGui() {
		return gui;
	}


	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}
	
	/**
	 * Whether the user's cursor is inside the container that holds this
	 * @return
	 */
	public boolean hasFocus() {
		//Note we have to use Swing's mouse coordinates rather than GDX as GDX mouse coordinates only cover the canvas dimensions, not the frame's dimensions
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		
		//We need the global position of the container rather than the local position since the above uses global coordiantes
		Point containerPosition = holdingContainer.getLocationOnScreen();
		
		return Geometry.isPointInsideRectangle(mousePosition.x, mousePosition.y, containerPosition.x, containerPosition.y, canvasWidth,canvasHeight);
	}
	
	//Centres the map on the life form's position
	public void centreMapOn(LifeForm lifeForm) {
        if (lifeForm == null)
            return;
		centreMapOn(lifeForm.getXPosition() - (Gdx.graphics.getWidth() >> 1), lifeForm.getYPosition() - (Gdx.graphics.getHeight() >> 1));
	}
	
	//Centres map via real coords
	public void centreMapOn(double x, double y) {
		setPosition((int)x,(int)y);
	}
	
	public void toggleRender() {
		render = !render;
	}

	public void toggleFollowLifeForm() {
		followLifeForm = !followLifeForm;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
}
