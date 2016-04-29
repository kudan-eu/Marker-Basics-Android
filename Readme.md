#Kudan Tutorials - Marker Basics
----

This tutorial will take you through the basics of getting started with KudanAR, leading on from our previous tutorial which described how to integrate our framework into an Android project. If you have not already set up a project with an ARActivity, I suggest you check it out before going any further.

This tutorial utilises bundled assets so ensure that you have imported the correct assets into your project. 

For this sample we have used:

* Marker	: spaceMarker.jpg
* Image 	: Augmentation: eyebrow.png
* Video	: Augmentation: waves.mp4
* Alpha video augmentation: kaboom.mp4
* Model 	: bloodhoud.armodel / bloodhound.jet 
* Model Texture : bloodhound.png

All of which can be downloaded [here](https://wiki.kudan.eu/Tutorials/assets.zip).

Note: For Android 6.0 and above you are going to ensure that you have requested the correct permissions otherwise your application will crash.

##Setting Up and Image Trackable

To create an Image Trackable, you are first going to need an image to track. We do not put any restrictions as to what image format you use as long as it is supported natively. For information about what creates a good marker please read our blog post:

[What makes a good marker? ](https://wiki.kudan.eu/What_Makes_a_Good_Marker%3F)

Notes: Common problems associated with a poor marker are your augmentation appears to twitch or shake.

~~~java
// Initialise image trackable
trackable = new ARImageTrackable("StarWars");
trackable.loadFromAsset("spaceMarker.jpg");

// Get instance of image tracker manager
ARImageTracker trackableManager = ARImageTracker.getInstance();

// Add image trackable to image tracker manager
trackableManager.addTrackable(trackable);
~~~

##Adding content to an Image Trackable
To add content to an Image Trackable you need to transform the content you have in the corresponding ARNode and add that to the trackable's world (the 3D space surrounding the marker). Kudan has 4 different ARNode subclasses:
 
* ARImageNode
* ARVideoNode
* ARAlphaVideoNode
* ARModelNode

Note: When adding any AR content to your application you should consider adding it on the background thread. This will help prevent the camera feed from stalling.

###Image Nodes 

Images are displayed using the ARImageNode class. These are initialised with an image. This image can use any format that is supported by the device's operating system.


~~~java
// Initialise image node
ARImageNode imageNode = new ARImageNode("eyebrow.png");

// Add image node to image trackable
trackable.getWorld().addChild(imageNode);
~~~

###Video Nodes

Videos are displayed using ARVideoNode class. Video nodes are initialised using a video file on iOS and a video texture initialised from a video file for Android. The video file can be any format supported by the native device.

~~~java
// Initialise video texture
ARVideoTexture videoTexture = new ARVideoTexture();
videoTexture.loadFromAsset("waves.mp4");

// Initialise video node with video texture
ARVideoNode videoNode = new ARVideoNode(videoTexture);

//Add video node to image trackable
trackable.getWorld().addChild(videoNode);
~~~    

###Alpha Video Nodes

Alpha videos are videos with a transparency channel and can be created through our Toolkit using a set of transparent PNGs. Alpha videos are displayed using the ARAlphaVideo class. They are initialised the same as a video node. 

~~~java

// Initialise video texture
ARVideoTexture videoTexture = new ARVideoTexture();
videoTexture.loadFromAsset("kaboom.mp4");

// Initialise alpha video node with video texture
ARAlphaVideoNode alphaVideoNode = new ARAlphaVideoNode(videoTexture);

// Add alpha video node to image trackable
trackable.getWorld().addChild(alphaVideoNode);

~~~

###Model Nodes

Models are displayed using the ARModelNode class. They are created in a two steps. First the model is imported using the ARModelImporter class. A texture material is then applied to the model's individual mesh nodes. This can be either a colour material, texture material or a light material.

For more information on using 3D models with Kudan please check out our Wiki entry:

[3D Models](https://wiki.kudan.eu/3D_Models)

Note: If you do not add lighting to your ARLightMaterial your material will show up as black. 

~~~java
// Import model
ARModelImporter modelImporter = new ARModelImporter();
modelImporter.loadFromAsset("ben.jet");
ARModelNode modelNode = (ARModelNode)modelImporter.getNode();

// Load model texture
ARTexture2D texture2D = new ARTexture2D();
texture2D.loadFromAsset("bigBenTexture.png");

// Apply model texture file to model texture material and add ambient lighting
ARLightMaterial material = new ARLightMaterial();
material.setTexture(texture2D);
material.setAmbient(0.8f, 0.8f, 0.8f);

// Apply texture material to models mesh nodes
for (ARMeshNode meshNode : modelImporter.getMeshNodes()){
    meshNode.setMaterial(material);
}

// Add model node to image trackable
trackable.getWorld().addChild(modelNode);
~~~

###Scaling

If the image/video/model you wish to add to the marker isn't the same size as the marker, you may wish to scale your ARNode. Providing your video/image is the same aspect ratio as your trackable you can divide one width/height from the other to get the correct scale. This value can then be used to scale your nodes.

Note: This tutorial scales using a uniform value, although you are able to scale your x, y and z axis separately.
  
~~~java
// Image scale
ARTextureMaterial textureMaterial = (ARTextureMaterial)imageNode.getMaterial();
float scale = trackable.getWidth() / textureMaterial.getTexture().getWidth();
imageNode.scaleByUniform(scale);
        
// Video scale
float scale = trackable.getWidth() / videoTexture.getWidth();
videoNode.scaleByUniform(scale);

// Alpha video scale
float scale = trackable.getWidth() / videoTexture.getWidth();
alphaVideoNode.scaleByUniform(scale);

~~~


###Content visibility

Each node has a boolean value which can be set to determine whether or not the node is displayed. This is useful when you have multiple nodes attached to a marker and you do not wish to display them all at once. This can be set using:

~~~java
// Hide image node
imageNode.setVisible(false);
~~~