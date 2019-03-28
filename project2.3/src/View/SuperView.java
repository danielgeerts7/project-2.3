package View;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Client.ClientSocket;
import Main.Config;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;

public abstract class SuperView extends Pane {
	
	private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;
    private Label label_fps = null;
    
    public ClientSocket client = null;

	public SuperView() {
		this.setBackground(new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		
		AnimationTimer animator = new AnimationTimer()
	    {
	        @Override
	        public void handle(long now) 
	        {
	            update();
	            showFPS(now);
	        }
	    };
	    animator.start();
	    
	    label_fps = new Label();
        label_fps.setText(String.format("FPS null"));
	    label_fps.setTranslateX(Config.WIDTH*0.9);
	    super.getChildren().add(label_fps);
	    
	    this.setUpClient();
	}
	
	
	private void showFPS(long now) {
		long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }
        if (arrayFilled) {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            if (label_fps != null) {
	            label_fps.setText(String.format("FPS %.1f", frameRate));
            }
        }
	}
	
	private void setUpClient() {
		try {
			client = new ClientSocket(InetAddress.getLocalHost(), 7789);
			System.out.println("Connected to Server: " + client.socket.getInetAddress());
			//client.sendInputMessage();		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * This method is called every available frame
	 */
	protected abstract void update();
}
