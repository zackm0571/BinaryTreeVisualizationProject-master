package com.zackmatthews.binarytreevisualizationproject;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by c1bank on 4/9/15.
 */
public class NodeManager {


    private Node head;

 //   public List<Node> nodes = new ArrayList<Node>();

    public NodeManager(Node head){
        this.head = head;
    }


    public static float radius = 50.0f; //hardcoded for test

    Random rand = new Random();

    public void populateTree(Node child, int depth){


        radius = RenderView.screenSize.x / 30;

        if(head == null || child == null){return;}

        if(depth > 0){


            int offsetrx  = Node.NODE_RIGHT_rX_OFFSET(child.radius) * 3
                    , offsetlx = Node.NODE_LEFT_lX_OFFSET(child.radius) * 3;



            if(child.type.equals(Node.NODE_TYPE_LEFT_CHILD)){
                offsetlx = Node.NODE_LEFT_lX_OFFSET(child.radius) - (int)child.radius;
                offsetrx = Node.NODE_LEFT_rX_OFFSET(child.radius) + (int)child.radius / 3;
            }

            else if(child.type.equals(Node.NODE_TYPE_RIGHT_CHILD)){
                offsetlx = Node.NODE_RIGHT_lX_OFFSET(child.radius) -(int)child.radius / 3;

                offsetrx = Node.NODE_RIGHT_rX_OFFSET(child.radius) + (int)child.radius;
            }

            int leftVal = rand.nextInt(child.value / 2) + 1;


            Node childLeft = new Node(leftVal, child.pos_x + offsetlx, child.pos_y + Node.NODE_Y_OFFSET(child.radius), radius);
            childLeft.type = Node.NODE_TYPE_LEFT_CHILD;
            child.childLeft = childLeft;


            int rightVal = rand.nextInt(child.value * 5) + child.value * 2;
            Node childRight  = new Node(rightVal, child.pos_x + offsetrx , child.pos_y + Node.NODE_Y_OFFSET(child.radius), radius);
            childRight.type = Node.NODE_TYPE_RIGHT_CHILD;
            child.childRight = childRight;


         //   nodes.add(childRight);
           // nodes.add(childLeft);

            depth -= 2;

            if(depth > (depth / 2) - 1){
                populateTree(childLeft, depth);
            }

            populateTree(childRight, depth);

        }





    }

}
