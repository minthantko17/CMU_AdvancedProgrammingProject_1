package se233.advprogrammingproject1.cropping;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import se233.advprogrammingproject1.controllers.CropController;
import se233.advprogrammingproject1.functions.CropFunctions;

import static se233.advprogrammingproject1.functions.CropFunctions.updateHandles;

public class RectangleBoxGroup extends Group {
    private final Circle topLeftHandle, topRightHandle, bottomLeftHandle, bottomRightHandle;
    private final Circle leftHandle, rightHandle, topHandle, bottomHandle;
    private final Rectangle rectangle = new Rectangle();
    private ImageView imageView=new ImageView();
    private double  initX, initY, initWidth, initHeight;
    private boolean resizingTop, resizingBottom, resizingLeft, resizingRight, isMoving;
    private static boolean canLarge;

    private static double tempWidth=1;
    private static double tempHeight=1;
    private static double tempWHelper=1;
    private static double tempHHelper=1;

    public RectangleBoxGroup(ImageView imageView, double x, double y, double width, double height) {
        this.imageView=imageView;
        canLarge = isWithinImageView(rectangle);

        //rectangle box creation
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(3);
        canLarge = isWithinImageView(rectangle);

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

//        if (resizingTop){
//            rectangle.setY(initY+deltaY);
//            rectangle.setHeight(initHeight-deltaY);
//
//            if(CropController.isAspectRatio){
//                if(canResizeLarger) {
////                    System.out.println("in if condition");
//                    rectangle.setWidth(rectangle.getHeight() * (CropController.ratioWidth / CropController.ratioHeight));
//                    updateHandles(rectangle);
//                }
//                if(rectangle.getWidth() > imageView.getFitWidth()){
////                    System.out.println("in if condition 2");
//                    rectangle.setWidth(imageView.getFitWidth());
//                    rectangle.setHeight(rectangle.getWidth() * (CropController.ratioHeight / CropController.ratioWidth));
//                    canResizeLarger=false;
//                }else{
////                    System.out.println("in else condition");
//                    canResizeLarger=canBeLarger(rectangle);
//                }
//            }
////            // DON'T DELETE --- works but not too specific
////            if(CropController.isAspectRatio){
//////                System.out.println("in if condition");
////                ensureRectangleWithinBounds(rectangle,imageView);
////                updateHandles(rectangle);
////                if(rectangle.getWidth()<imageView.getFitWidth()){
////                    rectangle.setWidth(rectangle.getHeight()*(CropController.ratioWidth/CropController.ratioHeight));
////                }else{
////                    rectangle.setWidth(imageView.getFitWidth());
////                }
////            }
//        }
        if (resizingTop) {
            System.out.println("resizing top");
            rectangle.setY(initY + deltaY);
            if (CropController.isAspectRatio) {
                canLarge=isWithinImageView(rectangle);
                tempHeight = initHeight-deltaY;
                tempWidth = tempHeight * (CropController.ratioWidth / CropController.ratioHeight);
                if(canLarge && tempWidth<imageView.getFitWidth() && tempHeight<imageView.getFitHeight()){
                    System.out.println("in if");
                    rectangle.setHeight(tempHeight);
                    rectangle.setWidth(tempWidth);
                }else{
                    System.out.println("in else");
                    CropFunctions.fitRecToSmallerImageViewSide(imageView, rectangle);
                    canLarge=false;
                }
            }else{
                rectangle.setHeight(initHeight - deltaY);
            }
//            updateHandles(this, rectangle);
        }

        if (resizingBottom){
            System.out.println("resizing bottom");
            if (CropController.isAspectRatio) {
                canLarge=isWithinImageView(rectangle);
                tempHeight = initHeight+deltaY;
                tempWidth = tempHeight * (CropController.ratioWidth / CropController.ratioHeight);
                if(canLarge && tempWidth<imageView.getFitWidth() && tempHeight<imageView.getFitHeight()){
                    System.out.println("in if");
                    rectangle.setHeight(tempHeight);
                    rectangle.setWidth(tempWidth);
                }else{
                    System.out.println("in else");
                    //need to change following code, maybe in if else cond: for ratioHeight>ratioWidth
                    CropFunctions.fitRecToSmallerImageViewSide(imageView, rectangle);
                    canLarge=false;
                }
            }else {
                rectangle.setHeight(initHeight + deltaY);
            }
        }

        if (resizingLeft){
            System.out.println("resizing left");
            rectangle.setX(initX+deltaX);
            if (CropController.isAspectRatio) {
                canLarge=isWithinImageView(rectangle);
                tempWidth=initWidth-deltaX;
                tempHeight = tempWidth * (CropController.ratioHeight / CropController.ratioWidth);
                if(canLarge && tempHeight<imageView.getFitHeight() && tempWidth<imageView.getFitWidth()){
                    System.out.println("in if");
                    rectangle.setWidth(tempWidth);
                    rectangle.setHeight(tempHeight);
                }else{
                    System.out.println("in else");
                    CropFunctions.fitRecToSmallerImageViewSide(imageView, rectangle);
                    canLarge=false;
                }
            }else {
                rectangle.setWidth(initWidth - deltaX);
            }
        }
        if (resizingRight){
            System.out.println("resizing right");
            if (CropController.isAspectRatio) {
                canLarge=isWithinImageView(rectangle);
                tempWidth=initWidth+deltaX;
                tempHeight = tempWidth * (CropController.ratioHeight / CropController.ratioWidth);
                if(canLarge && tempHeight<imageView.getFitHeight() && tempWidth<imageView.getFitWidth()){
                    System.out.println("in if");
                    rectangle.setWidth(tempWidth);
                    rectangle.setHeight(tempHeight);
                }else{
                    System.out.println("in else");
                    CropFunctions.fitRecToSmallerImageViewSide(imageView, rectangle);
                    canLarge=false;
                }
            }else {
                rectangle.setWidth(initWidth + deltaX);
            }
        }
        ensureRectangleWithinBounds(rectangle, imageView);
        updateHandles(this, rectangle);
    }

    private boolean isWithinImageView(Rectangle rectangle){
        if(imageView.getLayoutX() <= rectangle.getX()
                && imageView.getLayoutY() <= rectangle.getY()
                && (imageView.getLayoutX()+imageView.getFitWidth()) > rectangle.getWidth()
                && (imageView.getLayoutY()+imageView.getFitHeight()) > rectangle.getHeight()
        ){
            return true;
        }
        return false;
    }

    //Resize finish conditions
    private void finishResizing(Circle handlerCircle){
        resizingTop=false;
        resizingBottom=false;
        resizingLeft=false;
        resizingRight=false;
        handlerCircle.setFill(Color.SKYBLUE);
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
        updateHandles(this, rect );
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
                updateHandles(this,rectangle);
                initX=e.getX();
                initY=e.getY();
            }
        });
        rectangle.setOnMouseReleased(e->isMoving=false);

    }



    //Getter Setter
    public Rectangle getRectangle(){return rectangle;}

    public Circle getTopLeftHandle() {return topLeftHandle;}

    public Circle getTopRightHandle() {return topRightHandle;}

    public Circle getBottomLeftHandle() {return bottomLeftHandle;}

    public Circle getBottomRightHandle() {return bottomRightHandle;}

    public Circle getLeftHandle() {return leftHandle;}

    public Circle getRightHandle() {return rightHandle;}

    public Circle getTopHandle() {return topHandle;}

    public Circle getBottomHandle() {return bottomHandle;}
}


