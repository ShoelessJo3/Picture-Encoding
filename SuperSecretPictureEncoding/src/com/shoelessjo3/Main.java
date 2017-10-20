package com.shoelessjo3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {


    private static Scanner scanner = new Scanner( System.in );


    public static void main(String[] args) {

        //first get if encoding or decoding
        //if encoding go to encoding state etc
        //if decoding go to decoding state
        //when done with either return to ask state
        while(true)
        {
            System.out.println("Enter 1 to encode a picture and 2 to decode a picture");
          int userIn = scanner.nextInt();
            //System.out.println(userIn);
            //encodePic();]
            String clearPic = scanner.nextLine();
            if(userIn == 1) {
                encodePic();


            }
            if(userIn == 2)
                decodePic();
        }




    }
    public static void encodePic()
    {
        //encoding
        //then get picture to encode it on
        System.out.println("What would you like to encode on the picture?");
        String input = scanner.nextLine();

        System.out.println("What is the path of the picture you would like to encode '" + input + "' on");
        int inputInt[] = new int[input.length()];
        String output = "";
        BufferedImage image = null;

        String pathname = scanner.nextLine();//"C:/Users/gojoe/Documents/sprites/nickcage.png";
        //C:\Users\gojoe\IdeaProjects\SuperSecretPictureEncoding


        try {
            image = ImageIO.read(new File(pathname));
        } catch (IOException e) {
            System.out.print(e);
        }

        int[] pixels = image.getRGB(0,0, image.getWidth(),image.getHeight(), null, 0, image.getWidth());

        BufferedImage imageEncoded = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] encodedPixels =  ((DataBufferInt) imageEncoded.getRaster().getDataBuffer()).getData();

       // System.out.println("test");

        for(int i = 0; i < input.length(); i++)
        {
            inputInt[i] = (int) input.charAt(i);
            //System.out.println(inputInt[i]);
        }


        //message to encode in integer

        //pick random starting point in picture

        //int anchor = (int)(Math.random() * (pixels.length - inputInt.length));

        // System.out.println(anchor);
        //get value of pixel for the message to be encoded on
        //get r, g, and b with shifting encode message by adding message to r  message + 1 to g and then b message + 2
        //then move to the next pixel, so the next message is message (pixelNumber*3) r, message(pixelNumber*3 + 1) g ect
        int loopCounts = (int)Math.ceil((double) input.length()/3.);
        //System.out.println("run " + loopCounts + " times");

        int sectionWidth = encodedPixels.length/loopCounts;

        //System.out.println("putting out " + sectionWidth + " out of " + encodedPixels.length);

        for(int i = 0; i < pixels.length; i++)
        {
            encodedPixels[i] = pixels[i];
        }
        for(int i = 0; i < Math.ceil((double) input.length()/3.); i++)
        {   int location = i*sectionWidth + (int)(Math.random()*sectionWidth);
            int pixelValue = pixels[location];
            //pixelValue = 0xffffff;
/*
            int rValue = (pixelValue & 0xff0000) >> 16;
            int gValue = (pixelValue & 0x00ff00) >> 8 ;
            int bValue = (pixelValue & 0x0000ff);
            System.out.println("rValue = " + rValue + " gValue = " + gValue + " bValue = " + bValue);
            //then add to rValue, gValue and bValue (may be used in future update, currently easier to just set values = to
        //the integer code;*/
            int rValue = 0;
            int gValue = 0;
            int bValue = 0;

            if(input.length() > i*3 + 1)
                gValue = inputInt[i*3 + 1];
            if(input.length() > i*3 + 2)
                bValue = inputInt[i*3 + 2];


            rValue = inputInt[i*3];


            //now put this back in the new pixels array
            int thisoutput = (rValue << 16) + (gValue << 8) + bValue;
            encodedPixels[location] = thisoutput;

            //System.out.println(" new rValue = " + rValue + " gValue = " + gValue + " bValue " + bValue + " encodedPixel" + thisoutput);
        }


        String FileName = "Image" + (int)(Math.random()*7000) + ".png";

        File foo = new File(FileName);
        try{
            ImageIO.write(imageEncoded, "PNG", foo);
        }


        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Saved the encoded picture as " + FileName);
    }

    public static void decodePic()
    {
        System.out.println("What is the path of the original picture?");
        String pathname1 = scanner.nextLine();
        System.out.println("What is the path of the encoded picture?");
        String pathname2 = scanner.nextLine();

        int startingPoint = 0;

       /* for(int i = 0; i < pixels.length; i++)
        {
            //System.out.println(pixels[i] + " k " + encodedPixels[i]);
            if(pixels[i] != encodedPixels[i])
            {
                startingPoint = i;
                break;
            }
        }*/

        //System.out.println("starting decoding process at " + startingPoint + " anchor at " + anchor );

        //now find out how many pixels we have to decode
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(pathname1));
        } catch (IOException e) {
            System.out.print(e);
        }

        int[] pixels = image.getRGB(0,0, image.getWidth(),image.getHeight(), null, 0, image.getWidth());

        BufferedImage imageEncoded = null;

        try {
            imageEncoded = ImageIO.read(new File(pathname2));
        } catch (IOException e) {
            System.out.print(e);
        }

        int[] encodedPixels =  imageEncoded.getRGB(0,0, image.getWidth(),image.getHeight(), null, 0, image.getWidth());
        ;


        String output = "";

        int numPixels = 0;
        for(int f =0; f < encodedPixels.length; f++)
        {
            if(pixels[f] != encodedPixels[f])
            {
                numPixels++;
            }

        }

        int message[] = new int[numPixels];
        int route = 0;

        System.out.println(" decoding " + numPixels + " pixels");

        for(int f = 0; f < encodedPixels.length;f++)
        {
            if(pixels[f] != encodedPixels[f])
            {
                message[route] = encodedPixels[f];
                route++;
            }
        }


        for(int f = 0; f < message.length; f++)
        {
            int pixelValue = message[f];
            int rValue = (pixelValue & 0xff0000) >> 16;
            int gValue = (pixelValue & 0x00ff00) >> 8 ;
            int bValue = (pixelValue & 0x0000ff);
            //System.out.println("rValue = " + rValue + " gValue = " + gValue + " bValue = " + bValue);


            output += (char)rValue;
            output += (char)gValue;
            output += (char)bValue;
        }

        System.out.println("The decoded message is '" + output + "'");



    }

}
