/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hieund.Util;


import java.util.ArrayList;
import java.util.Vector;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.hieund.gui.Object.*;
/**
 *
 * @author Do Thanh An
 */
public class AStarAlgo {

    private Vector<TreeNode> OPEN = new Vector<TreeNode>();             //Tap cac nut con duoc sinh ra nhung chua xet
    private Vector<TreeNode> offspring = new Vector<TreeNode>();        //Tap cac trang thai con
    public Vector<Nodes> answer = new Vector<Nodes>();            //Loi giai bai toan
    public Vector<Stop> ansNodes = new Vector<Stop>();
    private double fmin;                       //f nho nhat trong cac nut cua OPEN
    private int minIndex;                   //chi so cua nut co f nho nhat trong OPEN
    //private boolean isOver;
    private String startPlace;
    private String endPlace;
    Stop startStop = null;
    Stop endStop = null;
    ArrayList<Lines> arrLine;
    ArrayList<Stop> arrayStop;
    int count;
    private static final int MAX_SIZE=8000;

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public AStarAlgo() {
    }

    public AStarAlgo(String startPlace, String endPlace, ArrayList<Stop> arrStop) {
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.arrayStop = arrStop;
        count=0;
    }

    public AStarAlgo(ArrayList<Stop> arrStop, Stop startStop, Stop endStop) {
        this.startStop = startStop;
        this.endStop = endStop;
        this.arrayStop = arrStop;
        count=0;
    }

    public void saveAnswer(TreeNode n) {
        if (n.getParent() != null) {
            saveAnswer(n.getParent());
            answer.add(n.getCurrentNode());
        } else {
            answer.add(n.getCurrentNode());
        }
    }

    /*
     * OPEN is the Vector which include nodes not been traverse
     * answer is the solution 
     */
//    public Nodes algorithm() {
//
//        answer.clear();
//        if (startStop == null) {
//            startStop = searchArrayStop(startPlace);
//        }
//        if (endStop == null) {
//            endStop = searchArrayStop(endPlace);
//        }
//        if (startStop == null || endStop == null) {
//            return null;
//        } else {
//            arrLine = new ArrayList<Lines>();
//            ArrayList<Nodes> tempArrayNode = startStop.getArrayNode();
//            for (int i = 0; i < tempArrayNode.size(); i++) {
//                TreeNode newTreeNode = new TreeNode(tempArrayNode.get(i), 0, 0);
//                newTreeNode.setWeight(heuristicFuntion(startStop, endStop));
//                newTreeNode.setParent(null);
//                OPEN.add(newTreeNode);
//            }
//
//            for (int i = 0; i < endStop.getArrayNode().size(); i++) {
//                arrLine.add(endStop.getArrayNode().get(i).getLine());
//            }
//
//            while (true) {
//                if (OPEN.isEmpty()) {
//                    //System.out.println("12345");
//                    OPEN.clear();
//                    if (offspring != null) {
//                        offspring.clear();
//                    }
//                    break;
//                }
//
//                //find the min index and the minimum weight of OPEN's nodes
//                minIndex = 0;
//                fmin = OPEN.elementAt(minIndex).getWeight();
//                int openSize = OPEN.size();
//                for (int i = 0; i < openSize; i++) {
//                    if (OPEN.elementAt(i).getWeight() < fmin) {
//                        minIndex = i;
//                        fmin = OPEN.elementAt(i).getWeight();
//                    }
//                }
//
//
//                TreeNode newTreeNode = OPEN.elementAt(minIndex);
//                OPEN.remove(newTreeNode);
//                count++;
//                //if you reach end place, save the answer and return
//                if (newTreeNode.getCurrentNode().getStopName().equals(endStop.getName())) {
//                    System.out.println("reach end place");
//                    saveAnswer(newTreeNode);
//                    offspring.clear();
//                    return null;
//                }else if(isNear(newTreeNode.getCurrentNode(),endStop)){
//                	System.out.println("reach end place");
//                	Log.i("Near", "TRUE");
//
//                    saveAnswer(newTreeNode);
//                    offspring.clear();
//                    return newTreeNode.getCurrentNode();
//                }
//                if (count==MAX_SIZE) {
//                    answer = null;
//                    return null;
//                }
//                //get offspring of newTreeNode
//                offspring = generateOffspring(newTreeNode);
//                
//                if (offspring != null) {
//                    int index = checkLine(offspring.get(0));
//                    if (index >= 0) {
//                        TreeNode endTreeNode = new TreeNode(endStop.getArrayNode().get(index), offspring.get(0));
//                        System.out.println("reach end place (1)");
//                        saveAnswer(endTreeNode);
//                        offspring.clear();
//                        return null;
//                    }
//                    int offspringSize = offspring.size();
//                    //add offspring into OPEN
//                    for (int i = 0; i < offspringSize; i++) {
//                        OPEN.add(0, offspring.elementAt(i));
//                    }
//
//                }
//
//
//            }
//        }
//		return null;
//    }
//    
    public void algorithm() {

        answer.clear();
        if (startStop == null) {
            startStop = searchArrayStop(startPlace);
        }
        if (endStop == null) {
            endStop = searchArrayStop(endPlace);
        }
        if (startStop == null || endStop == null) {
            return ;
        } else {
            arrLine = new ArrayList<Lines>();
            ArrayList<Nodes> tempArrayNode = startStop.getArrayNode();
            for (int i = 0; i < endStop.getArrayNode().size(); i++) {
                arrLine.add(endStop.getArrayNode().get(i).getLine());
            }
            
            for (int i = 0; i < tempArrayNode.size(); i++) {
                TreeNode newTreeNode = new TreeNode(tempArrayNode.get(i), 0, 0);
                newTreeNode.setWeight(heuristicFuntion(startStop, endStop));
                newTreeNode.setParent(null);
                OPEN.add(newTreeNode);
                int index = checkLine(newTreeNode);
				if (index >= 0)
				{
					TreeNode endTreeNode = new TreeNode(endStop.getArrayNode().get(index), newTreeNode);

					saveAnswer(endTreeNode);
					if (CheckValidAnswer())
					{
						OPEN.clear();
						return;
					}
					else
					{
						answer.clear();
						OPEN.clear();
						return;
					}
				}
            }
            

            while (true) {
                if (OPEN.isEmpty()) {
                    //System.out.println("12345");
                    OPEN.clear();
                    if (offspring != null) {
                        offspring.clear();
                    }
                    break;
                }

                //find the min index and the minimum weight of OPEN's nodes
                minIndex = 0;
                fmin = OPEN.elementAt(minIndex).getWeight();
                int openSize = OPEN.size();
                for (int i = 0; i < openSize; i++) {
                    if (OPEN.elementAt(i).getWeight() < fmin) {
                        minIndex = i;
                        fmin = OPEN.elementAt(i).getWeight();
                    }
                }


                TreeNode newTreeNode = OPEN.elementAt(minIndex);
                OPEN.remove(newTreeNode);
                count++;
                //if you reach end place, save the answer and return
                if (newTreeNode.getCurrentNode().getStopName().equals(endStop.getName())) {
                    System.out.println("reach end place");
                    saveAnswer(newTreeNode);
                    if (CheckValidAnswer())
					{
						offspring.clear();
						return;
					}
					else
					{
						answer.clear();
						offspring.clear();
						return;
					}
                }
                if (count==MAX_SIZE) {
                    answer = null;
                    return;
                }
                //get offspring of newTreeNode
                offspring = generateOffspring(newTreeNode);
                
                if (offspring != null) {
                    int index = checkLine(offspring.get(0));
                    if (index >= 0) {
                        TreeNode endTreeNode = new TreeNode(endStop.getArrayNode().get(index), offspring.get(0));
                        System.out.println("reach end place (1)");
                        saveAnswer(endTreeNode);
						if (CheckValidAnswer())
						{
							offspring.clear();
							return;
						}
						else 
						{
							answer.clear();
							offspring.clear();
							return;
						}
                    }
                    int offspringSize = offspring.size();
                    //add offspring into OPEN
                    for (int i = 0; i < offspringSize; i++) {
                        OPEN.add(0, offspring.elementAt(i));
                    }

                }


            }
        }
		return;
    }

    private boolean isNear(Nodes currentNode, Stop endStop2) {
		// TODO Auto-generated method stub
    	float[] result = new float[3];
    	Location.distanceBetween(currentNode.getGeo().latitude, currentNode.getGeo().longitude
    			, endStop2.getGeo().latitude, endStop2.getGeo().longitude, result);
    	Log.i("Near", ""+result[0]);

    	if (result[0]<200)
    		return true;
    	else 
		return false;
	}

	//get OffSpring of a treenode
    public Vector<TreeNode> generateOffspring(TreeNode n) {

        ArrayList<Nodes> newArrayNodes;
        Stop newStop, currentStop;
        currentStop = searchArrayStop(n.getCurrentNode().getStopName());
        Vector<TreeNode> newVector = new Vector<TreeNode>();

        if (n.getCurrentNode().getNextNode() != null) {
            String stop_name = n.getCurrentNode().getNextNode().getStopName();
            newStop = searchArrayStop(stop_name);
            newArrayNodes = newStop.getArrayNode();
        } else {
            return null;
        }
        int newSize = newArrayNodes.size();
        for (int i = 0; i < newSize; i++) {
            TreeNode newTreeNode;
            try {
                newTreeNode = new TreeNode(newArrayNodes.get(i), n);

//                if (Lines.lineEqual(newTreeNode.getCurrentNode().getLine(), n.getCurrentNode().getLine())
//                        && newTreeNode.getCurrentNode().getStopName().equals(n.getCurrentNode().getStopName())) {
                if (newTreeNode.getCurrentNode().getStopName().equals(n.getCurrentNode().getStopName())) {
                    continue;
                } else if (checkLine(newTreeNode) >= 0) {
                    newTreeNode.setParent(n);
                    newVector.add(0, newTreeNode);
                    return newVector;
                }  
                else if (!(newTreeNode.getCurrentNode().getLine().getCode().equals(n.getCurrentNode().getLine().getCode()))
                        || (newTreeNode.getCurrentNode().getLine().getDirection()
                        != n.getCurrentNode().getLine().getDirection()
                        && newTreeNode.getCurrentNode().getLine().getCode().equals(n.getCurrentNode().getLine().getCode()))) {
                    newTreeNode.setNumberOfChange(n.getNumberOfChange() + 1);
                    newTreeNode.setPreviousDistance(n.getPreviousDistance()
                            + BusFunction.convertToDistance(currentStop.getGeo(), newStop.getGeo()));
                    newTreeNode.setWeight(newTreeNode.getNumberOfChange() * 6.25 + newTreeNode.getPreviousDistance() + heuristicFuntion(newStop, endStop));
                    newTreeNode.setParent(n);
                    newVector.add(newTreeNode);
                } 
//                else {
//                    newTreeNode.setNumberOfChange(n.getNumberOfChange());
//                    newTreeNode.setPreviousDistance(n.getPreviousDistance()
//                            + BusFunction.convertToDistance(newStop.getGeo(), currentStop.getGeo()));
//                    newTreeNode.setWeight(newTreeNode.getNumberOfChange() * 6.25 + newTreeNode.getPreviousDistance() + heuristicFuntion(newStop, endStop));
//
//                    newTreeNode.setParent(n);
//                    newVector.add(newTreeNode);
//                }
                else
				{
					newTreeNode.setNumberOfChange(n.getNumberOfChange());
					newTreeNode.setPreviousDistance(n.getPreviousDistance() + BusFunction.convertToDistance(newStop.getGeo(), currentStop.getGeo()));
					newTreeNode.setWeight(newTreeNode.getNumberOfChange() * 6.25 + newTreeNode.getPreviousDistance() + heuristicFuntion(newStop, endStop));
					newTreeNode.setParent(n);
					newVector.add(newTreeNode);
				}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newVector;
    }
    public boolean CheckValidAnswer()
	{
//		for (int i = 0; i < answer.size() - 1; i++)
//		{
//			Nodes nxtNode = answer.get(i).getNextNode();
//			if (nxtNode == null)
//			{
//				return false;
//			}
//
//			if (nxtNode.getStopName().equals(answer.lastElement().getStopName()))
//			{
//				break;
//			}
//			while (!nxtNode.getStopName().equals(answer.get(i+1).getStopName()))
//			{
//				nxtNode = nxtNode.getNextNode();
//				if (nxtNode == null)
//				{
//					return false;
//				}
//			}
//		}
		return true;
	}

    public double heuristicFuntion(Stop currentStop, Stop endStop) {
        LatLng geo1 = currentStop.getGeo();
        LatLng geo2 = endStop.getGeo();
        return BusFunction.convertToDistance(geo1, geo2);
    }

    public int checkLine(TreeNode n) {
        int result = -1;
        for (int i = 0; i < arrLine.size(); i++) {
            if (arrLine.get(i).getCode().equals( n.getCurrentNode().getLine().getCode())) {
                result = i;
                break;
            }
        }
        return result;
    }

    public int getCount() {
        return count;
    }

    public Stop searchArrayStop(String name) {
		for (int i = 0; i < arrayStop.size(); i++) {
			if (arrayStop.get(i).getName().equals(name)) {
				return arrayStop.get(i);
			}
		}
		return null;
	}
      
}
