package se233.advprogrammingproject1.cropping;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

public class RectangleBoxGroup extends Group {
    private final Circle topLeftHandle, topRightHandle, bottomLeftHandle, bottomRightHandle;
    private final Circle leftHandle, rightHandle, topHandle, bottomHandle;
    private final Rectangle rectangle = new Rectangle();
    private ImageView imageView=new ImageView();
    private double  initX, initY, initWidth, initHeight;
    private boolean resizingTop, resizingBottom, resizingLeft, resizingRight, isMoving;

    public RectangleBoxGroup(ImageView imageView, double x, double y, double width, double height) {
        this.imageView=imageView;

        //rectangle box creation
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(3);

        //create Resize-handle circles
        double handleSize=10;
        topLeftHandle=createHandle(rectangle.getX(), rectangle.getY(), handleSize);
        topRightHandle=createHandle(rectangle.getX() + rectangle.getWidth(), rectangle.getY(), handleSize);
        bottomLeftHandle=createHandle(rectangle.getX(), rectangle.getY() + rectangle.getHeight(), handleSize);
        bottomRightHandle=createHandle(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight(), handleSize);
        topHandle=createHandle(rectangle.getX()+rectangle.getWidth()/2, rectangle.getY(), handleSize);
        bottomHandle=createHandle(rectangle.getX()+rectangle.getWidth()/2, rectangle.getY()+ rectangle.getHeight(), handleSize);
        leftHandle=createHandle(rectangle.getX(), rectangle.getY()+ rectangle.getHeight()/2, handleSize);
        rightHandle=createHandle(rectangle.getX()+rectangle.getWidth(), rectangle.getY()+ rectangle.getHeight()/2, handleSize);

        setResizeHandler(rectangle);
        setMoveHandler(rectangle);

        this.getChildren().addAll(rectangle,topLeftHandle, topRightHandle, bottomLeftHandle, bottomRightHandle, topHandle, bottomHandle, leftHandle, rightHandle);
    }

    //create resize-handle
    private Circle createHandle(double x, double y, double handleSize) {
        double radius=handleSize/2;
        Circle handle=new Circle(x, y, radius, Color.SKYBLUE);
        handle.setStroke(Color.BLACK);
        handle.setStrokeWidth(1);
        return handle;
    }

    //events for resize-handles
    private void setResizeHandler(Rectangle rectangle){
        topLeftHandle.setOnMousePressed(e->startResizing(topLeftHandle, rectangle, true, false, true, false));
        topRightHandle.setOnMousePressed(e->startResizing(topRightHandle, rectangle, true, false, false, true));
        bottomLeftHandle.setOnMousePressed(e->startResizing(bottomLeftHandle, rectangle, false, true, true, false));
        bottomRightHandle.setOnMousePressed(e->startResizing(bottomRightHandle, rectangle, false, true, false, true));
        topHandle.setOnMousePressed(e->startResizing(topHandle, rectangle, true, false, false, false));
        bottomHandle.setOnMousePressed(e->startResizing(bottomHandle, rectangle, false, true, false, false));
        leftHandle.setOnMousePressed(e->startResizing(leftHandle, rectangle, false, false, true, false));
        rightHandle.setOnMousePressed(e->startResizing(rightHandle, rectangle, false, false, false, true));

        topLeftHandle.setOnMouseDragged(e->resize(e, rectangle));
        topRightHandle.setOnMouseDragged(e->resize(e, rectangle));
        bottomLeftHandle.setOnMouseDragged(e->resize(e, rectangle));
        bottomRightHandle.setOnMouseDragged(e->resize(e, rectangle));
        topHandle.setOnMouseDragged(e->resize(e, rectangle));
        bottomHandle.setOnMouseDragged(e->resize(e, rectangle));
        leftHandle.setOnMouseDragged(e->resize(e, rectangle));
        rightHandle.setOnMouseDragged(e->resize(e, rectangle));

        topLeftHandle.setOnMouseReleased(e->finishResizing(topLeftHandle));
        topRightHandle.setOnMouseReleased(e->finishResizing(topRightHandle));
        bottomLeftHandle.setOnMouseReleased(e->finishResizing(bottomLeftHandle));
        bottomRightHandle.setOnMouseReleased(e->finishResizing(bottomRightHandle));
        topHandle.setOnMouseReleased(e->finishResizing(topHandle));
        bottomHandle.setOnMouseReleased(e->finishResizing(bottomHandle));
        leftHandle.setOnMouseReleased(e->finishResizing(leftHandle));
        rightHandle.setOnMouseReleased(e->finishResizing(rightHandle));
    }

    //Helper Methods for resizing
    private void startResizing(Circle handlerCircle, Rectangle rectangle, Boolean top, Boolean Bottom, Boolean left, Boolean right){
        initX=handlerCircle.getCenterX();
        initY=handlerCircle.getCenterY();
        initWidth=rectangle.getWidth();
        initHeight=rectangle.getHeight();
        resizingTop=top;
        resizingBottom=Bottom;
        resizingLeft=left;
        resizingRight=right;
        isMoving =false;
        handlerCircle.setFill(Color.INDIANRED);
    }

    //Resize rectangle from handler-circles
    private void resize(MouseEvent event, Rectangle rectangle){
        double deltaX;
        double deltaY;
        double imageViewMinX = imageView.getLayoutX();
        double imageViewMinY = imageView.getLayoutY();
        double imageViewMaxX = imageViewMinX +imageView.getFitWidth();
        double imageViewMaxY= imageViewMinY +imageView.getFitHeight();

        if(event.getX()< imageViewMinX){
            deltaX= imageViewMinX -initX;
        }else if(event.getX()> imageViewMaxX){
            deltaX=(imageViewMaxX) - initX;
        }else{
            deltaX=event.getX()-initX;
        }

        if(event.getY()<imageViewMinY){
            deltaY=imageViewMinY-initY;
        }else if(event.getY()>imageViewMaxY){
            deltaY=(imageViewMaxY) - initY;
        }else{
            deltaY=event.getY()-initY;
        }


        if (resizingTop){
            System.out.println("resizing top");
            rectangle.setY(initY+deltaY);
            rectangle.setHeight(initHeight-deltaY);
//            if(CropController.isAspectRatio){
//                System.out.println("in if condition");
//                ensureRectangleWithinBounds(rectangle,imageView);
//                updateHandles(rectangle);
//                if(rectangle.getWidth()<imageView.getFitWidth()){
//                    rectangle.setWidth(rectangle.getHeight()*(CropController.ratioWidth/CropController.ratioHeight));
//                }else{
//                    rectangle.setWidth(imageView.getFitWidth());
//                }
//            }
        }
        if (resizingBottom){
                rectangle.setHeight(initHeight + deltaY);
        }
        if (resizingLeft){
            rectangle.setX(initX+deltaX);
            rectangle.setWidth(initWidth-deltaX);
        }
        if (resizingRight){
            rectangle.setWidth(initWidth+deltaX);
        }

//        if(rectangle.getWidth()<10){ rectangle.setWidth(10); }
//        if(rectangle.getHeight()<10){ rectangle.setHeight(10); }

        ensureRectangleWithinBounds(rectangle, imageView);
        updateHandles(rectangle);
    }

    //Resize finish conditions
    private void finishResizing(Circle handlerCircle){
        resizingTop=false;
        resizingBottom=false;
        resizingLeft=false;
        resizingRight=false;
        handlerCircle.setFill(Color.SKYBLUE);
    }

    //update circle position according to rectangle
    private void updateHandles(Rectangle rectangle){
        topLeftHandle.setCenterX(rectangle.getX());
        topLeftHandle.setCenterY(rectangle.getY());
        topRightHandle.setCenterX(rectangle.getX() + rectangle.getWidth());
        topRightHandle.setCenterY(rectangle.getY());
        bottomLeftHandle.setCenterX(rectangle.getX());
        bottomLeftHandle.setCenterY(rectangle.getY() + rectangle.getHeight());
        bottomRightHandle.setCenterX(rectangle.getX() + rectangle.getWidth());
        bottomRightHandle.setCenterY(rectangle.getY() + rectangle.getHeight());
        topHandle.setCenterX(rectangle.getX() + rectangle.getWidth() / 2);
        topHandle.setCenterY(rectangle.getY());
        bottomHandle.setCenterX(rectangle.getX() + rectangle.getWidth() / 2);
        bottomHandle.setCenterY(rectangle.getY() + rectangle.getHeight());
        leftHandle.setCenterX(rectangle.getX());
        leftHandle.setCenterY(rectangle.getY() + rectangle.getHeight() / 2);
        rightHandle.setCenterX(rectangle.getX() + rectangle.getWidth());
        rightHandle.setCenterY(rectangle.getY() + rectangle.getHeight() / 2);
    }

    // Method to ensure the rectangle stays within image bounds
    private void ensureRectangleWithinBounds(Rectangle rect, ImageView imageView) {
        if (rect.getX() < imageView.getLayoutX()) rect.setX(imageView.getLayoutX());
        if (rect.getY() < imageView.getLayoutY()) rect.setY(imageView.getLayoutY());
        if (rect.getX() + rect.getWidth() > imageView.getLayoutX()+imageView.getFitWidth()) {
            rect.setX(imageView.getLayoutX()+imageView.getFitWidth() - rect.getWidth());
        }
        if (rect.getY() + rect.getHeight() > imageView.getLayoutY()+imageView.getFitHeight()) {
            rect.setY(imageView.getLayoutY()+imageView.getFitHeight() - rect.getHeight());
        }
        updateHandles(rect);
    }

    //events and method for moving rectangle
    private void setMoveHandler(Rectangle rectangle){
        rectangle.setOnMousePressed(e->{
            initX=e.getX();
            initY=e.getY();
            initWidth=rectangle.getWidth();
            initHeight=rectangle.getHeight();
            isMoving =true;
        });
        rectangle.setOnMouseDragged(e->{
            if(isMoving){
                double offsetX=e.getX()-initX;
                double offsetY=e.getY()-initY;
                rectangle.setX(rectangle.getX()+offsetX);
                rectangle.setY(rectangle.getY()+offsetY);
                ensureRectangleWithinBounds(rectangle, imageView);
                updateHandles(rectangle);
                initX=e.getX();
                initY=e.getY();
            }
        });
        rectangle.setOnMouseReleased(e->isMoving=false);

    }

    //Getter Setter
    public Rectangle getRectangle(){return rectangle;}
}

