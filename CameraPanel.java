import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class CameraPanel extends JPanel implements Runnable, ActionListener {
	BufferedImage image;
	VideoCapture capture;
	JButton screenshot;
	CascadeClassifier faceDetector;
    MatOfRect faceDetections;

	CameraPanel()
	{
		screenshot = new JButton("screenshot");
		screenshot.addActionListener(this);
		add(screenshot);	      
		faceDetector = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
		faceDetections = new MatOfRect();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );  
	    capture = new VideoCapture(0);	
	    Mat web_image = new Mat();
	    if(capture.isOpened())
	    {
	    	while(true)
	    	{
	    		capture.read(web_image);
	    	 // Core.flip(web_image, web_image, 1);
	    	if(!web_image.empty())
	    		MatToBufferedImage(web_image);
	        faceDetector.detectMultiScale(web_image, faceDetections);
	    	repaint();
	    	}
	    }
	}
	
	public void MatToBufferedImage(Mat a)
	{
		int width=a.width();
		int height=a.height();
		int channels = a.channels();
		byte[] source =  new byte[width*height*channels];
		a.get(0, 0, source);
		image = new BufferedImage(width, height,BufferedImage.TYPE_3BYTE_BGR);
		final byte[] target = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(source, 0, target, 0, source.length);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(image==null)
			return;
		g.drawImage(image,10,40,image.getWidth()*2,image.getHeight()*2,null);
		System.out.println(image.getWidth());
		System.out.println(image.getHeight());
		g.setColor(Color.blue);
		for (Rect rect : faceDetections.toArray()) {
           g.drawRect((rect.x)*2, (rect.y)*2, (rect.width)*2,( rect.height)*2);
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		File screenshot= new File("screenshot"+".png");
		int i=0;
		if(screenshot.exists())
		{
			screenshot = new File("screenshot" + i + ".png");
			i++;
		}
		try {
			ImageIO.write(image, "png", screenshot);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void switchCamera(int num) {
		capture = new VideoCapture(num);
	}

}
