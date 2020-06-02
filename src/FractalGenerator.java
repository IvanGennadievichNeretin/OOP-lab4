import java.awt.geom.Rectangle2D;

/**
 * Этот класс предоставляет общий интерфейс и операции для генераторов фракталов, 
 * которые можно просматривать в Fractal Explorer.
 */
public abstract class FractalGenerator {

    /**
     * Эта статическая вспомогательная функция принимает целочисленную координату 
	 * и преобразует ее в значение двойной точности, соответствующее определенному диапазону. 
	 * Он используется для преобразования координат пикселей в значения 
	 * двойной точности для вычисления фракталов и т. Д.
     *
     * @param rangeMin минимальное значение диапазона с плавающей запятой
     * @param rangeMax максимальное значение диапазона с плавающей запятой
     *
     * @param size размер измерения, из которого берется пиксельная координата.
     *        Например, это может быть ширина изображения или высота изображения.
     *
     * @param coord координата, чтобы вычислить значение двойной точности
     *        Координата должна находиться в диапазоне [0, размер].
     */
    public static double getCoord(double rangeMin, double rangeMax,
        int size, int coord) {

        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }


    /**
     * Устанавливает указанный прямоугольник, чтобы содержать начальный диапазон, подходящий
     * для генерируемого фрактала.
     */
    public static void getInitialRange(Rectangle2D.Double range) {};
    public static int numIterations(double x, double y) { return 0; };

    /**
     * Обновляет текущий диапазон с центром в указанных координатах, 
     * а также для увеличения или уменьшения с помощью указанного коэффициента масштабирования.
     */
    public static void recenterAndZoomRange(Rectangle2D.Double range,
        double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }

    /**
     * Созданный класс для фрактала Мандельброта
     * **/
    public static class Mandelbrot extends FractalGenerator {
        public static final int MAX_ITERATIONS = 2000;
        public static void getInitialRange(Rectangle2D.Double rect){
            rect.x = -2;
            rect.y = -1.5;
            rect.height = 3;
            rect.width = 3;
        }

        //Возвращает количество итераций для точки (x, y), при которых очевидно, что точка не принадлежит
        // набору. Возвращает -1, если точка находится во множестве Мандельброта
        public static int numIterations(double x, double y){
            int iterations = 0;
            Complex c = new Complex(x, y);
            Complex z = new Complex(0, 0);
            while (iterations < MAX_ITERATIONS){
                iterations++;
                z = z.step2().sum(c);
                if (z.isMoreThan(2)){
                    return iterations;
                }
            }
            return -1;
        }
    }
}
