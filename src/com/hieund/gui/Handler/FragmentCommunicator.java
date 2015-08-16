package com.hieund.gui.Handler;

import java.util.ArrayList;
import java.util.Vector;

import com.hieund.gui.Object.Nodes;
import com.hieund.gui.Object.ResultSearchObject;

public interface FragmentCommunicator {
	public void passData (ArrayList<ResultSearchObject> resultText, ArrayList<String> resultGeo, ArrayList<Vector<Nodes>> arrayNodeAns);
}
