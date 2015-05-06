package com.zackmatthews.binarytreevisualizationproject;

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



        for(int i = 0; i < depth; i++) {
            int offsetrx = Node.NODE_RIGHT_rX_OFFSET(child.radius) * 3, offsetlx = Node.NODE_LEFT_lX_OFFSET(child.radius) * 3;


            if (child.type.equals(Node.NODE_TYPE_LEFT_CHILD)) {
                offsetlx = Node.NODE_LEFT_lX_OFFSET(child.radius) - (int) child.radius;
                offsetrx = Node.NODE_LEFT_rX_OFFSET(child.radius) + (int) child.radius / 3;
            } else if (child.type.equals(Node.NODE_TYPE_RIGHT_CHILD)) {
                offsetlx = Node.NODE_RIGHT_lX_OFFSET(child.radius) - (int) child.radius / 3;

                offsetrx = Node.NODE_RIGHT_rX_OFFSET(child.radius) + (int) child.radius;
            }

            int leftVal = rand.nextInt(child.value / 2) + 1;
            int rightVal = rand.nextInt(child.value * 5) + child.value * 2;

            Node childLeft = insert(head, leftVal);
            Node childRight = insert(head, rightVal);
        }
}

    public Node insert(Node leaf, int key){
        if(key < leaf.value) {

            if(leaf.childLeft != null){
                insert(leaf.childLeft, key);
            }

            else{

                /*** PLACE HOLDER **/

                int offset_lx = Node.NODE_LEFT_lX_OFFSET(leaf.radius) * 3;



                if(leaf.type.equals(Node.NODE_TYPE_LEFT_CHILD)){
                    offset_lx = Node.NODE_LEFT_lX_OFFSET(leaf.radius) - (int)leaf.radius;

                }

                else if(leaf.type.equals(Node.NODE_TYPE_RIGHT_CHILD)){
                    offset_lx = Node.NODE_RIGHT_lX_OFFSET(leaf.radius) -(int)leaf.radius / 3;


                }


                leaf.childLeft = new Node(key, leaf.pos_x + offset_lx, leaf.pos_y + Node.NODE_Y_OFFSET(radius), radius );
                leaf.childLeft.type = Node.NODE_TYPE_LEFT_CHILD;

                return leaf.childLeft;
            }

        }

        else if(key >= leaf.value) {

            if (leaf.childRight != null) {
                insert(leaf.childRight, key);
            } else {

                /*** PLACE HOLDER **/

                int offset_rx = Node.NODE_RIGHT_rX_OFFSET(leaf.radius) * 3;


                if (leaf.type.equals(Node.NODE_TYPE_LEFT_CHILD)) {

                    offset_rx = Node.NODE_LEFT_rX_OFFSET(leaf.radius) + (int) leaf.radius / 3;
                } else if (leaf.type.equals(Node.NODE_TYPE_RIGHT_CHILD)) {


                    offset_rx = Node.NODE_RIGHT_rX_OFFSET(leaf.radius) + (int) leaf.radius;
                }

                leaf.childRight = new Node(key, leaf.pos_x + offset_rx, leaf.pos_y + Node.NODE_Y_OFFSET(radius), radius);
                leaf.childRight.type = Node.NODE_TYPE_RIGHT_CHILD;

                return leaf.childRight;
            }
        }

        return null;

    }

}
