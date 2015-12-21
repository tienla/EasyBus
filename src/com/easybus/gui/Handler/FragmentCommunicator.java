package com.easybus.gui.Handler;

import java.util.ArrayList;
import java.util.Vector;

import com.easybus.gui.Object.Nodes;
import com.easybus.gui.Object.ResultSearchObject;

//Communication between fragement
public interface FragmentCommunicator {
	public void passData (ArrayList<ResultSearchObject> resultText, ArrayList<String> resultGeo, ArrayList<Vector<Nodes>> arrayNodeAns);
}
