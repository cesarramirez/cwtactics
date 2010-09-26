package com.client.graphic.tools;

import com.jslix.tools.ImgLibrary;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import org.newdawn.slick.Graphics;

/**
 * MovingMenu.java
 *
 * A remix of PixMenu, helps you to create a menu of selectable image items
 *
 * @author Crecen
 */
public class MovingMenu extends MovingImage{

    public final int REGULAR = 0;
    public final int ROUND_BOX = 1;
    public final int RECTANGLE = 2;
    public final int RND_BORDER = 3;
    public final int BORDER = 4;

    private MenuItem[] allItems;
    private ArrayList<Integer> resetImage;
    public int select;
    private MenuItem item;
    private double sx;
    private double sy;
    private ImgLibrary imgResize;

    public MovingMenu(int locx, int locy, double speed){
        super(locx, locy, speed);
        active = false;
        select = 0;
        resetImage = new ArrayList<Integer>();
        allItems = new MenuItem[0];
        item = new MenuItem(locx, locy, speed);
        imgResize = new ImgLibrary();
        sx = 0;
        sy = 0;
    }

    //Creates a new item to be drawn on the screen
    public void createNewItem(int locx, int locy, double speed){
        item = new MenuItem(locx, locy, speed);
    }

    //Adds an image onto the item
    public void addImagePart(String imgPath, double opacity){
        addImgPart(imgPath, null, opacity);
    }
    public void addImagePart(Image img, double opacity){
    	addImgPart("", img, opacity);
    }

    //Adds/Replaces the item to/on the list of items (do once per menu item)
    public void addMenuItem(int select, boolean selectable){
        addMenuItem(-1, select, selectable);
    }
    public void addMenuItem(int index, int select, boolean selectable){
        addItem(index, REGULAR, select, null, imgRef.getX(allItems.length),
                imgRef.getY(allItems.length), 0, selectable);
    }

    //Adds a Rounded Filled Box to the Menu items list
    public void addRoundBox(int select, Color theColor,
            int sizex, int sizey, int arc, boolean selectable){
        addRoundBox(-1, ROUND_BOX, theColor,
                sizex, sizey, arc, selectable);
    }
    public void addRoundBox(int index, int select, Color theColor,
            int sizex, int sizey, int arc, boolean selectable){
        addItem(index, ROUND_BOX, select, theColor,
                sizex, sizey, arc, selectable);
    }

    //Adds a rectangle Filled Box to the Menu Items list
    public void addBox(int select, Color theColor, int sizex, int sizey,
            boolean selectable){
        addBox(-1, RECTANGLE, theColor, sizex, sizey, selectable);
    }
    public void addBox(int index, int select, Color theColor,
            int sizex, int sizey, boolean selectable){
        addItem(index, RECTANGLE, select, theColor,
                sizex, sizey, 0, selectable);
    }

    //Adds a rectangle border box to the Menu Items list
    public void addBorder(int select, Color theColor, int sizex, int sizey,
            boolean selectable){
        addItem(-1, BORDER, select, theColor, sizex, sizey, 0, selectable);
    }

    //Adds a rounded border box to the menu items list
    public void addRoundBorder(int select, Color theColor, int sizex,
            int sizey, int arc, boolean selectable){
        addItem(-1, RND_BORDER, select, theColor,
                sizex, sizey, arc, selectable);
    }

    public void setItemPosition(int index, int locx, int locy){
        setItemPosition(index, locx, locy, false);
    }
    public void setItemPosition(int index, int locx, int locy,
            boolean addPrev){
        if(index >= 0 && index < allItems.length){
            item = allItems[index];
            if(addPrev)
                item.setFinalPosition((int)item.posx+locx,
                        (int)item.posy+locy);
            else
                item.setFinalPosition(locx, locy);
            allItems[index] = item;
        }
    }

    //Sets whether this item is drawable
    public void setItemDraw(int index, boolean draw){
        if(index >= 0 && index < allItems.length){
            item = allItems[index];
            item.drawthis = draw;
            allItems[index] = item;
        }
    }

    //This changes how selected items react in a menu
    public void setItemChoice(int index, int choice){
        if(index >= 0 && index < allItems.length){
            item = allItems[index];
            item.choice = choice;
            allItems[index] = item;
        }
    }

    public void setItemImage(int index, int itemIndex, String imgPath){
        if(index >= 0 && index < allItems.length){
            if(item.getIndexExists(itemIndex)){
            item = allItems[index];
                imgRef.addImage(item.refPath[itemIndex], imgPath);
                allItems[index] = item;
            }
        }
    }
    
    public boolean mouseSelect(int mx, int my){
    	for(MenuItem itm: allItems){
            if(!itm.drawthis)
                continue;
            if(itm.selectable && select != itm.select){           	
            	if(itm.sizex == 0 && itm.sizey == 0){
            		itm.sizex = imgRef.getX(itm.getPicture(false));
            		itm.sizey = imgRef.getY(itm.getPicture(false));
            	}
            	if(mx > (int)((itm.posx+posx)*scalex) && 
            			mx < (int)((itm.posx+posx+itm.sizex)*scalex)){
                    if(my > (int)((itm.posy+posy)*scaley) && 
                    		my < (int)((itm.posy+posy+itm.sizey)*scaley)){
                    	
                        select = itm.select;
                        return true;
                    }
                }
            }
    	}
    	return false;
    }

    @Override
    public void update(int width, int height, int sysTime, int mouseScroll){
        super.update(width, height, sysTime, mouseScroll);
        for(int i = 0; i < allItems.length; i++)
            allItems[i].renderSpeed();
        if(sx != scalex || sy != scaley){
            for(int i = 0; i < imgRef.length(); i++){
                imgResize.setImageSize((int)((double)imgRef.getX(i)*scalex),
                    (int)((double)imgRef.getY(i)*scaley));
                imgResize.addImage(i, imgRef.getImage(i));
            }
            sx = scalex;
            sy = scaley;
        }
    }

    @Override
    public void render(Graphics g){
        for(MenuItem itm: allItems){
            if(!itm.drawthis)
                continue;

            switch(itm.id){
                case REGULAR:
                	if(itm.opacity >= 0 && itm.opacity <= 1)
                		imgResize.getSlickImage(
                				itm.getPicture(itm.select == select))
                				.setAlpha((float)itm.opacity);
                	else if(opacity < 1)
                		imgResize.getSlickImage(
                				itm.getPicture(itm.select == select))
                				.setAlpha((float)opacity);
                    g.drawImage(imgResize.getSlickImage(itm.getPicture(
                            itm.select == select)),
                            (int)((posx+itm.posx)*scalex), 
                            (int)((posy+itm.posy)*scaley));
                    break;
                default:
                    if(select == itm.select || !itm.selectable){
                        if(itm.theColor != null)
                            g.setColor(imgRef.getColor(itm.theColor));
                        if(itm.select == RECTANGLE)
                            g.fillRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley));
                        else if(itm.select == ROUND_BOX)
                            g.fillRoundRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley), 
                                    (int)(itm.arc*scaley));
                        else if(itm.select == BORDER)
                            g.drawRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley));
                        else if(itm.select == RND_BORDER)
                            g.drawRoundRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley),
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley), 
                                    (int)(itm.arc*scaley));
                    }
            }
        }
    }

    @Override
    public void render(Graphics2D g, Component dthis){
        for(MenuItem itm: allItems){

            if(!itm.drawthis)
                continue;

            switch(itm.id){
                case REGULAR:
                	if(itm.opacity >= 0 && itm.opacity <= 1)
                		g.setComposite(AlphaComposite.getInstance(
                				AlphaComposite.SRC_OVER,
                                (float)itm.opacity));
                	else if(opacity < 1)
                		g.setComposite(AlphaComposite.getInstance(
                				AlphaComposite.SRC_OVER,
                                (float)opacity));
                    g.drawImage(imgResize.getImage(itm.getPicture(
                            itm.select == select)),
                            (int)((posx+itm.posx)*scalex), 
                            (int)((posy+itm.posy)*scaley), dthis);
                    if(opacity < 1 || itm.opacity >= 0 && itm.opacity < 1)
                    	g.setComposite(AlphaComposite.SrcOver);
                    break;
                default:
                    if(select == itm.select || !itm.selectable){
                        if(itm.theColor != null)
                            g.setColor(itm.theColor);
                        if(itm.select == RECTANGLE)
                            g.fillRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley));
                        else if(itm.select == ROUND_BOX)
                            g.fillRoundRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley), 
                                    (int)(itm.arc*scalex), 
                                    (int)(itm.arc*scaley));
                        else if(itm.select == BORDER)
                            g.drawRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley));
                        else if(itm.select == RND_BORDER)
                            g.drawRoundRect((int)((posx+itm.posx)*scalex), 
                                    (int)((posy+itm.posy)*scaley), 
                                    (int)(itm.sizex*scalex), 
                                    (int)(itm.sizey*scaley),
                                    (int)(itm.arc*scalex), 
                                    (int)(itm.arc*scaley));
                    }
            }
        }
    }

    //Cleanly deletes items for the menu List
    public void deleteItems(){
        for(int i = 0; i < allItems.length; i++){
            deleteItem(i);
        }
    }
    public void deleteItems(int type){
        for(int i = 0; i < allItems.length; i++){
            if(item.id == type)
                deleteItem(i);
        }
    }
    public void deleteItem(int index){
        if(index >= 0 && index < allItems.length){
            item = allItems[index];
            for(int i = 0; i < item.refPath.length; i++)
                resetImage.add(item.refPath[i]);

            MenuItem[] temp = allItems;
            allItems = new MenuItem[temp.length-1];
            for(int i = 0; i < allItems.length; i++){
                if(i < index)
                    allItems[i] = temp[i];
                else if(i > index)
                    allItems[i] = temp[i+1];
            }
        }
    }

    //This universally adds an item to the list
    private void addItem(int index, int id, int select, Color theColor,
         int sizex, int sizey, int arc, boolean selectable){
        item.id = id;
        item.select = select;
        item.theColor = theColor;
        item.selectable = selectable;
        item.sizex = sizex;
        item.sizey = sizey;
        item.arc = arc;
        if(index < 0 || index >= allItems.length)
            addItem();
        else
            replaceItem(index);
    }

    private void addItem(){
        //Adds an item onto the item array
        MenuItem[] temp = allItems;
        allItems = new MenuItem[temp.length+1];
        System.arraycopy(temp, 0, allItems, 0, temp.length);
        allItems[allItems.length-1] = item;
    }

    private void replaceItem(int index){
        if(index >= 0 && index < allItems.length){
        	MenuItem[] temp = allItems;
            allItems = new MenuItem[temp.length];
            System.arraycopy(temp, 0, allItems, 0, temp.length);
            allItems[index] = item;
        }
    }
    
    private void addImgPart(String imgPath, Image img, double opacity){
    	if(resetImage.isEmpty()){
            item.addReference(imgRef.length());
            if(img == null)
            	setImage(imgPath);
            else
            	setImage(img);
        }else{
        	if(img == null)
        		imgRef.addImage(resetImage.get(0), imgPath);
            else
            	imgRef.addImage(resetImage.get(0), img);
            item.addReference(resetImage.remove(0));
        }
        item.opacity = opacity;
    }


}