package eu.kudan.kudansamples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARAlphaVideoNode;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARRenderer;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARTextureMaterial;
import eu.kudan.kudan.ARVideoNode;
import eu.kudan.kudan.ARVideoTexture;
import eu.kudan.kudan.ARView;

public class ARCameraActivity extends ARActivity {

    private ARImageTrackable trackable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
    }

    public void setup() {
        addImageTrackable();
        addImageNode();
        addVideoNode();
        addAlphaVideoNode();
        addModelNode();
    }

    private void addImageTrackable() {

        // Initialise image trackable
        trackable = new ARImageTrackable("StarWars");
        trackable.loadFromAsset("spaceMarker.jpg");

        // Get instance of image tracker manager
        ARImageTracker trackableManager = ARImageTracker.getInstance();

        // Add image trackable to image tracker manager
        trackableManager.addTrackable(trackable);
    }

    private void addModelNode() {
        // Import model
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset("ben.jet");
        ARModelNode modelNode = (ARModelNode)modelImporter.getNode();

        // Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("bigBenTexture.png");

        // Apply model texture to model texture material
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);

        // Apply texture material to models mesh nodes
        for(ARMeshNode meshNode : modelImporter.getMeshNodes()){
            meshNode.setMaterial(material);
        }


        modelNode.rotateByDegrees(90,1,0,0);
        modelNode.scaleByUniform(0.25f);

        // Add model node to image trackable
        trackable.getWorld().addChild(modelNode);
        modelNode.setVisible(true);

    }

    private void addAlphaVideoNode() {

        // Initialise video texture
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset("kaboom.mp4");

        // Initialise alpha video node with video texture
        ARAlphaVideoNode alphaVideoNode = new ARAlphaVideoNode(videoTexture);

        // Add alpha video node to image trackable
        trackable.getWorld().addChild(alphaVideoNode);

        // Alpha video scale
        float scale = trackable.getWidth() / videoTexture.getWidth();
        alphaVideoNode.scaleByUniform(scale);

        alphaVideoNode.setVisible(false);

    }

    private void addVideoNode() {

        // Initialise video texture
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset("waves.mp4");

        // Initialise video node with video texture
        ARVideoNode videoNode = new ARVideoNode(videoTexture);

        //Add video node to image trackable
        trackable.getWorld().addChild(videoNode);

        // Video scale
        float scale = trackable.getWidth() / videoTexture.getWidth();
        videoNode.scaleByUniform(scale);

        videoNode.setVisible(false);

    }

    private void addImageNode() {

        // Initialise image node
        ARImageNode imageNode = new ARImageNode("eyebrow.png");

        // Add image node to image trackable
        trackable.getWorld().addChild(imageNode);

        // Image scale
        ARTextureMaterial textureMaterial = (ARTextureMaterial)imageNode.getMaterial();
        float scale = trackable.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);

        // Hide image node
        imageNode.setVisible(false);

    }


    public void addModelButtonPressed(View view) {

        hideAll();
        trackable.getWorld().getChildren().get(3).setVisible(true);
    }

    public void addAlphaButtonPressed(View view) {

        hideAll();
        trackable.getWorld().getChildren().get(2).setVisible(true);

    }

    public void addVideoButtonPressed(View view) {

        hideAll();
        trackable.getWorld().getChildren().get(1).setVisible(true);
    }

    public void addImageButtonPressed(View view) {
        hideAll();
        trackable.getWorld().getChildren().get(0).setVisible(true);
    }


    private void hideAll(){
       List<ARNode> nodes =  trackable.getWorld().getChildren();
        for(ARNode node : nodes){
            node.setVisible(false);
        }
    }
}
