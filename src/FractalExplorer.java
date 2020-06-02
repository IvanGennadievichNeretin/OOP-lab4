import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.color.ICC_ProfileRGB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int screenSize;
    private JImageDisplay display;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;
    FractalExplorer(int size){
        screenSize = size;
        display = new JImageDisplay(screenSize, screenSize);
        range = new Rectangle2D.Double();
        FractalGenerator.Mandelbrot.getInitialRange(range);
    }
    public void createAndShowGUI(){
        //кнопка сброса
        Button reset = new Button("Reset");
        reset.setSize(screenSize, 50);
        reset.addActionListener(new JButtonClick());
        reset.setVisible(true);

        //панель вывода компонентов интерфейса
        BorderLayout layout = new BorderLayout(screenSize, screenSize);
        display.clearImage();

        //создание и настройка окна
        JFrame frame = new JFrame();
        frame.add(reset, BorderLayout.SOUTH);
        frame.add(display, BorderLayout.CENTER);
        //frame.setLayout(layout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(screenSize, screenSize));
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addMouseListener(new JMouseAdapter());
        this.drawFractal();
    }

    private void drawFractal(){
        int i, j;
        double xCoord, yCoord;
        int iterations;
        float hue;
        int rgbColor;
        for (i = 0; i < display.getWidth(); i++){
            for (j = 0; j < display.getHeight(); j++){
                xCoord = FractalGenerator.Mandelbrot.getCoord(range.x, range.x + range.width, screenSize, i);
                yCoord = FractalGenerator.Mandelbrot.getCoord(range.y, range.y + range.height, screenSize, j);
                iterations = FractalGenerator.Mandelbrot.numIterations(xCoord, yCoord);
                if (iterations == -1){
                    rgbColor = 0;
                }
                else{
                    hue = 0.7f + (float) iterations / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                display.drawPixel(i, j, rgbColor);
                display.repaint();
            }
        }

    }

    private class JMouseAdapter extends java.awt.event.MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            display.drawPixel(e.getX(), e.getY(), ICC_ProfileRGB.icSigGreenColorantTag);
            double xCoord = FractalGenerator.Mandelbrot.getCoord(range.x, range.x + range.width, screenSize, e.getX());;
            double yCoord = FractalGenerator.Mandelbrot.getCoord(range.y, range.y + range.height, screenSize, e.getY());
            FractalGenerator.Mandelbrot.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            display.repaint();
            drawFractal();
        }
    }

    private class JButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FractalGenerator.Mandelbrot.getInitialRange(range);
            drawFractal();
        }
    }
}
