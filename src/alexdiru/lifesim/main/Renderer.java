package alexdiru.lifesim.main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
public class Renderer implements Runnable{
	
	public static int TILE_SIZE = 32;
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private LibGDXApplicationListener applicationListener;
	
	private Texture obstacleTexture; //Image used for the obstacles
	private Texture grassTexture; //Image used for the grass 
	private Sprite lifeFormTexture; //Images used for the life forms
	private Texture foodTexture; //Image used for the food
	private Texture poisonTexture; //Image used for the poison
	private Texture waterTexture; //Image used for the water
	private Texture lilypadTexture; //Image used for the lilypads
	
	//Creates the renderer - loads the images used to render and passes the sprite batch 
	public Renderer(LibGDXApplicationListener al, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		this.spriteBatch = spriteBatch;
		this.shapeRenderer = shapeRenderer;
		applicationListener = al;
		
		//Setup the libGDX file access method
		Gdx.files = new com.badlogic.gdx.backends.lwjgl.LwjglFiles();
				
		//Load all of the images the renderer requires
		obstacleTexture = new Texture(Gdx.files.internal("assets/obstacle.png"));
		lifeFormTexture = new Sprite(new Texture(Gdx.files.internal("assets/lifeform.png")));
		grassTexture = new Texture(Gdx.files.internal("assets/grass.png"));
		foodTexture = new Texture(Gdx.files.internal("assets/food.png"));
		poisonTexture = new Texture(Gdx.files.internal("assets/poison.png"));
		waterTexture = new Texture(Gdx.files.internal("assets/water.png"));
		lilypadTexture = new Texture(Gdx.files.internal("assets/lilypad.png"));
		
		//Make the grass texture wrap if it is stretched when rendered
		grassTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	//Renders all of the world onto the sprite batch
	public void renderWorld(World world, LifeForm lifeForm, int scrollOffsetX, int scrollOffsetY) {
		spriteBatch.draw(grassTexture, scrollOffsetX,  scrollOffsetY, world.getXSize()*Renderer.TILE_SIZE,world.getYSize()*Renderer.TILE_SIZE,0,0,world.getXSize(),world.getYSize());
		
		//Note to self: think about where you render new things - things are rendered in layers
		synchronized (world) {
			renderObstacles(world, scrollOffsetX, scrollOffsetY);
			renderWater(world, scrollOffsetX, scrollOffsetY);
			//renderDeadLifeForms(world, scrollOffsetX, scrollOffsetY);
			renderItems(world, scrollOffsetX, scrollOffsetY);
            lifeForm.renderFacing(this, spriteBatch, shapeRenderer, lifeFormTexture, scrollOffsetX, scrollOffsetY);
			//
			// renderCurrentLifeForms(world, scrollOffsetX, scrollOffsetY);
		}
	}
	
	//Renders the item
	public void renderItem(Item item, int scrollOffsetX, int scrollOffsetY) {
		if (item instanceof Food)
			item.render(spriteBatch, foodTexture, scrollOffsetX, scrollOffsetY);
		else if (item instanceof Poison)
			item.render(spriteBatch, poisonTexture, scrollOffsetX, scrollOffsetY);
    }
	
	private void renderObstacles(World world, int scrollOffsetX, int scrollOffsetY) {
		for (Obstacle obstacle : world.getObstacles())
			obstacle.render(spriteBatch, obstacleTexture, scrollOffsetX, scrollOffsetY);
	}
	
	private void renderWater(World world, int scrollOffsetX, int scrollOffsetY) {
		for (Water water: world.getWaters()) 
			water.render(spriteBatch, waterTexture, scrollOffsetX, scrollOffsetY);
	}
	
	private void renderItems(World world, int scrollOffsetX, int scrollOffsetY) {
		synchronized (world.getItems()) {
		for (Item item : world.getItems())
			if (item.shouldRender())
				renderItem(item, scrollOffsetX, scrollOffsetY);
		}
	}
	
	private void renderCurrentLifeForms(World world, int scrollOffsetX, int scrollOffsetY) {
		if (world.getCurrentLifeForm() == null)
			return;
	
		synchronized (world.getCurrentLifeForm()) {
			world.getCurrentLifeForm().renderFacing(this, spriteBatch, shapeRenderer, lifeFormTexture, scrollOffsetX, scrollOffsetY);
		}
	}

	@Override
	public void run() {
		while (true)
			applicationListener.render();
	}
}
