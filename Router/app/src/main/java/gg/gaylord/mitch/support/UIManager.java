package gg.gaylord.mitch.support;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import gg.gaylord.mitch.network.AdjacencyTable;
import gg.gaylord.mitch.network.AdjacencyTableEntry;
import gg.gaylord.mitch.network.LL1Daemon;
import gg.gaylord.mitch.network.LL2Daemon;
import gg.gaylord.mitch.network.LL2P;
import gg.gaylord.mitch.router.R;

/**
 * Created by mitchell.gaylord on 1/21/2016.
 */
public class UIManager {

    Activity parentActivity;
    Context context;
    Factory myFactory;
    LL1Daemon layer1Daemon;
    LL2Daemon layer2Daemon;
    ArrayAdapter<AdjacencyTableEntry> adjacencyAdapter;

    List<AdjacencyTableEntry> adjacencyList;

    /* Screen Widgets */
    private TextView ll2pCRCTextView;
    private TextView ll2pPayloadTextView;
    private TextView ll2pDestinationAddressTextView;
    private TextView ll2pSourceAddressTextView;
    private TextView ll2pTypeTextView;

    private TextView ll2pLabelAddressTextView;
    private TextView ipAddressLabelTextView;
    private EditText ipAddressEditText;
    private EditText ll2pAddressEditText;
    private ListView adjacencyTableListView;
    private Button addAdjacencyButton;
    private Button clearAdjacencyButton;

    private TextView ll2pEchoRequestPayloadTextView;
    private EditText ll2pEchoRequestPayloadEditText;

    public UIManager(){

    }

    public void createWidgets(){
        ll2pCRCTextView = (TextView)   parentActivity.findViewById(R.id.ll2pCRCTextView);
        ll2pDestinationAddressTextView = (TextView) parentActivity.findViewById(R.id.ll2pDestinationAddressTextView);
        ll2pPayloadTextView = (TextView) parentActivity.findViewById(R.id.ll2pPayloadTextView);
        ll2pSourceAddressTextView = (TextView) parentActivity.findViewById(R.id.ll2pSourceAddressTextView);
        ll2pTypeTextView = (TextView) parentActivity.findViewById(R.id.ll2pTypeTextView);

        ll2pLabelAddressTextView = (TextView) parentActivity.findViewById(R.id.ll2pAddressLabelTextView);
        ipAddressLabelTextView = (TextView) parentActivity.findViewById(R.id.ipAddressLabelTextView);
        ipAddressEditText = (EditText) parentActivity.findViewById(R.id.ipAddressEditText);
        ll2pAddressEditText = (EditText) parentActivity.findViewById(R.id.ll2pAddressEditText);
        adjacencyTableListView = (ListView) parentActivity.findViewById(R.id.adjacencyTableListView);
        addAdjacencyButton = (Button) parentActivity.findViewById(R.id.addAdjacencyButton);
        clearAdjacencyButton = (Button) parentActivity.findViewById(R.id.clearAdjacencyButton);

        ll2pEchoRequestPayloadTextView = (TextView) parentActivity.findViewById(R.id.ll2pEchoRequestPayloadTextView);
        ll2pEchoRequestPayloadEditText = (EditText) parentActivity.findViewById(R.id.ll2pEchoRequestPayloadEditText);

        addAdjacencyButton.setOnClickListener(addAdjacency);
        clearAdjacencyButton.setOnClickListener(clearAdjacency);
        adjacencyTableListView.setOnItemClickListener(sendToLL2P);
        adjacencyTableListView.setOnItemLongClickListener(removeAdjacency);
    }

    public void getOjbectReferences(Factory factory){
        myFactory = factory;
        parentActivity = factory.getParentActivity();
        context = parentActivity.getBaseContext();
        layer1Daemon = factory.getLl1Daemon();
        layer2Daemon = factory.getLl2Daemon();

        /* Creates all screen widgets */
        createWidgets();

        listToDisplay();
    }

    private AdapterView.OnItemClickListener sendToLL2P = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapter, View viewObject, int positionTapped, long id){
            AdjacencyTableEntry target = adjacencyList.get(positionTapped);
            String payload = ll2pEchoRequestPayloadEditText.getText().toString();
            Integer ll2p = target.getLl2pAddress();
            LL2P newFrame = new LL2P(Integer.toHexString(ll2p), NetworkConstants.MY_LL2P_ADDRESS, NetworkConstants.LL2P_ECHO_REQUEST_TYPE, payload);

            try{
                layer1Daemon.sendLL2PFrame(newFrame);
            } catch (LabException e){
                e.printStackTrace();
            }

            //layer2Daemon.sendLL2PEchoRequest(payload, ll2p);
        }
    };

    private AdapterView.OnItemLongClickListener removeAdjacency = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View viewObject, int positionTapped, long id) {
            AdjacencyTableEntry target = adjacencyList.get(positionTapped);
            Integer ll2p = target.getLl2pAddress();
            boolean removed = false;
            try{
                layer1Daemon.removeAdjacency(ll2p);
                resetAdjacencyListAdapter();
                removed = true;
                return removed;
            } catch (LabException e){
                e.printStackTrace();
            }

            return removed;
        }
    };

    public View.OnClickListener clearAdjacency = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ll2pAddressEditText.setText("");
            ipAddressEditText.setText("");
        }
    };

    public View.OnClickListener addAdjacency = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            String tempLL2P;
            String ipAddress;

            tempLL2P = ll2pAddressEditText.getText().toString();
            ipAddress = ipAddressEditText.getText().toString();

            Integer ll2pAddress = Integer.parseInt(tempLL2P,16);

            layer1Daemon.addAdjacency(ll2pAddress, ipAddress);
            listToDisplay();
            resetAdjacencyListAdapter();
        }

    };

    private void resetAdjacencyListAdapter(){
        adjacencyList = layer1Daemon.getAdjacencyList();
        adjacencyAdapter.clear();
        Iterator<AdjacencyTableEntry> listIterator = adjacencyList.iterator();
        while(listIterator.hasNext()){
            adjacencyAdapter.add(listIterator.next());
        }
    }

    public void raiseToast(String message,int length){
        Toast.makeText(context, message, length).show();
    }

    public void raiseToast(String message){
        raiseToast(message, Toast.LENGTH_SHORT);
    }

    public void setAdapter(ArrayAdapter<AdjacencyTableEntry> adjacencyAdapter) {
        adjacencyAdapter = adjacencyAdapter;
    }

    public void listToDisplay(){
        adjacencyList = layer1Daemon.getAdjacencyList();

        adjacencyAdapter = new ArrayAdapter<AdjacencyTableEntry>(parentActivity,android.R.layout.simple_list_item_1, adjacencyList);

        adjacencyTableListView.setAdapter(adjacencyAdapter);

        resetAdjacencyListAdapter();
    }



    public void updateLL2PDisplay(LL2P ll2pObject){
        ll2pCRCTextView.setText(ll2pObject.getCRCHexString());
        ll2pDestinationAddressTextView.setText(ll2pObject.getDestAddressHexString());
        ll2pPayloadTextView.setText(ll2pObject.getPayloadHexString());
        ll2pTypeTextView.setText(ll2pObject.getTypeHexString());
        ll2pSourceAddressTextView.setText(ll2pObject.getSrcAddressHexString());
    }
}
