package laberinto;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private final Image image = new Image("file:tileLab.png");
    private LaberintoGen m;
    private GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int size = 10;
        m = new LaberintoGen(size, size);
        primaryStage.setTitle("Laberinto");
        Group root = new Group();
        Canvas canvas = new Canvas(32 * size, 32 * size);
        gc = canvas.getGraphicsContext2D();

        dibujarMapa();

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Task taskResolver = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                resolver(0, 0);
                return true;
            }
        };
        Thread threadResolver = new Thread(taskResolver);
        threadResolver.start();
    }

    /**
     * Función recursiva para resolver el laberinto
     *
     * @param x posicion actual en x a resolver
     * @param y posicion actual en y a resolver
     * @return true si la celda es parte de la solucion, sino false
     */
    private boolean resolver(int x, int y) {
        
        m.visitar(x, y);
        dibujarPunto(x, y, 1);
        
        /*Caso Base:
        *
        */
        if(x == m.getW()-1  &&  y == m.getH()-1){
            dibujarPunto(x, y, 2);
            return true;
        }
        /*fin Caso Base*/

        /*Si hay espacio vacio hacia arriba llamado recursivo a resolver()*/
        if((m.getData(x, y) & 1) == 1  && !m.esVisitado(x, y-1))
            if(resolver (x, y-1)){
                dibujarPunto(x, y, 2);
                return true;
            }
        /**/
        /*Si hay espacio vacio hacia abajo llamado recursivo a resolver()*/
        if((m.getData(x, y) & 2) == 2 && !m.esVisitado(x, y+1))
            if(resolver (x, y+1)){
                dibujarPunto(x, y, 2);
                return true;
            }
        /**/
        /*Si hay espacio vacio hacia la derecha llamado recursivo a resolver()*/
        if((m.getData(x, y) & 4) == 4 && !m.esVisitado(x+1, y))
            if(resolver (x+1, y)){
                dibujarPunto(x, y, 2);
                return true;
            }
        /**/
        /*Si hay espacio vacio hacia la izquierda llamado recursivo a resolver()*/
        if((m.getData(x, y) & 8) == 8 && !m.esVisitado(x-1, y))
            if(resolver (x-1, y)){
                dibujarPunto(x, y, 2);
                return true;
            }
        /**/
        
        dibujarPunto(x, y, 0);
        return false;
    }

    /**
     * Dibuja el mapa en el canvas
     */
    private void dibujarMapa() {
        for (int i = 0; i < m.getW(); i++)
            for (int j = 0; j < m.getH(); j++)
                gc.drawImage(image, 0, 32 * m.getData(i, j), 32, 32, i * 32, j * 32, 32, 32);
    }

    /**
     * Dibuja un punto amarillo, verde o rojo en x,y
     *
     * @param x     pos y
     * @param y     pos x
     * @param color 0 es rojo, 1 es amarillo y 2 es verde
     */
    private void dibujarPunto(int x, int y, int color) {
        int radio = 10;

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (color) {
            case 0:
                gc.setFill(Color.RED);
                break;
            case 1:
                gc.setFill(Color.YELLOW);
                break;
            case 2:
                gc.setFill(Color.GREEN);
                break;
        }
        Platform.runLater(() ->
                gc.fillOval(x * 32 + 16 - radio, y * 32 + 16 - radio, 2 * radio, 2 * radio)
        );
    }
}