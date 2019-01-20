/**
 * Filename: Recycling.java
 * Date: January 19th, 2019
 * 
 * Code was heavily based off sarxo's webcam QR Code Reader Example.
 * https://github.com/sarxos/webcam-capture/tree/master/webcam-capture-examples/webcam-capture-qrcode
 * 
 * Uses the Webcam Capture API (https://github.com/sarxos/webcam-capture) and 
 * ZXing QR/Barcode Decoder (https://github.com/zxing/zxing). Both are required
 * to execute this program.
 * 
 */

import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class Recycling extends JFrame implements Runnable, ThreadFactory {

	private static final long serialVersionUID = 6441489157408381878L;

	private static final String RECYCLE_STR = "r"; // What string to search for
	private static final String NOT_RECYCLE = "Unable to be Recycled";
	private static final String RECYCLE = "Able to be Recycled";
	private static final String NO_BARCODE = "No barcode found";
	private static final int CAM_NUM = 0; // 0 is built-in webcam, 1 is external webcam
	
	private Executor executor = Executors.newSingleThreadExecutor(this); // Multi-threading!
	
	private Webcam webcam = null; // Reference to the webcam obj
	private WebcamPanel panel = null; // Reference to the GUI webcam panel
	private JTextArea textarea = null; // Reference to the textbox GUI panel
	
	/**
	 * Constructor for Recycling() Instance
	 */
	public Recycling() {
		super();
		
		/* -- Setting up the GUI -- */
		setLayout(new FlowLayout());
		setTitle("Checking Recycle-ability");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = WebcamResolution.QVGA.getSize(); // size of the camera output

		try
		{
			webcam = Webcam.getWebcams().get(CAM_NUM);
		}
		catch(IndexOutOfBoundsException e) // No camera attached!
		{
			System.err.printf("No Camera Attached :( \n");
			e.printStackTrace();
		}
		
		/* -- Setting up settings for the GUI -- */
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);

		textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setPreferredSize(size);
		textarea.setFont(textarea.getFont().deriveFont(24f));
		
		add(panel);
		add(textarea);

		pack();
		setVisible(true);

		executor.execute(this); // Run it in a thread!
	}

	/**
	 * Executes the loop to analyze the video stream to scan for QR Codes
	 */
	@Override
	public void run() {

		do {
			/* -- Give the CPU some time to do other stuff -- */
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Result output = null; // The output given by the QR/Barcode decoder
			BufferedImage image = null;  // The image to be decoded

			if (webcam.isOpen()) {

				/* -- Is there an image? No? -- */
				if ((image = webcam.getImage()) == null) {
					continue;
				}

				/* -- Used to search for a QR/Barcode found in the video stream -- */
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try 
				{
					output = new MultiFormatReader().decode(bitmap); // attempt to decode QR/Barcode
				} catch (NotFoundException e)
				{ 
					textarea.setText(NO_BARCODE); // Clear text 
					continue; // No QR/Barcode found, continue loop
				}
			}
			
			/* -- If text was found, decode, and sleep to prevent spam -- */
			if (output != null) {
				textarea.setText("\n\n" + calculateRecycle(output.getText()));
				try 
				{
					Thread.sleep(2500);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}


		} while (true);
	}

	/**
	 * Makes a new Thread.
	 * 
	 * @param r An object designed to be run in a thread
	 */
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, r.toString());
		t.setDaemon(true);
		return t;
	}

	/**
	 * Calculates the String to print out
	 * 
	 * @param input The input from the QR/Barcode reader
	 * 
	 * @return What string to print out
	 */
	String calculateRecycle(String input)
	{
		if(input.toLowerCase().trim().equals(RECYCLE_STR))
		{
			return RECYCLE;
		}
		else
		{
			return NOT_RECYCLE;
		}
	}
	
	/**
	 * Main for Recycling
	 * 
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		new Recycling();
	}
}
