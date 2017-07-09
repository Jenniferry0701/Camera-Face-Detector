import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.core.Core;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class CameraFrame extends JFrame implements ActionListener{
	
	public static void main(String[] args) {
		 CameraFrame a = new CameraFrame();
	}
	CameraPanel cp;
   public CameraFrame(){
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );  
	      VideoCapture list = new VideoCapture(0);
	      cp = new CameraPanel();
	      Thread thread = new Thread(cp);
	      JMenu camera = new JMenu("camera");
	      JMenuBar bar = new JMenuBar();
	      bar.setSize(1000, 2000);
	      bar.add(camera);
	      int i =1;
	      while(list.isOpened())
	      {
	    	  JMenuItem cam = new JMenuItem("camera"+i);
	    	  cam.addActionListener(this);
	    	  camera.add(cam);
	    	  list.release();
	    	  list = new VideoCapture(i);
	    	  i++;
	      }
	      add(cp);
	      thread.start();
	      setJMenuBar(bar);
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      setSize(1400,1400);
	      setVisible(true);
   }

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	JMenuItem source=(JMenuItem)e.getSource();
	int num = Integer.parseInt(source.getText().substring(6))-1;
	cp.switchCamera(num);
} 
}

